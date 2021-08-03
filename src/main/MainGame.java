package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import settings.Settings;
import java.io.IOException;
import java.util.Objects;

public class MainGame extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle(Settings.WINDOW_TITLE);
        primaryStage.setScene(new Scene(Objects.requireNonNull(this.createOptionsContent())));
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    private AnchorPane createOptionsContent() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainGame.class.getResource("/views/MainMenu.fxml"));
            return (AnchorPane) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
