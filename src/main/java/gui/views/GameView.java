package gui.views;

import gui.base.SoundManager;
import gui.base.View;
import gui.base.ViewManager;
import gui.components.BoardPane;
import gui.components.ControlPane;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import logic.controller.GameController;
import logic.controller.GameMode;
import logic.controller.GameState;
import logic.utils.Point;

import java.util.Set;

public class GameView implements View {

    private static final int ROWS = 9;
    private static final int COLS = 9;

    private final Scene scene;
    private GameController controller;
    private BoardPane boardPane;
    private ControlPane controlPane;

    private Label scoreLabel;
    private Label moveLabel;

    private int selectedRow = -1;
    private int selectedCol = -1;

    public GameView(GameMode mode) {
        controller = new GameController(ROWS, COLS, mode);

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #2c3e50;");

        boardPane = new BoardPane();
        root.setCenter(boardPane);
        BorderPane.setAlignment(boardPane, Pos.CENTER);

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

        controlPane = new ControlPane(
                controller,
                () -> controller.getGameState() == GameState.PLAY,
                this::runGameLoop
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
        if(controlPane != null) controlPane.resetCounts();
    }

    private void handleTileClick(int r, int c) {
        if (controller.getGameState() != GameState.PLAY) return;
        if (selectedRow == -1) {
            selectedRow = r; selectedCol = c; updateView(null);
            SoundManager.playSFX("click.mp3");
        } else {
            if (Math.abs(selectedRow - r) + Math.abs(selectedCol - c) == 1) {
                Set<Point> matchResults = controller.handleSwap(selectedRow, selectedCol, r, c);
                selectedRow = -1; selectedCol = -1;
                if (!matchResults.isEmpty()) runGameLoop(matchResults);
                else updateView(null);
            } else {
                selectedRow = r; selectedCol = c; updateView(null);
                SoundManager.playSFX("click.mp3");
            }
        }
    }

    private void runGameLoop(Set<Point> initialRemoves) {
        if (initialRemoves != null && initialRemoves.isEmpty()) {
            updateView(null);
            return;
        }
//        if (initialRemoves != null && !initialRemoves.isEmpty()) {
//            SoundManager.playSFX("pop.mp3");
//        }

        updateView(null);

        new Thread(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            Platform.runLater(() -> {
                if (initialRemoves != null && !initialRemoves.isEmpty()) {
                    SoundManager.playSFX("pop.mp3");
                }
                updateView(initialRemoves);
            });

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            Platform.runLater(() -> {
                controller.boardUpdate(initialRemoves);
                updateView(null);
            });
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                Set<Point> chainRemoves = controller.checkChainReaction();

                if (!chainRemoves.isEmpty()) {
                    Platform.runLater(() -> runGameLoop(chainRemoves));
                } else {
                    Platform.runLater(() -> {
                        controller.endTurn();
                        updateView(null);
                        controller.setReadyToPlay();
                        checkGameOver();
                    });
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    private void checkGameOver() {
        if (controller.getGameState() == GameState.GAME_OVER) {
            new Thread(()->{
                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Platform.runLater(()->{
                    ViewManager.getInstance().showEndScreen(controller.getScore());
                });
            }).start();
        }
    }
}