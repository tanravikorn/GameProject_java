package gui.components;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import logic.candy.Candy;
import logic.candy.CandyColor;
import logic.candy.CandyType;

import java.util.function.BiConsumer;

public class CandyTile extends StackPane {

    private static final int TILE_SIZE = 60;

    public CandyTile(int r, int c, Candy candy, boolean isSelected, boolean shouldHide, BiConsumer<Integer, Integer> onClick) {
        setPrefSize(TILE_SIZE, TILE_SIZE);

        this.setOnMouseClicked(e -> onClick.accept(r, c));

        Rectangle bg = new Rectangle(TILE_SIZE, TILE_SIZE);

        if ((r + c) % 2 == 0) {
            bg.setFill(Color.WHITE);
        } else {
            bg.setFill(Color.web("#F5F5F5"));
        }
        bg.setStroke(Color.LIGHTGRAY);
        bg.setStrokeWidth(1);

        if (isSelected) {
            bg.setFill(Color.web("#FFF9C4")); // เหลืองอ่อน
            bg.setStroke(Color.ORANGE);
            bg.setStrokeWidth(3);
        }
        getChildren().add(bg);

        if (candy != null && !shouldHide) {
            drawCandyImage(candy);
        }
    }

    private void drawCandyImage(Candy candy) {

        String imagePath = getImagePath(candy);

        if (!imagePath.isEmpty()) {
            try {
                Image img = new Image(imagePath);
                ImageView imgView = new ImageView(img);

                imgView.setFitWidth(TILE_SIZE - 8);
                imgView.setFitHeight(TILE_SIZE - 8);
                imgView.setPreserveRatio(true);

                getChildren().add(imgView);
            } catch (Exception e) {
                System.out.println("Image not found: " + imagePath);
            }
        }

        if (candy.isFrozen()) {
            Rectangle ice = new Rectangle(TILE_SIZE, TILE_SIZE);
            ice.setFill(Color.rgb(176, 224, 230, 0.5)); // สีฟ้าจางๆ
            ice.setStroke(Color.DEEPSKYBLUE);
            ice.setStrokeWidth(2);
            getChildren().add(ice);
        }
    }

    private String getImagePath(Candy candy) {
        CandyType type = candy.getType();

        if (type == CandyType.COLOR_BOMB) {
            return "candy/color_bomb.png";
        }

        String colorStr = "";
        switch (candy.getColor()) {
            case RED:    colorStr = "red"; break;
            case GREEN:  colorStr = "green"; break;
            case BLUE:   colorStr = "blue"; break;
            case YELLOW: colorStr = "yellow"; break;
            case PURPLE: colorStr = "purple"; break;
            default: return "";
        }

        String typePrefix = "";
        switch (type) {
            case NORMAL:      typePrefix = "candy_"; break; // candy_red.png
            case STRIPED_HOR: typePrefix = "hor_";   break; // hor_red.png
            case STRIPED_VER: typePrefix = "ver_";   break; // ver_red.png
            case BOMB:        typePrefix = "bomb_";  break; // bomb_red.png
            default:          typePrefix = "candy_"; break;
        }

        return "candy/"+typePrefix + colorStr + ".png";
    }
}