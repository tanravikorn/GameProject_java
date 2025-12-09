package gui.components;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import logic.board.Board;
import logic.candy.Candy;
import logic.candy.CandyColor;
import logic.candy.CandyType;
import logic.controller.GameController;
import logic.utils.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ItemPane extends StackPane {

    private final String itemType; // "BOMB", "STRIPED", "ICE", "COLOR_BOMB"
    private int count;
    private final GameController controller;
    private final Button button;
    private final Random random = new Random();

    // Callback เพื่อแจ้งกลับไปที่ GameView เมื่อใช้ไอเทมสำเร็จ
    private final Consumer<Set<Point>> onActionSuccess;
    // Callback เพื่อเช็คว่าตอนนี้กดได้ไหม (เช่น Animation เล่นอยู่หรือเปล่า)
    private final Supplier<Boolean> canClick;

    public ItemPane(String itemType, int initialCount, String displayName, String colorHex,
                    GameController controller, Supplier<Boolean> canClick, Consumer<Set<Point>> onActionSuccess) {

        this.itemType = itemType;
        this.count = initialCount;
        this.controller = controller;
        this.canClick = canClick;
        this.onActionSuccess = onActionSuccess;

        this.button = new Button();
        styleButton(colorHex);
        updateButtonText(displayName);

        // --- ใส่ Action ไว้ใน Class นี้เลย ---
        this.button.setOnAction(e -> handleUseItem());

        this.getChildren().add(button);
        updateState(); // เช็ค enable/disable ครั้งแรก
    }

    private void handleUseItem() {
        // 1. เช็คเงื่อนไขจาก GameView (เช่น Animation เล่นอยู่ไหม)
        if (!canClick.get() || controller.getMoveLeft() <= 0 || count <= 0) return;

        List<Point> targetPoints = new ArrayList<>();
        Board board = controller.getBoard();
        boolean used = false;
        int rows = board.getRows();
        int cols = board.getCols();

        // --- Logic เดิมที่ย้ายมาจาก GameView ---
        if (itemType.equals("BOMB")) {
            int r = random.nextInt(rows);
            int c = random.nextInt(cols);
            Candy candy = board.getCandy(r, c);
            if (candy != null) {
                candy.setType(CandyType.BOMB);
                targetPoints.add(new Point(r, c));
                used = true;
            }
        }
        else if (itemType.equals("STRIPED")) {
            int r = random.nextInt(rows);
            int c = random.nextInt(cols);
            Candy candy = board.getCandy(r, c);
            if (candy != null) {
                if (random.nextBoolean()) candy.setType(CandyType.STRIPED_HOR);
                else candy.setType(CandyType.STRIPED_VER);
                targetPoints.add(new Point(r, c));
                used = true;
            }
        }
        else if (itemType.equals("ICE")) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    Candy candy = board.getCandy(r, c);
                    if (candy != null && candy.isFrozen()) {
                        targetPoints.add(new Point(r, c));
                    }
                }
            }
            if (!targetPoints.isEmpty()) used = true;
        }
        else if (itemType.equals("COLOR_BOMB")) {
            List<CandyColor> availableColors = new ArrayList<>();
            for(int r=0; r<rows; r++) {
                for(int c=0; c<cols; c++) {
                    Candy candy = board.getCandy(r, c);
                    if(candy != null && candy.getColor() != CandyColor.NONE && !availableColors.contains(candy.getColor())) {
                        availableColors.add(candy.getColor());
                    }
                }
            }
            if(!availableColors.isEmpty()) {
                CandyColor targetColor = availableColors.get(random.nextInt(availableColors.size()));
                for(int r=0; r<rows; r++) {
                    for(int c=0; c<cols; c++) {
                        Candy candy = board.getCandy(r, c);
                        if(candy != null && candy.getColor() == targetColor) {
                            targetPoints.add(new Point(r, c));
                        }
                    }
                }
                used = true;
            }
        }

        // --- ถ้าใช้สำเร็จ ---
        if (used && !targetPoints.isEmpty()) {
            // ลดจำนวนของตัวเอง
            this.count--;
            updateButtonText(button.getText().split(" \\(")[0]); // รีเฟรช text
            updateState(); // เช็คว่าต้อง disable มั้ย

            // สั่ง Controller ระเบิด
            Set<Point> allRemoves = controller.activateItems(targetPoints);

            // แจ้ง GameView ให้เริ่ม Animation
            onActionSuccess.accept(allRemoves);
        }
    }

    public void updateState() {
        if (count <= 0) {
            button.setDisable(true);
            button.setOpacity(0.5);
        } else {
            button.setDisable(false);
            button.setOpacity(1.0);
        }
        // อัปเดต text ใหม่ทุกครั้งที่มีการเรียก update
        String currentName = button.getText().split(" \\(")[0];
        updateButtonText(currentName);
    }

    // เผื่อต้องการรีเซ็ตเกมแล้วคืนค่าจำนวนไอเทม
    public void setCount(int count) {
        this.count = count;
        updateState();
    }

    private void updateButtonText(String name) {
        button.setText(name + " (" + count + ")");
    }

    private void styleButton(String color) {
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10; -fx-font-size: 12px;");
        button.setPrefWidth(140);
        button.setPrefHeight(40);
    }
}