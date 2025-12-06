package application;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import logic.board.Board;
import logic.candy.Candy;
import logic.candy.CandyColor;
import logic.candy.CandyType;
import logic.controller.GameController; // เช็ค package
import logic.utils.Point;

import java.util.Set;

public class Main extends Application {

    private static final int ROWS = 9;
    private static final int COLS = 9;
    private static final int TILE_SIZE = 60;

    private GameController controller;
    private GridPane gridPane;
    private Label scoreLabel;

    private boolean isAnimating = false;
    private int selectedRow = -1;
    private int selectedCol = -1;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        controller = new GameController(ROWS, COLS);

        BorderPane root = new BorderPane();
        gridPane = new GridPane();
        scoreLabel = new Label("Score: 0");
        scoreLabel.setStyle("-fx-font-size: 24px; -fx-padding: 10; -fx-font-weight: bold;");

        root.setCenter(gridPane);
        root.setTop(scoreLabel);

        updateView(null);

        Scene scene = new Scene(root, COLS * TILE_SIZE, ROWS * TILE_SIZE + 60);
        primaryStage.setTitle("Candy Crush Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateView(Set<Point> hiddenCandies) {
        gridPane.getChildren().clear();
        Board board = controller.getBoard();

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                StackPane tile = new StackPane();
                tile.setPrefSize(TILE_SIZE, TILE_SIZE);

                Rectangle bg = new Rectangle(TILE_SIZE, TILE_SIZE);
                bg.setFill((r + c) % 2 == 0 ? Color.LIGHTGRAY : Color.DARKGRAY);
                bg.setStroke(Color.BLACK);
                tile.getChildren().add(bg);

                Candy candy = board.getCandy(r, c);
                // เช็คว่าต้องซ่อนมั้ย (Visual Trick)
                boolean shouldHide = (hiddenCandies != null && hiddenCandies.contains(new Point(r, c)));

                if (candy != null && !shouldHide) {
                    Circle circle = new Circle(TILE_SIZE / 2 - 8);
                    circle.setFill(getColor(candy.getColor()));
                    circle.setStroke(Color.BLACK);
                    circle.setStrokeWidth(2);

                    String typeText = getTypeCode(candy.getType());
                    Text text = new Text(typeText);
                    text.setFill(Color.WHITE);
                    text.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

                    tile.getChildren().addAll(circle, text);
                }

                if (r == selectedRow && c == selectedCol) {
                    bg.setStroke(Color.YELLOW);
                    bg.setStrokeWidth(4);
                }

                int finalR = r;
                int finalC = c;
                tile.setOnMouseClicked(e -> handleTileClick(finalR, finalC));

                gridPane.add(tile, c, r);
            }
        }
        scoreLabel.setText("Score: " + controller.getScore());
    }

    // ใน Main.java

    private void handleTileClick(int r, int c) {
        if (isAnimating) return;

        if (selectedRow == -1) {
            selectedRow = r;
            selectedCol = c;
            updateView(null);
        } else {
            if (Math.abs(selectedRow - r) + Math.abs(selectedCol - c) == 1) {
                // 1. สั่ง Controller สลับ (Backend สลับค่าใน Grid แล้ว)
                Set<Point> removes = controller.handleSwap(selectedRow, selectedCol, r, c);

                if (!removes.isEmpty()) {
                    isAnimating = true;

                    // ✅ [เพิ่มตรงนี้] PHASE 0: โชว์การสลับก่อน!
                    // วาดกระดานปกติ (ยังไม่ซ่อนอะไร) เพื่อให้เห็นว่าลูกอมสลับที่กันแล้ว
                    updateView(null);

                    // รอ 0.3 วินาที ให้ตาคนมองทัน
                    PauseTransition waitSwap = new PauseTransition(Duration.seconds(0.3));

                    waitSwap.setOnFinished(e -> {
                        // พอครบเวลา ค่อยเริ่มเข้าลูประเบิด (ที่จะทำให้เกิดรูโหว่)
                        runGameLoop(removes);
                    });
                    waitSwap.play();

                } else {
                    System.out.println("Invalid Move");
                    // เพิ่ม visual feedback ว่าสลับไม่ได้ตรงนี้ก็ได้
                }

                selectedRow = -1;
                selectedCol = -1;
                // updateView(null); <--- ลบบรรทัดนี้ออก หรือใส่ไว้ใน else ของ invalid move ก็ได้
                if (removes.isEmpty()) updateView(null); // อัปเดตเฉพาะถ้าไม่ระเบิด (ถ้าระเบิดเราจัดการใน waitSwap แล้ว)
            } else {
                selectedRow = r;
                selectedCol = c;
                updateView(null);
            }
        }
    }

    // 🔥 Animation Loop ที่ปลอดภัยขึ้น
    private void runGameLoop(Set<Point> removes) {
        try {
            // PHASE 1: ระเบิด (Visual Holes)
            updateView(removes);

            PauseTransition waitExplosion = new PauseTransition(Duration.seconds(0.5));
            waitExplosion.setOnFinished(e -> {
                try {
                    // PHASE 2: Physics (Backend ทำงาน)
                    controller.applyPhysics(removes);

                    updateView(null); // วาดของใหม่

                    PauseTransition waitGravity = new PauseTransition(Duration.seconds(0.5));
                    waitGravity.setOnFinished(e2 -> {
                        try {
                            // PHASE 3: Check Chain Reaction
                            Set<Point> newRemoves = controller.checkChainReaction();

                            if (!newRemoves.isEmpty()) {
                                // Recursion: วนลูปถ้ามีระเบิดต่อ
                                runGameLoop(newRemoves);
                            } else {
                                // จบ: ปลดล็อค
                                isAnimating = false;
                                System.out.println("--- Board Settled ---");
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            isAnimating = false; // กันค้างถ้าระเบิด error
                        }
                    });
                    waitGravity.play();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    isAnimating = false;
                }
            });
            waitExplosion.play();

        } catch (Exception e) {
            e.printStackTrace();
            isAnimating = false;
        }
    }

    // Helpers สีและ Type (เหมือนเดิม)
    private Color getColor(CandyColor c) {
        if (c == null) return Color.TRANSPARENT;
        switch (c) {
            case RED: return Color.RED;
            case GREEN: return Color.LIMEGREEN;
            case BLUE: return Color.DODGERBLUE;
            case YELLOW: return Color.GOLD;
            case PURPLE: return Color.MEDIUMPURPLE;
            default: return Color.BLACK;
        }
    }
    private String getTypeCode(CandyType t) {
        if(t == null) return "";
        switch (t) {
            case STRIPED_HOR: return "H";
            case STRIPED_VER: return "V";
            case BOMB: return "B";
            case COLOR_BOMB: return "C";
            default: return "";
        }
    }
}