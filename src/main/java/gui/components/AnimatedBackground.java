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
        // สีพื้นหลังหลัก (Gradient หรือสีพื้น)
        this.pane.setStyle("-fx-background-color: linear-gradient(to bottom, #ffe6f2, #e6f7ff);");

        // สร้างลูกกวาดลอยๆ จำนวน 20 อัน
        for (int i = 0; i < 20; i++) {
            createFloatingCandy();
        }
    }

    public Pane getPane() {
        return pane;
    }

    private void createFloatingCandy() {
        // ใช้วงกลมแทนลูกกวาดก่อน (เปลี่ยนเป็นรูปภาพ Image หรือ SVGPath ได้ทีหลัง)
        double radius = 10 + random.nextInt(20);
        Circle candy = new Circle(radius);
        candy.setFill(colors[random.nextInt(colors.length)]);
        candy.setOpacity(0.6); // โปร่งแสงนิดๆ

        // สุ่มตำแหน่งเริ่มต้น
        double startX = random.nextInt(800); // สมมติจอ 800
        double startY = random.nextInt(600);

        candy.setCenterX(startX);
        candy.setCenterY(startY);

        pane.getChildren().add(candy);
        animateCandy(candy);
    }

    private void animateCandy(Circle candy) {
        // ทำให้ขยับขึ้นลงแบบสุ่ม
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(candy);
        transition.setDuration(Duration.seconds(3 + random.nextInt(5)));
        transition.setByY(50 + random.nextInt(100)); // ระยะทางขยับ
        transition.setAutoReverse(true);
        transition.setCycleCount(Animation.INDEFINITE);
        transition.play();
    }
}