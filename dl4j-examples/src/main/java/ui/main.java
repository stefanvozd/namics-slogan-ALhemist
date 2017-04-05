package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class main extends Application {

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Main.fxml"));
        Scene scene = new Scene(root, 500, 500);
        scene.getStylesheets().add("/fxml/main.css");

        primaryStage.setResizable(false);
        primaryStage.setScene(scene); // start te scene
        primaryStage.setTitle("Slogan. Alchemist. Namics.");
        primaryStage.show();

    }
}
