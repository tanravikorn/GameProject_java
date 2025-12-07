package gui.views;

import gui.base.View;
import gui.base.ViewManager;
import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

// Import Logic จาก Backend
import logic.board.Board;
import logic.candy.Candy;
import logic.candy.CandyColor;
import logic.candy.CandyType;
import logic.controller.GameController;
import logic.controller.GameMode;
import logic.utils.Point;

import java.util.Set;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GameView implements View {

    // --- ค่าคงที่จาก Main เดิม ---
    private static final int ROWS = 9;
    private static final int COLS = 9;
    private static final int TILE_SIZE = 60;

    // --- ตัวแปรระบบ ---
    private final Scene scene;
    private GameController controller;
    private GridPane gridPane;
    private Label scoreLabel;

    // --- ตัวแปรสถานะเกม ---
    private boolean isAnimating = false;
    private int selectedRow = -1;
    private int selectedCol = -1;

    public GameView() {
        // 1. สร้าง Controller (เริ่มเกมใหม่ทันทีที่เข้าหน้านี้)
        controller = new GameController(ROWS, COLS, GameMode.NORMAL);

        // 2. สร้าง UI Layout
        BorderPane root = new BorderPane();

        // ---------------------------------------------------------
        // [แก้ไขส่วนที่ 1] : เพิ่มพื้นหลังรูปภาพ
        // ---------------------------------------------------------

        // โหลดรูปภาพ (ตรวจสอบให้แน่ใจว่าไฟล์ bg.png อยู่หน้า folder project)
        Image bgImage = new Image("file:bg.png");
        ImageView bgView = new ImageView(bgImage);

        // ปรับขนาดรูปให้พอดีกับหน้าจอ
        bgView.setFitWidth(800);
        bgView.setFitHeight(700);
        bgView.setPreserveRatio(false); // ยืดให้เต็ม
        bgView.setOpacity(0.6); // ปรับความจางของรูป (0.0 - 1.0)

        // สร้าง StackPane เพื่อซ้อนเลเยอร์ (รูปอยู่ล่าง ตารางอยู่บน)
        StackPane gameArea = new StackPane();

        // ส่วนแสดงผลกระดาน
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);

        // เอารูปกับตารางมารวมร่างกัน
        gameArea.getChildren().addAll(bgView, gridPane);
        // ---------------------------------------------------------

        // ส่วนหัว (Header) : ปุ่มกลับเมนู + คะแนน
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER);
        header.setStyle("-fx-padding: 15; -fx-background-color: #2c3e50;");

        Button btnBack = new Button("Main Menu");
        styleButton(btnBack);
        // สั่งให้ ViewManager กลับไปหน้า Start
        btnBack.setOnAction(e -> ViewManager.getInstance().showStartScreen());

        scoreLabel = new Label("Score: 0");
        scoreLabel.setTextFill(Color.WHITE);
        scoreLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

        header.getChildren().addAll(btnBack, scoreLabel);

        root.setTop(header);

        // [แก้ไข] เปลี่ยนจาก gridPane เป็น gameArea (ที่มีรูปซ้อนอยู่)
        root.setCenter(gameArea);

        // 3. วาดกระดานครั้งแรก
        updateView(null);

        // 4. สร้าง Scene ส่งกลับไปให้ ViewManager
        scene = new Scene(root, 800, 700);
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    private void styleButton(Button btn) {
        btn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
    }

    // ==========================================
    // ด้านล่างนี้คือ Logic เดิมจาก Main.java
    // (นำมาปรับปรุงเล็กน้อยให้เข้ากับ Class ใหม่)
    // ==========================================

    private void updateView(Set<Point> animateCandidates) {
        gridPane.getChildren().clear();
        Board board = controller.getBoard();

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                StackPane tile = new StackPane();
                tile.setPrefSize(TILE_SIZE, TILE_SIZE);

                // พื้นหลังช่อง
                Rectangle bg = new Rectangle(TILE_SIZE, TILE_SIZE);

                // ---------------------------------------------------------
                // [แก้ไขส่วนที่ 2] : ปรับสีช่องให้โปร่งใส (Transparency)
                // ---------------------------------------------------------
                if ((r + c) % 2 == 0) {
                    // สีขาว จางๆ (Opacity 0.3)
                    bg.setFill(Color.rgb(255, 255, 255, 0.3));
                } else {
                    // สีดำ จางๆ (Opacity 0.1)
                    bg.setFill(Color.rgb(0, 0, 0, 0.1));
                }

                // Highlight ช่องที่เลือก (ยังคงสีชัดเจนไว้)
                if (r == selectedRow && c == selectedCol) {
                    bg.setFill(Color.web("#f1c40f", 0.7)); // สีเหลืองโปร่งแสงนิดหน่อย
                    bg.setStroke(Color.WHITE);
                    bg.setStrokeWidth(3);
                }

                tile.getChildren().add(bg);

                // วาดลูกกวาด
                Candy candy = board.getCandy(r, c);
                // ถ้าลูกกวาดกำลังจะระเบิด ให้ซ่อนไว้ (Animation)
                if (animateCandidates != null && animateCandidates.contains(new Point(r, c))) {
                    // ว่างเปล่า
                } else if (candy != null) {
                    Circle circle = new Circle(TILE_SIZE / 2.5);
                    circle.setFill(getColor(candy.getColor()));

                    // Effect พิเศษ
                    if (candy.getType() == CandyType.COLOR_BOMB) {
                        circle.setFill(Color.BLACK);
                        circle.setStroke(Color.WHITE);
                        circle.setStrokeWidth(2);
                    }
                    if(candy.isFrozen()){
                        circle.setStroke(Color.CYAN);
                        circle.setStrokeWidth(3);
                    }

                    tile.getChildren().add(circle);

                    // ตัวอักษร
                    String typeText = getTypeCode(candy.getType());
                    if (!typeText.isEmpty()) {
                        Text txt = new Text(typeText);
                        txt.setFont(Font.font("Arial", FontWeight.BOLD, 20));
                        txt.setFill(Color.WHITE);
                        txt.setStroke(Color.BLACK);
                        tile.getChildren().add(txt);
                    }
                }

                // Event คลิก
                final int finalR = r;
                final int finalC = c;
                tile.setOnMouseClicked(e -> handleTileClick(finalR, finalC));

                gridPane.add(tile, c, r);
            }
        }
        scoreLabel.setText("Score: " + controller.getScore());
    }

    private void handleTileClick(int r, int c) {
        if (isAnimating) return;

        if (selectedRow == -1) {
            selectedRow = r;
            selectedCol = c;
            updateView(null);
        } else {
            if (Math.abs(selectedRow - r) + Math.abs(selectedCol - c) == 1) {
                Set<Point> matchResults = controller.handleSwap(selectedRow, selectedCol, r, c);
                selectedRow = -1;
                selectedCol = -1;

                if (!matchResults.isEmpty()) {
                    runGameLoop(matchResults);
                } else {
                    updateView(null);
                }
            } else {
                selectedRow = r;
                selectedCol = c;
                updateView(null);
            }
        }
    }

    private void runGameLoop(Set<Point> initialRemoves) {
        isAnimating = true;
        updateView(initialRemoves); // Phase 1: ระเบิด

        PauseTransition waitExplosion = new PauseTransition(Duration.seconds(0.3));
        waitExplosion.setOnFinished(e -> {
            try {
                controller.applyPhysics(initialRemoves);
                updateView(null); // Phase 2: ร่วงลงมา

                PauseTransition waitGravity = new PauseTransition(Duration.seconds(0.4));
                waitGravity.setOnFinished(e2 -> {
                    try {
                        Set<Point> chainRemoves = controller.checkChainReaction();
                        if (!chainRemoves.isEmpty()) {
                            runGameLoop(chainRemoves); // Loop
                        } else {
                            isAnimating = false;
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
    }

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
        if (t == null) return "";
        switch (t) {
            case STRIPED_HOR: return "H";
            case STRIPED_VER: return "V";
            case BOMB: return "B";
            case COLOR_BOMB: return "C";
            default: return "";
        }
    }
}