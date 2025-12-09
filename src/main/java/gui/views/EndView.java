package gui.views;

import gui.base.View;
import gui.base.ViewManager;
import gui.components.AnimatedBackground; // ใช้พื้นหลังเดิมให้สวยงาม
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


public class EndView implements View {

    private final Scene scene;

    public EndView(int score) {
        StackPane root = new StackPane();
        AnimatedBackground bg = new AnimatedBackground();
        root.getChildren().add(bg.getPane());

        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);

        String headerText = "GAME END";
        Text title = new Text(headerText);
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 60));
        title.setFill(Color.GOLD);
        title.setStroke(Color.web("#ff4081"));
        title.setStrokeWidth(2);

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.BLACK);
        shadow.setRadius(10);
        title.setEffect(shadow);

        Text scoreText = new Text("Final Score: " + score);
        scoreText.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        scoreText.setFill(Color.WHITE);
        scoreText.setEffect(shadow);

        Button menuBtn = new Button("Main Menu");
        styleButton(menuBtn, "#29b6f6"); // สีฟ้า
        menuBtn.setOnAction(e -> {
            ViewManager.getInstance().showStartScreen();
        });


        Button exitBtn = new Button("Exit Game");
        styleButton(exitBtn, "#f44336"); // สีแดง
        exitBtn.setOnAction(e -> {
            System.exit(0);
        });

        content.getChildren().addAll(title, scoreText, menuBtn, exitBtn);
        root.getChildren().add(content);

        this.scene = new Scene(root, 600, 700);

    }

    @Override
    public Scene getScene() {
        return scene;
    }

    private void styleButton(Button btn, String colorHex) {
        btn.setStyle(
                "-fx-background-color: " + colorHex + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 25;" +
                        "-fx-padding: 10 30 10 30;" +
                        "-fx-cursor: hand;"
        );
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: white; -fx-text-fill: " + colorHex + "; -fx-font-size: 18px; -fx-font-weight: bold; -fx-background-radius: 25; -fx-padding: 10 30 10 30; -fx-cursor: hand;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: " + colorHex + "; -fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-background-radius: 25; -fx-padding: 10 30 10 30; -fx-cursor: hand;"));
    }
}