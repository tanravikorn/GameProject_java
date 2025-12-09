package gui.components;

import javafx.animation.PauseTransition;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import logic.Item.*;
import logic.controller.GameController;
import logic.controller.GameState;
import logic.utils.Point;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ItemPane extends StackPane {

    private final String itemType;
    private int count;
    private final GameController controller;
    private final Button button;

    private final Consumer<Set<Point>> onActionSuccess;


    public ItemPane(String itemType, int initialCount, String displayName, String colorHex,
                    GameController controller, Consumer<Set<Point>> onActionSuccess) {
        this.itemType = itemType;
        this.count = initialCount;
        this.controller = controller;
        this.onActionSuccess = onActionSuccess;

        this.button = new Button();
        styleButton(colorHex);
        updateButtonText(displayName);

        this.button.setOnAction(e -> handleItemClick());

        this.getChildren().add(button);
        updateState();
    }

    private void handleItemClick() {
        if (controller.getGameState() != GameState.PLAY) return;

        Item itemStrategy = createItemStrategy();
        if (itemStrategy == null) return;

        List<Point> targetPoints = controller.applyItemTransform(itemStrategy);

        if (targetPoints != null && (!targetPoints.isEmpty() || isInstantItem())) {

            refreshCount();

            if (itemType.equals("BOMB") || itemType.equals("STRIPED")) {

                if (onActionSuccess != null) onActionSuccess.accept(new HashSet<>());

                PauseTransition wait = new PauseTransition(Duration.seconds(0.5));
                wait.setOnFinished(e -> {
                    Set<Point> finalRemoves = controller.activateItems(targetPoints);
                    if (onActionSuccess != null) onActionSuccess.accept(finalRemoves);
                });
                wait.play();
            }
            else {
                if (onActionSuccess != null) {
                    onActionSuccess.accept(new HashSet<>());
                }
            }
        }
    }
    private boolean isInstantItem() {
        return itemType.equals("ICE") || itemType.equals("COLOR_BOMB");
    }
    private Item createItemStrategy() {
        switch (itemType) {
            case "BOMB": return new BombItem();
            case "STRIPED": return new StripedItem();
            case "ICE":
            case "COLOR_BOMB": return new IceBreakItem();
            default: return null;
        }
    }
    public void refreshCount() {
        switch (itemType) {
            case "BOMB": this.count = controller.getBombItemAmount(); break;
            case "STRIPED": this.count = controller.getStripedItemAmount(); break;
            case "ICE":
            case "COLOR_BOMB": this.count = controller.getIceItemAmount(); break;
        }
        updateState();
    }

    public void updateState() {
        if (count <= 0) {
            button.setDisable(true);
            button.setOpacity(0.5);
        } else {
            button.setDisable(false);
            button.setOpacity(1.0);
        }
        String currentName = button.getText().split(" \\(")[0];
        updateButtonText(currentName);
    }

    public void updateButtonText(String name) {
        button.setText(name + " (" + count + ")");
    }

    private void styleButton(String colorHex) {
        button.setStyle(
                "-fx-background-color: " + colorHex + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 15;" +
                        "-fx-min-width: 100px;" +
                        "-fx-padding: 8;"
        );
        button.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    }

}