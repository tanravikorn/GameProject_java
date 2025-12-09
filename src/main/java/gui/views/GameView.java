package gui.views;

import gui.base.View;
import gui.base.ViewManager;
import gui.components.BoardPane;
import gui.components.ControlPane; // Import Class ใหม่
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import logic.controller.GameController;
import logic.controller.GameMode;
import logic.utils.Point;

import java.util.Set;

public class GameView implements View {

    private static final int ROWS = 9;
    private static final int COLS = 9;

    private final Scene scene;
    private GameController controller;
    private BoardPane boardPane;
    private ControlPane controlPane; // ใช้แทนปุ่ม Item แยกๆ

    private Label scoreLabel;
    private Label moveLabel;

    private boolean isAnimating = false;
    private int selectedRow = -1;
    private int selectedCol = -1;

    public GameView(GameMode mode) {
        controller = new GameController(ROWS, COLS, mode);

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #2c3e50;");

        // --- Center: Board ---
        boardPane = new BoardPane();
        root.setCenter(boardPane);
        BorderPane.setAlignment(boardPane, Pos.CENTER);

        // --- Top: HUD ---
        BorderPane topPanel = new BorderPane();
        topPanel.setPadding(new Insets(15, 20, 15, 20));
        topPanel.setStyle("-fx-background-color: rgba(0,0,0,0.3);");

        scoreLabel = new Label();
        styleLabel(scoreLabel);
        topPanel.setLeft(scoreLabel);

        moveLabel = new Label();
        styleLabel(moveLabel);
        topPanel.setRight(moveLabel);

        root.setTop(topPanel);

        // --- Bottom: ControlPane (Clean!) ---
        // เราส่ง:
        // 1. controller
        // 2. เงื่อนไขการคลิก (() -> !isAnimating)
        // 3. สิ่งที่ต้องทำเมื่อใช้ไอเทมสำเร็จ (this::runGameLoop)
        controlPane = new ControlPane(
                controller,
                () -> !isAnimating,  // canClick?
                this::runGameLoop    // onSuccess
        );
        root.setBottom(controlPane);

        updateView(null);
        scene = new Scene(root, 600, 700);
    }

    private void styleLabel(Label lbl) {
        lbl.setTextFill(Color.WHITE);
        lbl.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
    }

    @Override
    public Scene getScene() { return scene; }

    private void updateView(Set<Point> animateCandidates) {
        boardPane.update(controller.getBoard(), animateCandidates, selectedRow, selectedCol, this::handleTileClick);
        scoreLabel.setText("Score: " + controller.getScore());
        moveLabel.setText("Moves: " + controller.getMoveLeft());
        // ไม่ต้อง updateItemButtons() ที่นี่แล้ว เพราะ ItemPane จัดการตัวเอง
    }

    private void handleTileClick(int r, int c) {
        if (isAnimating || controller.getMoveLeft() <= 0) return;
        if (selectedRow == -1) {
            selectedRow = r; selectedCol = c; updateView(null);
        } else {
            if (Math.abs(selectedRow - r) + Math.abs(selectedCol - c) == 1) {
                Set<Point> matchResults = controller.handleSwap(selectedRow, selectedCol, r, c);
                selectedRow = -1; selectedCol = -1;
                if (!matchResults.isEmpty()) runGameLoop(matchResults);
                else updateView(null);
            } else {
                selectedRow = r; selectedCol = c; updateView(null);
            }
        }
    }

    // *** ไม่ต้องมี handleUseItem แล้ว เพราะย้ายไป ItemPane ***

    private void runGameLoop(Set<Point> initialRemoves) {
        isAnimating = true;
        updateView(initialRemoves);
        PauseTransition waitExplosion = new PauseTransition(Duration.seconds(0.3));
        waitExplosion.setOnFinished(e -> {
            try {
                controller.boardUpdate(initialRemoves);
                updateView(null);
                PauseTransition waitGravity = new PauseTransition(Duration.seconds(0.4));
                waitGravity.setOnFinished(e2 -> {
                    try {
                        Set<Point> chainRemoves = controller.checkChainReaction();
                        if (!chainRemoves.isEmpty()) runGameLoop(chainRemoves);
                        else isAnimating = false;
                    } catch (Exception ex) { ex.printStackTrace(); isAnimating = false; }
                });
                waitGravity.play();
            } catch (Exception ex) { ex.printStackTrace(); isAnimating = false; }
        });
        waitExplosion.play();
    }
}