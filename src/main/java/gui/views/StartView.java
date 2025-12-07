package gui.views;

import gui.base.View;
import gui.base.ViewManager;
import gui.components.AnimatedBackground;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class StartView implements View {

    private final StackPane root;
    private final Scene scene;

    public StartView() {
        root = new StackPane();

        // 1. ใส่พื้นหลัง Animation
        AnimatedBackground bg = new AnimatedBackground();
        root.getChildren().add(bg.getPane());

        // 2. สร้าง Layout ตรงกลาง
        VBox content = new VBox(20); // ระยะห่าง 20px
        content.setAlignment(Pos.CENTER);

        // 3. ชื่อเกม
        Text title = new Text("Candy Bomb");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 60));
        title.setFill(Color.web("#ff4081"));
        title.setStroke(Color.WHITE);
        title.setStrokeWidth(3);
        title.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        // เงาตัวหนังสือ
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.LIGHTPINK);
        shadow.setRadius(10);
        title.setEffect(shadow);

        // 4. ปุ่ม Start Game
        Button startButton = new Button("Start Game");
        styleButton(startButton);

        // Action เมื่อกดปุ่ม
        startButton.setOnAction(e -> {
            ViewManager.getInstance().showGameScreen();
        });

        // ปุ่ม Exit
        Button exitButton = new Button("Exit");
        styleButton(exitButton);
        exitButton.setOnAction(e -> System.exit(0));

        content.getChildren().addAll(title, startButton, exitButton);
        root.getChildren().add(content);

        // สร้าง Scene ขนาด 800x600 (หรือตามต้องการ)
        scene = new Scene(root, 600, 700);
    }

    private void styleButton(Button btn) {
        // ใช้ CSS ตกแต่งปุ่มให้ดูน่ารัก
        btn.setStyle(
                "-fx-background-color: #ff80ab;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 30;" +
                        "-fx-padding: 10 30 10 30;" +
                        "-fx-cursor: hand;"
        );

        // Effect ตอนเอาเมาส์ไปโดน
        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: #ff4081;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 30;" +
                        "-fx-padding: 10 30 10 30;" +
                        "-fx-cursor: hand;"
        ));

        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: #ff80ab;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 30;" +
                        "-fx-padding: 10 30 10 30;" +
                        "-fx-cursor: hand;"
        ));
    }

    @Override
    public Scene getScene() {
        return scene;
    }
}