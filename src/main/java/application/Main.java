package application;

import javafx.animation.PauseTransition;
import logic.controller.GameController;
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
import logic.board.Board;
import logic.candy.Candy;
import logic.candy.CandyType;
import logic.utils.Point;
import javafx.util.Duration;

import java.util.Set;

public class Main extends Application {

    private static final int ROWS = 9;
    private static final int COLS = 9;
    private static final int TILE_SIZE = 60;
    private boolean isAnimating = false;
    private GameController controller;
    private GridPane gridPane;
    private Label scoreLabel;

    // เก็บสถานะการคลิก (Click แรกเลือกตัว, Click สองสลับ)
    private int selectedRow = -1;
    private int selectedCol = -1;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // 1. Initialize Controller
        controller = new GameController(ROWS, COLS);

        // 2. Setup GUI Layout
        BorderPane root = new BorderPane();
        gridPane = new GridPane();
        scoreLabel = new Label("Score: 0");
        scoreLabel.setStyle("-fx-font-size: 20px; -fx-padding: 10;");

        root.setCenter(gridPane);
        root.setTop(scoreLabel);

        // 3. วาดกระดานครั้งแรก
        updateView();

        Scene scene = new Scene(root, COLS * TILE_SIZE, ROWS * TILE_SIZE + 50);
        primaryStage.setTitle("Candy Crush Logic Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // เมธอดวาดกระดานใหม่ตามข้อมูลใน Board
    private void updateView() {
        gridPane.getChildren().clear();
        Board board = controller.getBoard();

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                StackPane tile = new StackPane();
                tile.setPrefSize(TILE_SIZE, TILE_SIZE);

                // พื้นหลังตาราง (ลายตารางหมากรุก)
                Rectangle bg = new Rectangle(TILE_SIZE, TILE_SIZE);
                bg.setFill((r + c) % 2 == 0 ? Color.LIGHTGRAY : Color.GRAY);
                bg.setStroke(Color.BLACK);

                tile.getChildren().add(bg);

                Candy candy = board.getCandy(r, c);
                if (candy != null) {
                    // วาดลูกอม (วงกลม)
                    Circle circle = new Circle(TILE_SIZE / 2 - 5);
                    circle.setFill(getColor(candy.getColor()));

                    // ใส่ Text บอกประเภท (N=Normal, H=Hor, V=Ver, B=Bomb, C=Color)
                    String typeText = getTypeCode(candy.getType());
                    Text text = new Text(typeText);
                    text.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

                    tile.getChildren().addAll(circle, text);
                }

                // Highlight ตัวที่เลือก
                if (r == selectedRow && c == selectedCol) {
                    bg.setStroke(Color.YELLOW);
                    bg.setStrokeWidth(3);
                }

                // Event: เมื่อคลิกที่ช่อง
                int finalR = r;
                int finalC = c;
                tile.setOnMouseClicked(e -> handleTileClick(finalR, finalC));

                gridPane.add(tile, c, r);
            }
        }
        scoreLabel.setText("Score: " + controller.getScore());
    }

    private void handleTileClick(int r, int c) {
        if (isAnimating) return; // ถ้ากำลังเล่น Animation ห้ามกด!

        if (selectedRow == -1) {
            selectedRow = r;
            selectedCol = c;
            updateView(); // เพื่อโชว์ highlight
        } else {
            if (Math.abs(selectedRow - r) + Math.abs(selectedCol - c) == 1) {
                // 1. สั่ง Controller ให้สลับ
                Set<Point> removes = controller.handleSwap(selectedRow, selectedCol, r, c);

                if (!removes.isEmpty()) {
                    // ถ้ามีการระเบิด -> เริ่มเล่นหนัง (Animation Loop)
                    isAnimating = true;
                    runGameLoop(removes);
                } else {
                    System.out.println("Invalid Move");
                }
                selectedRow = -1;
                selectedCol = -1;
                updateView();
            } else {
                selectedRow = r;
                selectedCol = c;
                updateView();
            }
        }
    }
    private void runGameLoop(Set<Point> removes) {
        // Step 1: วาดภาพระเบิด (ตอนนี้ของยังอยู่ แต่เตรียมหาย)
        updateView();

        // สร้าง Delay 0.5 วินาที ก่อนจะให้ของตกลงมา
        PauseTransition waitBeforeFall = new PauseTransition(Duration.seconds(0.5));

        waitBeforeFall.setOnFinished(e -> {
            // Step 2: สั่ง Backend ให้คำนวณ Gravity & Refill
            controller.applyPhysics(removes);
            updateView(); // ผู้เล่นจะเห็นของตกลงมาและของใหม่เติมเข้ามาตอนนี้

            // สร้าง Delay อีก 0.5 วินาที ก่อนจะเช็คระเบิดรอบถัดไป
            PauseTransition waitBeforeNextMatch = new PauseTransition(Duration.seconds(0.5));

            waitBeforeNextMatch.setOnFinished(e2 -> {
                // Step 3: เช็ค Chain Reaction
                Set<Point> newRemoves = controller.checkChainReaction();

                if (!newRemoves.isEmpty()) {
                    // ถ้ามีระเบิดต่อ -> วนลูปเรียกตัวเองซ้ำ! (Recursion)
                    runGameLoop(newRemoves);
                } else {
                    // ถ้านิ่งแล้ว -> จบ Animation ปลดล็อคให้ผู้เล่นกดต่อได้
                    isAnimating = false;
                    System.out.println("Board Settled.");
                }
            });

            waitBeforeNextMatch.play();
        });

        waitBeforeFall.play();
    }

    // แปลง CandyColor ของเรา เป็น JavaFX Color
    private Color getColor(logic.candy.CandyColor c) {
        if (c == null) return Color.TRANSPARENT;
        switch (c) {
            case RED: return Color.RED;
            case GREEN: return Color.GREEN;
            case BLUE: return Color.BLUE;
            case YELLOW: return Color.GOLD; // สีเหลืองมองยาก ใช้ Gold แทน
            case PURPLE: return Color.PURPLE;
            // case ORANGE: return Color.ORANGE;
            default: return Color.BLACK;
        }
    }

    // ตัวย่อ Type
    private String getTypeCode(CandyType t) {
        switch (t) {
            case STRIPED_HOR: return "HOR";
            case STRIPED_VER: return "VER";
            case BOMB: return "BOMB";
            case COLOR_BOMB: return "COLOR";
            default: return "";
        }
    }
}