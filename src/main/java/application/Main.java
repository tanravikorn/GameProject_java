package application;

import gui.base.ViewManager;
import javafx.application.Application;
import javafx.stage.Stage;

// เปลี่ยนชื่อคลาสให้ตรงกับชื่อไฟล์ (Main.java)
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 1. ส่ง Stage หลักไปให้ Manager เป็นคนถือไว้
        ViewManager.getInstance().setMainStage(primaryStage);

        // 2. สั่งให้ Manager เปิดหน้าเมนู (Start Screen) ขึ้นมา
        ViewManager.getInstance().showStartScreen();
    }

    public static void main(String[] args) {
        launch(args);
    }
}