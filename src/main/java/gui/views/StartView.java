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
import logic.controller.GameMode;

public class StartView implements View {

    private final Scene scene;

    public StartView() {
        StackPane root = new StackPane();


        AnimatedBackground bg = new AnimatedBackground();
        root.getChildren().add(bg.getPane());

        VBox content = new VBox(25);
        content.setAlignment(Pos.CENTER);

        Text title = new Text("SUPER\nCANDY");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 70));
        title.setFill(Color.WHITE); // สีขาว
        title.setStroke(Color.web("#ff4081"));
        title.setStrokeWidth(3);
        title.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.web("#ff4081"));
        shadow.setRadius(20);
        title.setEffect(shadow);

        Button normalBtn = new Button("Normal Mode");
        styleButton(normalBtn, "#29b6f6");
        normalBtn.setOnAction(e -> ViewManager.getInstance().showGameScreen(GameMode.NORMAL));

        Button hardBtn = new Button("Hard Mode");
        styleButton(hardBtn, "#FF9800");
        hardBtn.setOnAction(e -> ViewManager.getInstance().showGameScreen(GameMode.HARD));

        Button exitBtn = new Button("Exit");
        styleButton(exitBtn, "#f44336");
        exitBtn.setOnAction(e -> System.exit(0));

        content.getChildren().addAll(title, normalBtn, hardBtn, exitBtn);
        root.getChildren().add(content);

        scene = new Scene(root, 600, 700);
    }

    private void styleButton(Button btn, String colorHex) {
        btn.setStyle(
                "-fx-background-color: " + colorHex + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 30;" +
                        "-fx-padding: 12 40 12 40;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 10, 0, 0, 5);" // เพิ่มเงาให้ปุ่มดูมีมิติ
        );
        btn.setPrefWidth(250);
    }

    @Override
    public Scene getScene() {
        return scene;
    }
}