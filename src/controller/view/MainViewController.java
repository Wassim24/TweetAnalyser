package controller.view;

import domain.Annotation;
import domain.Dictionary;
import domain.Tweet;
import domain.Vocabulary;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import services.algorithms.classification.Algorithm;
import services.algorithms.classification.Bayes;
import services.algorithms.classification.Glossary;
import services.algorithms.classification.KNN;
import services.dao.DictionaryDaoFactory;
import services.dao.TweetDaoFactory;
import services.dao.VocabularyDaoFactory;
import services.twitter.DictionaryServiceImpl;
import services.twitter.TweetServiceImpl;
import services.twitter.VocabularyServiceImpl;
import twitter4j.RateLimitStatus;
import twitter4j.TwitterException;

import java.io.*;
import java.net.URL;
import java.util.*;

public class MainViewController
{

    @FXML private TextField consumerKeyTextField;
    @FXML private TextField consumerKeySecretTextField;
    @FXML private TextField accessTokenTextField;
    @FXML private TextField accessTokenSecretTextField;
    @FXML private TextField proxyHostTextField;
    @FXML private TextField proxyPortTextField;

    @FXML private Label databseLabel;

    @FXML private TableView tweetsInDatabaseTableView;

    @FXML private ComboBox dataBaseComboBox;
    @FXML private ComboBox annotatingMethodComboBox;
    @FXML private ComboBox savingMethodComboBox;
    private File configFile = null;

    public void onSelectingTableCombobox()
    {
        if (dataBaseComboBox.getValue().toString().equals("Tweets")) {
            databseLabel.setText("Your saved Tweets");
            this.displayTweetsSavedInDatabase();
        } else if (dataBaseComboBox.getValue().toString().equals("Dictionary")) {
            databseLabel.setText("Your saved Dictionary");
            this.displayDictionarySavedInDatabase();
        }else {
            databseLabel.setText("Your saved Vocabulary");
            this.displayVocabularySavedInDatabase();
        }
    }

    public void onClickResetSettingsBtn() throws IOException
    {
        saveProperties("", "", "", "", "", "");

        this.consumerKeyTextField.clear();
        this.consumerKeySecretTextField.clear();
        this.accessTokenTextField.clear();
        this.accessTokenSecretTextField.clear();
        this.proxyHostTextField.clear();
        this.proxyPortTextField.clear();
    }

    public void onClickSaveSettingsBtn() throws IOException
    {
        saveProperties(this.consumerKeyTextField.getText(), this.consumerKeySecretTextField.getText(), this.accessTokenTextField.getText(), this.accessTokenSecretTextField.getText(), this.proxyHostTextField.getText(), this.proxyPortTextField.getText());
    }

    private void saveProperties(String consumer, String consumerSec, String token, String tokenSec, String host, String port) throws IOException
    {
        Properties props = new Properties();
        props.setProperty("oauth.consumerKey", consumer);
        props.setProperty("oauth.consumerSecret", consumerSec);
        props.setProperty("oauth.accessToken", token);
        props.setProperty("oauth.accessTokenSecret", tokenSec);
        props.setProperty("http.proxyHost", host);
        props.setProperty("http.proxyPort", port);
        props.store(new FileOutputStream(new File("twitter4j.properties")), "User preferences");
    }

    public void onSelectingSettingsTab() throws IOException
    {
        if (this.configFile == null)
        {
            this.configFile = new File("twitter4j.properties");
            InputStream inputStream = new FileInputStream(this.configFile);
            Properties props = new Properties();
            props.load(inputStream);

            this.consumerKeyTextField.setText(props.getProperty("oauth.consumerKey"));
            this.consumerKeySecretTextField.setText(props.getProperty("oauth.consumerSecret"));
            this.accessTokenTextField.setText(props.getProperty("oauth.accessToken"));
            this.accessTokenSecretTextField.setText(props.getProperty("oauth.accessTokenSecret"));
            this.proxyHostTextField.setText(props.getProperty("http.proxyHost"));
            this.proxyPortTextField.setText(props.getProperty("http.proxyPort"));
        }
    }

    public void onSelectingDatabaseTab() {
        this.displayTweetsSavedInDatabase();
    }

    private void displayTweetsSavedInDatabase()
    {
        TableColumn id = new TableColumn<Tweet, String>("ID");
        TableColumn user = new TableColumn<Tweet, String>("User");
        TableColumn tweet = new TableColumn<Tweet, String>("Tweet");
        TableColumn created = new TableColumn<Tweet, Date>("Date");
        TableColumn keyword = new TableColumn<Tweet, String>("Keyword");
        TableColumn annotation = new TableColumn<Tweet, Annotation>("Annotation");
        TableColumn wordsCount = new TableColumn<Tweet, Integer>("Words Count");

        id.setCellValueFactory(new PropertyValueFactory("id"));
        user.setCellValueFactory(new PropertyValueFactory("username"));
        tweet.setCellValueFactory(new PropertyValueFactory("tweet"));
        created.setCellValueFactory(new PropertyValueFactory("date"));
        annotation.setCellValueFactory(new PropertyValueFactory("annotation"));
        keyword.setCellValueFactory(new PropertyValueFactory("keyword"));
        wordsCount.setCellValueFactory(new PropertyValueFactory("wordsCount"));

        tweetsInDatabaseTableView.getColumns().clear();
        tweetsInDatabaseTableView.getColumns().addAll(id, user, tweet, created, keyword, annotation, wordsCount);

        this.tweetsInDatabaseTableView.setItems((ObservableList<Tweet>) TweetServiceImpl.getInstance().getAll());

    }

    private void displayDictionarySavedInDatabase()
    {
        TableColumn id = new TableColumn<Dictionary, Integer>("ID");
        TableColumn word = new TableColumn<Dictionary, String>("Word");
        TableColumn annotation = new TableColumn<Dictionary, String>("Annotation");

        id.setCellValueFactory(new PropertyValueFactory("id"));
        word.setCellValueFactory(new PropertyValueFactory("word"));
        annotation.setCellValueFactory(new PropertyValueFactory("annotation"));

        tweetsInDatabaseTableView.getColumns().clear();
        tweetsInDatabaseTableView.getColumns().addAll(id, word, annotation);

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

        tweetsInDatabaseTableView.getColumns().clear();
        tweetsInDatabaseTableView.getColumns().addAll(id, word, nGramme, posocc, negocc, neuocc);

        this.tweetsInDatabaseTableView.setItems((ObservableList<Vocabulary>) VocabularyServiceImpl.getInstance().getAll());
    }
}