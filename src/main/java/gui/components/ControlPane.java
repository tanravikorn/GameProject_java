package gui.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import logic.controller.GameController;
import logic.controller.GameMode;
import logic.utils.Point;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ControlPane extends HBox {

    private final ItemPane item1;
    private final ItemPane item2;
    private final ItemPane item3;
    private final GameController controller;

    public ControlPane(GameController controller, Supplier<Boolean> canClick, Consumer<Set<Point>> onActionSuccess) {
        this.controller = controller;

        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(10));
        this.setSpacing(15);
        this.setStyle("-fx-background-color: rgba(0,0,0,0.3);");

        GameMode mode = controller.getGameMode();


        String type1 = (mode == GameMode.HARD) ? "ICE" : "COLOR_BOMB";
        String name1 = (mode == GameMode.HARD) ? "Melt Ice" : "Color";
        int count1 = controller.getIceItemAmount();

        item1 = new ItemPane(type1, count1, name1, "#29b6f6", controller, canClick, onActionSuccess);

        item2 = new ItemPane("BOMB", controller.getBombItemAmount(), "Bomb", "#ef5350", controller, canClick, onActionSuccess);

        item3 = new ItemPane("STRIPED", controller.getStripedItemAmount(), "Striped", "#66bb6a", controller, canClick, onActionSuccess);

        this.getChildren().addAll(item1, item2, item3);
    }

    public void resetCounts() {
        item1.refreshCount();
        item2.refreshCount();
        item3.refreshCount();
    }
}