package gui.base;

import gui.views.GameView;
import gui.views.StartView;
import javafx.stage.Stage;
import logic.controller.GameMode;

public class ViewManager {
    private static ViewManager instance;
    private Stage mainStage;

    private ViewManager() {}

    public static ViewManager getInstance() {
        if (instance == null) instance = new ViewManager();
        return instance;
    }

    public void setMainStage(Stage stage) {
        this.mainStage = stage;
    }

    public void showStartScreen() {
        StartView startView = new StartView();
        mainStage.setScene(startView.getScene());
        mainStage.setTitle("Candy Crush Clone - Menu");
        mainStage.show();
    }

    public void showGameScreen(GameMode mode) {
        GameView gameView = new GameView(mode);
        mainStage.setScene(gameView.getScene());
        mainStage.setTitle("Candy Crush Clone - Playing (" + mode + ")");
    }
}