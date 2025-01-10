package org.MainMenu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;

public class MainRun extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/MainMenu/MainMenu.fxml"));
            primaryStage.setTitle("Main Menu");
            primaryStage.setScene(new javafx.scene.Scene(root));
            //css
            primaryStage.getScene().getStylesheets().add(getClass().getResource("/org/MainMenu/MainMenu.css").toExternalForm());
            primaryStage.show();
        } catch (IOException e) {
            Logger.getLogger(MainRun.class.getName()).severe(e.getMessage());
        }
    }
}
