package gui.components;

import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.Random;

public class AnimatedBackground {
    private final Pane pane;
    private final Random random = new Random();
    private final Color[] colors = {
            Color.HOTPINK, Color.CYAN, Color.GOLD, Color.LIMEGREEN, Color.MEDIUMPURPLE
    };

    public AnimatedBackground() {
        this.pane = new Pane();

        this.pane.setStyle("-fx-background-color: #2c3e50;");

        for (int i = 0; i < 20; i++) {
            createFloatingCandy();
        }
    }

    public Pane getPane() {
        return pane;
    }

    private void createFloatingCandy() {
        double radius = 10 + random.nextInt(20);
        Circle candy = new Circle(radius);
        candy.setFill(colors[random.nextInt(colors.length)]);
        candy.setOpacity(0.4); // ลดความทึบลงหน่อยให้ดูไม่กวนสายตาบนพื้นหลังมืด

        double startX = random.nextInt(800);
        double startY = random.nextInt(800);

        candy.setCenterX(startX);
        candy.setCenterY(startY);

        pane.getChildren().add(candy);
        animateCandy(candy);
    }

    private void animateCandy(Circle candy) {
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(candy);
        transition.setDuration(Duration.seconds(3 + random.nextInt(5)));
        transition.setByY(50 + random.nextInt(100));
        transition.setAutoReverse(true);
        transition.setCycleCount(Animation.INDEFINITE);
        transition.play();
    }
}