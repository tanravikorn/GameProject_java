package gui.base;

import gui.views.StartView;
import javafx.stage.Stage;

public class ViewManager {
    private static ViewManager instance;
    private Stage mainStage;

    private ViewManager() {}

    public static ViewManager getInstance() {
        if (instance == null) {
            instance = new ViewManager();
        }
        return instance;
    }

    public void setMainStage(Stage stage) {
        this.mainStage = stage;
    }

    public void showStartScreen() {
        StartView startView = new StartView();
        mainStage.setScene(startView.getScene());
        mainStage.setTitle("Candy Bomb - Menu");
        mainStage.show();
    }

    public void showGameScreen() {
        // สร้าง GameView ใหม่ทุกครั้งที่เรียกใช้ (เพื่อเป็นการ Reset เกมใหม่)
        // เราเรียก Constructor ของ GameView ที่เราเพิ่งสร้าง
        gui.views.GameView gameView = new gui.views.GameView();

        // สลับ Scene ใน Stage หลัก
        mainStage.setScene(gameView.getScene());
        mainStage.setTitle("Candy Bomb - Playing");
    }
}