package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        System.setProperty("prism.lcdtext", "false");
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/appStructure.fxml"));

        primaryStage.setTitle("Feelings Analyzer for Twitter | V 0.0");
        primaryStage.getIcons().add(new Image("img/icon/TwitterLogo_white.png"));
        primaryStage.setScene(new Scene(root, 1000, 700));
        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}