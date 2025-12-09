package application;

import gui.base.ViewManager;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        ViewManager.getInstance().setMainStage(primaryStage);

        ViewManager.getInstance().showStartScreen();
    }

    public static void main(String[] args) {
        launch(args);
    }
}