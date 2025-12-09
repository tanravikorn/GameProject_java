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

        // สร้าง Item 1 (Ice หรือ ColorBomb)
        String type1 = (mode == GameMode.HARD) ? "ICE" : "COLOR_BOMB";
        String name1 = (mode == GameMode.HARD) ? "Melt Ice" : "Color";
        int count1 = controller.getIceItemAmount();

        item1 = new ItemPane(type1, count1, name1, "#29b6f6", controller, canClick, onActionSuccess);

        // สร้าง Item 2 (Bomb)
        item2 = new ItemPane("BOMB", controller.getBombItemAmount(), "Bomb", "#ef5350", controller, canClick, onActionSuccess);

        // สร้าง Item 3 (Striped)
        item3 = new ItemPane("STRIPED", controller.getStripedItemAmount(), "Striped", "#66bb6a", controller, canClick, onActionSuccess);

        this.getChildren().addAll(item1, item2, item3);
    }

    // ฟังก์ชันสำหรับรีเซ็ตค่าเมื่อกด Restart Game
    public void resetCounts() {
        item1.setCount(controller.getIceItemAmount());
        item2.setCount(controller.getBombItemAmount());
        item3.setCount(controller.getStripedItemAmount());
    }
}