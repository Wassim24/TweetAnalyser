package app;

import domain.TweetEntityBeans;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import services.twitter.TweetServiceImpl;
import twitter4j.TweetEntity;

import java.util.Date;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        /*
         * Test Database
         */

        TweetServiceImpl.getInstance();
        TweetServiceImpl.getInstance().add("Khaled", "Mon premier tweet", new Date(), "premier");
        TweetServiceImpl.getInstance().add("Khaled", "Mon deuxieme tweet", new Date(), "deuxieme");
        TweetServiceImpl.getInstance().add("Khaled", "Mon troisieme tweet", new Date(), "troisieme");
        System.out.println(TweetServiceImpl.getInstance().getAll());




        System.setProperty("prism.lcdtext", "false");

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/appStructure.fxml"));

        primaryStage.setTitle("Feelings Analyzer for Twitter | V 0.0");
        primaryStage.setScene(new Scene(root, 1000, 700));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}