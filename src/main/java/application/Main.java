package application;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color; // Import นี้สำคัญ
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import logic.board.Board;
import logic.candy.Candy;
import logic.candy.CandyColor;
import logic.candy.CandyType; // Import นี้สำคัญ
import logic.controller.GameController;
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

    // --- View Rendering ---

    private void updateView(Set<Point> hiddenCandies) {
        gridPane.getChildren().clear();
        Board board = controller.getBoard();

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                StackPane tile = new StackPane();
                tile.setPrefSize(TILE_SIZE, TILE_SIZE);

                // Background
                Rectangle bg = new Rectangle(TILE_SIZE, TILE_SIZE);
                bg.setFill((r + c) % 2 == 0 ? Color.LIGHTGRAY : Color.DARKGRAY);
                bg.setStroke(Color.BLACK);
                tile.getChildren().add(bg);

                // Check Visual Holes
                boolean shouldHide = (hiddenCandies != null && hiddenCandies.contains(new Point(r, c)));
                Candy candy = board.getCandy(r, c);

                if (candy != null && !shouldHide) {
                    Circle circle = new Circle(TILE_SIZE / 2 - 8);

                    // 🔥 LOGIC สีดำสำหรับ COLOR_BOMB (แก้ตรงนี้) 🔥
                    if (candy.getType() == CandyType.COLOR_BOMB) {
                        circle.setFill(Color.BLACK); // สีดำ
                        circle.setStroke(Color.WHITE); // ขอบขาวให้เด่น
                        circle.setStrokeWidth(3);
                    } else {
                        circle.setFill(getColor(candy.getColor())); // สีปกติ
                        circle.setStroke(Color.BLACK);
                        circle.setStrokeWidth(2);
                    }

                    // Text Type
                    String typeText = getTypeCode(candy.getType());
                    Text text = new Text(typeText);
                    text.setFill(Color.WHITE);
                    text.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-effect: dropshadow(three-pass-box, black, 2, 0, 0, 0);");

                    tile.getChildren().addAll(circle, text);
                }

                // Highlight Selection
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

    // --- Interaction & Game Loop ---

    private void handleTileClick(int r, int c) {
        if (isAnimating) return;

        if (selectedRow == -1) {
            selectedRow = r;
            selectedCol = c;
            updateView(null);
        } else {
            if (Math.abs(selectedRow - r) + Math.abs(selectedCol - c) == 1) {
                // เรียก Backend
                Set<Point> removes = controller.handleSwap(selectedRow, selectedCol, r, c);

                if (!removes.isEmpty()) {
                    isAnimating = true;

                    // PHASE 0: โชว์การสลับก่อน (0.3 วินาที)
                    updateView(null);

                    PauseTransition waitSwap = new PauseTransition(Duration.seconds(0.3));
                    waitSwap.setOnFinished(e -> runGameLoop(removes));
                    waitSwap.play();

                } else {
                    System.out.println("Invalid Move");
                }

                selectedRow = -1;
                selectedCol = -1;
                if(removes.isEmpty()) updateView(null);
            } else {
                selectedRow = r;
                selectedCol = c;
                updateView(null);
            }
        }
    }

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
                                runGameLoop(newRemoves); // Recursion
                            } else {
                                isAnimating = false;
                                System.out.println("--- Board Settled ---");
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            isAnimating = false;
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

    // Helpers
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