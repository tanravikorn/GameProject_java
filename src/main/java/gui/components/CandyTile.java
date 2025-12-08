package gui.components;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import logic.candy.Candy;
import logic.candy.CandyColor;
import logic.candy.CandyType;

import java.util.function.BiConsumer;

public class CandyTile extends StackPane {

    private static final int TILE_SIZE = 60;

    public CandyTile(int r, int c, Candy candy, boolean isSelected, boolean shouldHide, BiConsumer<Integer, Integer> onClick) {
        setPrefSize(TILE_SIZE, TILE_SIZE);

        this.setOnMouseClicked(e -> onClick.accept(r, c));

        // 1. วาดพื้นหลัง
        Rectangle bg = new Rectangle(TILE_SIZE, TILE_SIZE);
        if ((r + c) % 2 == 0) {
            bg.setFill(Color.WHITE);
        } else {
            bg.setFill(Color.web("#F0F0F0"));
        }
        bg.setStroke(Color.BLACK);
        bg.setStrokeWidth(1);

        if (isSelected) {
            bg.setFill(Color.web("#f1c40f"));
            bg.setStroke(Color.RED);
            bg.setStrokeWidth(3);
        }
        getChildren().add(bg);

        // 2. วาด Candy
        if (candy != null && !shouldHide) {
            drawCandyImage(candy);
        }
    }

    private void drawCandyImage(Candy candy) {
        String imagePath = getImagePath(candy);

        if (!imagePath.isEmpty()) {
            try {
                // โหลดรูปภาพ
                Image img = new Image(imagePath);
                ImageView imgView = new ImageView(img);

                imgView.setFitWidth(TILE_SIZE - 10);
                imgView.setFitHeight(TILE_SIZE - 10);
                imgView.setPreserveRatio(true);

                getChildren().add(imgView);
            } catch (Exception e) {
                // ถ้าหาไฟล์ไม่เจอ ให้ปริ้นบอก (ช่วย debug ได้ดีมาก)
                System.out.println("Image load failed: " + imagePath);
            }
        }

        // Layer: น้ำแข็ง
        if (candy.isFrozen()) {
            Rectangle ice = new Rectangle(TILE_SIZE, TILE_SIZE);
            ice.setFill(Color.rgb(173, 216, 230, 0.6));
            ice.setStroke(Color.BLUE);
            getChildren().add(ice);
        }

        // Layer: ตัวอักษรบอกประเภท (เฉพาะที่ไม่ใช่ Normal และ ColorBomb)
        if (candy.getType() != CandyType.NORMAL && candy.getType() != CandyType.COLOR_BOMB) {
            String typeText = getTypeCode(candy.getType());
            if (!typeText.isEmpty()) {
                Text txt = new Text(typeText);
                txt.setFont(Font.font("Arial", FontWeight.BOLD, 24));
                txt.setFill(Color.WHITE);
                txt.setStroke(Color.BLACK);
                txt.setStrokeWidth(1.5);
                getChildren().add(txt);
            }
        }
    }

    // --- ส่วนที่แก้ไข Path รูปภาพ ---
    private String getImagePath(Candy candy) {
        CandyType type = candy.getType();

        // กำหนดโฟลเดอร์หลัก (ถ้าโฟลเดอร์ชื่อ resources อยู่หน้าโปรเจกต์)
        String prefix = "file:resources/";

        // 1. กรณี Color Bomb
        if (type == CandyType.COLOR_BOMB) {
            return prefix + "color_bomb.png";
        }

        String colorName = "";
        switch (candy.getColor()) {
            case RED:    colorName = "red"; break;
            case GREEN:  colorName = "green"; break;
            case BLUE:   colorName = "blue"; break;
            case YELLOW: colorName = "yellow"; break;
            case PURPLE: colorName = "purple"; break;
            default: return "";
        }

        // 2. คืนค่า Path โดยรวม prefix เข้าไปด้วย
        return prefix + colorName + ".png";
    }

    private String getTypeCode(CandyType t) {
        if (t == null) return "";
        switch (t) {
            case STRIPED_HOR: return "H";
            case STRIPED_VER: return "V";
            case BOMB: return "B";
            default: return "";
        }
    }
}