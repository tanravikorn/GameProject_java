package gui.views;

import gui.base.View;
import gui.components.BoardPane;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import logic.board.Board;
import logic.candy.Candy;
import logic.candy.CandyColor;
import logic.candy.CandyType;
import logic.controller.GameController;
import logic.controller.GameMode;
import logic.utils.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class GameView implements View {

    private static final int ROWS = 9;
    private static final int COLS = 9;

    private final Scene scene;
    private GameController controller;
    private BoardPane boardPane;

    // UI Components
    private Label scoreLabel;
    private Label moveLabel;

    // ปุ่มไอเทม
    private Button item1Btn;
    private Button item2Btn;
    private Button item3Btn;

    // ตัวแปรนับจำนวนไอเทม (Local Trackers)
    private int localItem1Count;
    private int localBombCount;
    private int localStripedCount;

    private boolean isAnimating = false;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private final Random random = new Random();

    public GameView(GameMode mode) {
        controller = new GameController(ROWS, COLS, mode);

        // ดึงค่าเริ่มต้นจาก Controller มาเก็บไว้เอง
        this.localBombCount = controller.getBombItemAmount();
        this.localStripedCount = controller.getStripedItemAmount();
        this.localItem1Count = controller.getIceItemAmount();

        BorderPane root = new BorderPane();
        // กำหนดสีพื้นหลังหลัก (ธีมมืด)
        root.setStyle("-fx-background-color: #2c3e50;");

        // --- Center: Board ---
        boardPane = new BoardPane();
        root.setCenter(boardPane);
        BorderPane.setAlignment(boardPane, Pos.CENTER);

        // --- Top: HUD (Score & Moves) ---
        BorderPane topPanel = new BorderPane();
        topPanel.setPadding(new Insets(15, 25, 15, 25)); // เว้นขอบซ้ายขวาให้สวยงาม
        topPanel.setStyle("-fx-background-color: rgba(0,0,0,0.3);"); // พื้นหลังจางๆ

        // 1. Score -> มุมซ้ายบน
        scoreLabel = new Label();
        styleLabel(scoreLabel);
        topPanel.setLeft(scoreLabel);

        // 2. Moves -> มุมขวาบน
        moveLabel = new Label();
        styleLabel(moveLabel);
        topPanel.setRight(moveLabel);

        root.setTop(topPanel);

        // --- Bottom: Items ---
        HBox bottomPanel = new HBox(15);
        bottomPanel.setAlignment(Pos.CENTER);
        bottomPanel.setPadding(new Insets(15));
        bottomPanel.setStyle("-fx-background-color: rgba(0,0,0,0.3);");

        // Item 1: เปลี่ยนตามโหมด (Ice หรือ Color Bomb)
        item1Btn = new Button();
        if (mode == GameMode.HARD) {
            item1Btn.setText("Melt Ice (" + localItem1Count + ")");
            item1Btn.setOnAction(e -> handleUseItem("ICE"));
        } else {
            item1Btn.setText("Color Bomb (" + localItem1Count + ")");
            item1Btn.setOnAction(e -> handleUseItem("COLOR_BOMB"));
        }
        styleItemButton(item1Btn, "#29b6f6");

        // Item 2: Bomb
        item2Btn = new Button("Bomb (" + localBombCount + ")");
        item2Btn.setOnAction(e -> handleUseItem("BOMB"));
        styleItemButton(item2Btn, "#ef5350");

        // Item 3: Striped
        item3Btn = new Button("Striped (" + localStripedCount + ")");
        item3Btn.setOnAction(e -> handleUseItem("STRIPED"));
        styleItemButton(item3Btn, "#66bb6a");

        bottomPanel.getChildren().addAll(item1Btn, item2Btn, item3Btn);
        root.setBottom(bottomPanel);

        // อัปเดตสถานะปุ่มครั้งแรก
        updateItemButtons();

        updateView(null);
        scene = new Scene(root, 600, 750);
    }

    // --- Styling Helpers ---
    private void styleLabel(Label lbl) {
        lbl.setTextFill(Color.WHITE);
        lbl.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
    }

    private void styleItemButton(Button btn, String color) {
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10;");
        btn.setPrefWidth(140);
        btn.setPrefHeight(40);
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    // --- Update View ---
    private void updateView(Set<Point> animateCandidates) {
        boardPane.update(
                controller.getBoard(),
                animateCandidates,
                selectedRow,
                selectedCol,
                this::handleTileClick
        );

        scoreLabel.setText("Score: " + controller.getScore());
        moveLabel.setText("Moves: " + controller.getMoveLeft());

        // อัปเดตสถานะปุ่ม (Enable/Disable/Text)
        updateItemButtons();
    }

    private void updateItemButtons() {
        String name1 = (controller.getGameMode() == GameMode.HARD) ? "Melt Ice" : "Color Bomb";
        updateSingleButton(item1Btn, name1, localItem1Count);
        updateSingleButton(item2Btn, "Bomb", localBombCount);
        updateSingleButton(item3Btn, "Striped", localStripedCount);
    }

    private void updateSingleButton(Button btn, String name, int count) {
        btn.setText(name + " (" + count + ")");
        if (count <= 0) {
            btn.setDisable(true);
            btn.setOpacity(0.5);
        } else {
            btn.setDisable(false);
            btn.setOpacity(1.0);
        }
    }

    // --- Actions ---
    private void handleTileClick(int r, int c) {
        if (isAnimating || controller.getMoveLeft() <= 0) return;

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

    // --- Item Logic (เขียนเองใน View เพราะห้ามแก้ Controller) ---
    private void handleUseItem(String type) {
        // 1. ตรวจสอบเงื่อนไขพื้นฐาน
        if (isAnimating || controller.getMoveLeft() <= 0) return;

        List<Point> targetPoints = new ArrayList<>();
        Board board = controller.getBoard();
        boolean used = false;

        // --- Logic: BOMB ---
        if (type.equals("BOMB") && localBombCount > 0) {
            int r = random.nextInt(ROWS);
            int c = random.nextInt(COLS);
            Candy candy = board.getCandy(r, c);
            if (candy != null) {
                candy.setType(CandyType.BOMB);
                targetPoints.add(new Point(r, c));
                localBombCount--;
                used = true;
            }
        }
        // --- Logic: STRIPED ---
        else if (type.equals("STRIPED") && localStripedCount > 0) {
            int r = random.nextInt(ROWS);
            int c = random.nextInt(COLS);
            Candy candy = board.getCandy(r, c);
            if (candy != null) {
                if (random.nextBoolean()) candy.setType(CandyType.STRIPED_HOR);
                else candy.setType(CandyType.STRIPED_VER);
                targetPoints.add(new Point(r, c));
                localStripedCount--;
                used = true;
            }
        }
        // --- Logic: ICE (Hard Mode) ---
        else if (type.equals("ICE") && localItem1Count > 0) {
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                    Candy candy = board.getCandy(r, c);
                    if (candy != null && candy.isFrozen()) {
                        targetPoints.add(new Point(r, c));
                    }
                }
            }
            if(!targetPoints.isEmpty()) {
                localItem1Count--;
                used = true;
            }
        }
        // --- Logic: COLOR BOMB (Normal Mode) ---
        else if (type.equals("COLOR_BOMB") && localItem1Count > 0) {
            List<CandyColor> availableColors = new ArrayList<>();
            for(int r=0; r<ROWS; r++) {
                for(int c=0; c<COLS; c++) {
                    Candy candy = board.getCandy(r, c);
                    if(candy != null && candy.getColor() != CandyColor.NONE && !availableColors.contains(candy.getColor())) {
                        availableColors.add(candy.getColor());
                    }
                }
            }

            if(!availableColors.isEmpty()) {
                CandyColor targetColor = availableColors.get(random.nextInt(availableColors.size()));
                for(int r=0; r<ROWS; r++) {
                    for(int c=0; c<COLS; c++) {
                        Candy candy = board.getCandy(r, c);
                        if(candy != null && candy.getColor() == targetColor) {
                            targetPoints.add(new Point(r, c));
                        }
                    }
                }
                localItem1Count--;
                used = true;
            }
        }

        // สั่งให้ Controller ทำลายลูกกวาด
        if (used && !targetPoints.isEmpty()) {
            Set<Point> allRemoves = controller.activateItems(targetPoints);
            runGameLoop(allRemoves);
        }
    }

    private void runGameLoop(Set<Point> initialRemoves) {
        isAnimating = true;
        updateView(initialRemoves);

        PauseTransition waitExplosion = new PauseTransition(Duration.seconds(0.3));
        waitExplosion.setOnFinished(e -> {
            try {
                controller.applyPhysics(initialRemoves);
                updateView(null);

                PauseTransition waitGravity = new PauseTransition(Duration.seconds(0.4));
                waitGravity.setOnFinished(e2 -> {
                    try {
                        Set<Point> chainRemoves = controller.checkChainReaction();
                        if (!chainRemoves.isEmpty()) {
                            runGameLoop(chainRemoves);
                        } else {
                            isAnimating = false;
                        }
                    } catch (Exception ex) { ex.printStackTrace(); isAnimating = false; }
                });
                waitGravity.play();
            } catch (Exception ex) { ex.printStackTrace(); isAnimating = false; }
        });
        waitExplosion.play();
    }
}