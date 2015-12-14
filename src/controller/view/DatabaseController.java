package controller.view;

import domain.Annotation;
import domain.Dictionary;
import domain.Tweet;
import domain.Vocabulary;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import services.dao.TweetDaoFactory;
import services.dao.sqlite.TweetSqliteDaoImpl;
import services.twitter.DictionaryServiceImpl;
import services.twitter.TweetServiceImpl;
import services.twitter.VocabularyServiceImpl;
import java.util.*;

public class DatabaseController
{
    @FXML private Label databseLabel;
    @FXML private TableView tweetsInDatabaseTableView;
    @FXML private ComboBox dataBaseComboBox;

    private int selectedItem = 0;

    public void onSelectingTableCombobox()
    {
        switch (dataBaseComboBox.getValue().toString())
        {
            case "Tweets":
                this.databseLabel.setText("Your saved Tweets");
                this.displayTweetsSavedInDatabase();
                break;
            case "Dictionary":
                this.databseLabel.setText("Your saved Dictionary");
                this.displayDictionarySavedInDatabase();
                break;
            default:
                this.databseLabel.setText("Your saved Vocabulary");
                this.displayVocabularySavedInDatabase();
                break;
        }
    }

    public void onSelectingDatabaseTab()
    {
        this.initiateContextMenu();
        this.displayTweetsSavedInDatabase();
    }

    private void initiateContextMenu() {

        ContextMenu ctx = new ContextMenu();
        MenuItem positive = new MenuItem("Set Positive");
        MenuItem neutre = new MenuItem("Set Neutral");
        MenuItem negative = new MenuItem("Set Negative");

        positive.setOnAction(event -> {
            Tweet tweet = (Tweet) tweetsInDatabaseTableView.getSelectionModel().getSelectedItem();
            tweet.setAnnotation(Annotation.POSITIF);
            this.selectedItem = this.tweetsInDatabaseTableView.getItems().indexOf(tweet);
            if (TweetDaoFactory.getInstance().update(tweet))
                this.displayTweetsSavedInDatabase();

        });

        neutre.setOnAction(event -> {
            Tweet tweet = (Tweet) tweetsInDatabaseTableView.getSelectionModel().getSelectedItem();
            tweet.setAnnotation(Annotation.NEUTRE);
            this.selectedItem = this.tweetsInDatabaseTableView.getItems().indexOf(tweet);
            if (TweetDaoFactory.getInstance().update(tweet))
                this.displayTweetsSavedInDatabase();

        });

        negative.setOnAction(event -> {
            Tweet tweet = (Tweet) tweetsInDatabaseTableView.getSelectionModel().getSelectedItem();
            tweet.setAnnotation(Annotation.NEGATIF);
            this.selectedItem = this.tweetsInDatabaseTableView.getItems().indexOf(tweet);
            if (TweetDaoFactory.getInstance().update(tweet))
                this.displayTweetsSavedInDatabase();

        });

        ctx.getItems().addAll(positive, neutre, negative);

        tweetsInDatabaseTableView.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> {
                    if (e.getButton() == MouseButton.SECONDARY)
                        ctx.show(tweetsInDatabaseTableView, e.getScreenX(), e.getScreenY());
                    else
                        ctx.hide();
                });
    }

    private void displayTweetsSavedInDatabase()
    {

        TableColumn id = new TableColumn<Tweet, String>("ID");
        TableColumn user = new TableColumn<Tweet, String>("User");
        TableColumn tweet = new TableColumn<Tweet, String>("Tweet");
        TableColumn created = new TableColumn<Tweet, Date>("Date");
        TableColumn keyword = new TableColumn<Tweet, String>("Keyword");
        TableColumn annotation = new TableColumn<Tweet, Annotation>("Annotation");

        id.setCellValueFactory(new PropertyValueFactory("id"));
        user.setCellValueFactory(new PropertyValueFactory("username"));
        tweet.setCellValueFactory(new PropertyValueFactory("tweet"));
        created.setCellValueFactory(new PropertyValueFactory("date"));
        annotation.setCellValueFactory(new PropertyValueFactory("annotation"));
        keyword.setCellValueFactory(new PropertyValueFactory("keyword"));

        this.tweetsInDatabaseTableView.getColumns().clear();
        this.tweetsInDatabaseTableView.getColumns().addAll(id, user, tweet, created, keyword, annotation);

        this.tweetsInDatabaseTableView.setItems((ObservableList<Tweet>) TweetServiceImpl.getInstance().getAll());
        this.tweetsInDatabaseTableView.getSelectionModel().select(this.selectedItem);

    }

    private void displayDictionarySavedInDatabase()
    {
        TableColumn id = new TableColumn<Dictionary, Integer>("ID");
        TableColumn word = new TableColumn<Dictionary, String>("Word");
        TableColumn annotation = new TableColumn<Dictionary, String>("Annotation");

        id.setCellValueFactory(new PropertyValueFactory("id"));
        word.setCellValueFactory(new PropertyValueFactory("word"));
        annotation.setCellValueFactory(new PropertyValueFactory("annotation"));

        this.tweetsInDatabaseTableView.getColumns().clear();
        this.tweetsInDatabaseTableView.getColumns().addAll(id, word, annotation);

        this.tweetsInDatabaseTableView.setItems((ObservableList<Dictionary>) DictionaryServiceImpl.getInstance().getAll());
    }

    private void displayVocabularySavedInDatabase()
    {
        TableColumn id = new TableColumn<Vocabulary, Integer>("ID");
        TableColumn word = new TableColumn<Vocabulary, String>("Word");
        TableColumn nGramme = new TableColumn<Vocabulary, Integer>("Gramme");
        TableColumn posocc = new TableColumn<Vocabulary, Integer>("Positive Occurences");
        TableColumn negocc = new TableColumn<Vocabulary, Integer>("Negative Occurences");
        TableColumn neuocc = new TableColumn<Vocabulary, Integer>("Neutral Occurences");

        id.setCellValueFactory(new PropertyValueFactory("id"));
        word.setCellValueFactory(new PropertyValueFactory("word"));
        nGramme.setCellValueFactory(new PropertyValueFactory("ngramme"));
        posocc.setCellValueFactory(new PropertyValueFactory("posocc"));
        negocc.setCellValueFactory(new PropertyValueFactory("negocc"));
        neuocc.setCellValueFactory(new PropertyValueFactory("neuocc"));

        this.tweetsInDatabaseTableView.getColumns().clear();
        this.tweetsInDatabaseTableView.getColumns().addAll(id, word, nGramme, posocc, negocc, neuocc);

        this.tweetsInDatabaseTableView.setItems((ObservableList<Vocabulary>) VocabularyServiceImpl.getInstance().getAll());
    }
}