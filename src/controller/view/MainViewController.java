package controller.view;

import domain.Annotation;
import domain.TweetEntityBeans;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import services.dao.DaoFactory;
import services.labeling.LabelingServiceImpl;
import services.twitter.TweetServiceImpl;
import twitter4j.RateLimitStatus;
import twitter4j.TwitterException;

import java.io.*;
import java.util.*;

public class MainViewController
{

    @FXML
    private TextField keywordsTextField;
    @FXML
    private ListView<TweetEntityBeans> foundTweetsListView;

    @FXML
    private TextField consumerKeyTextField;
    @FXML
    private TextField consumerKeySecretTextField;
    @FXML
    private TextField accessTokenTextField;
    @FXML
    private TextField accessTokenSecretTextField;
    @FXML
    private TextField proxyHostTextField;
    @FXML
    private TextField proxyPortTextField;

    @FXML
    private TableView<TweetEntityBeans> tweetsInDatabaseTableView;
    @FXML
    private TableColumn<TweetEntityBeans, String> id;
    @FXML
    private TableColumn<TweetEntityBeans, String> user;
    @FXML
    private TableColumn<TweetEntityBeans, String> tweet;
    @FXML
    private TableColumn<TweetEntityBeans, Date> created;
    @FXML
    private TableColumn<TweetEntityBeans, Annotation> annotation;
    @FXML
    private TableColumn<TweetEntityBeans, String> keyword;
    @FXML
    private TableColumn<TweetEntityBeans, Integer> wordsCount;

    @FXML
    private Label queriesStatusLabel;

    private File configFile = null;

    /*** Methods ***/

    public void onKeyPressKeywords(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER)
            onClickSearchForTweetsBtn();
    }

    public void onClickSearchForTweetsBtn() {

        this.foundTweetsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.foundTweetsListView.setCellFactory(param -> new RadioListCell());

        try {
            ObservableList<TweetEntityBeans> foundTweets = FXCollections.observableArrayList();
            foundTweets.addAll(TweetServiceImpl.getInstance().search(this.keywordsTextField.getText()));
            this.foundTweetsListView.setItems(foundTweets);
        }
        catch (TwitterException ignored)
        {
        }

        updateRateInStatusBar();

    }

    public void onClickClearTweetsListBtn() {this.foundTweetsListView.getItems().clear();}

    public void onClickSaveAllTweetsBtn() {
        TweetServiceImpl.getInstance().addAll(this.foundTweetsListView.getItems());}

    public void onClickSaveSelectedTweetsBtn() {

        List<TweetEntityBeans> tweetsToAnnote = new ArrayList<>();
        List<TweetEntityBeans> tweetsToSave = new ArrayList<>();


        for (TweetEntityBeans tweet : this.foundTweetsListView.getSelectionModel().getSelectedItems())
            if(tweet.getAnnotation() == -2) tweetsToAnnote.add(tweet); else tweetsToSave.add(tweet);

        if(tweetsToAnnote.size() != 0)
            if(tweetsToSave.addAll(LabelingServiceImpl.getInstance().KNNLabeling(tweetsToAnnote, 3)))
                TweetServiceImpl.getInstance().addAll(tweetsToSave);
        else
                TweetServiceImpl.getInstance().addAll(tweetsToSave);
    }

    public void onClickResetSettingsBtn() throws IOException{

        saveProperties("", "", "", "", "", "");

        consumerKeyTextField.clear();
        consumerKeySecretTextField.clear();
        accessTokenTextField.clear();
        accessTokenSecretTextField.clear();
        proxyHostTextField.clear();
        proxyPortTextField.clear();
    }

    public void onClickSaveSettingsBtn() throws IOException{
        saveProperties(consumerKeyTextField.getText(),
                consumerKeySecretTextField.getText(),
                accessTokenTextField.getText(),
                accessTokenSecretTextField.getText(),
                proxyHostTextField.getText(),
                proxyPortTextField.getText());
    }

    private void saveProperties(String consumer, String consumerSec, String token, String tokenSec, String host, String port) throws IOException {
        File configFile = new File("twitter4j.properties");

        Properties props = new Properties();
        FileOutputStream ofs = new FileOutputStream(configFile);

        props.setProperty("oauth.consumerKey", consumer);
        props.setProperty("oauth.consumerSecret", consumerSec);
        props.setProperty("oauth.accessToken", token);
        props.setProperty("oauth.accessTokenSecret", tokenSec);

        props.setProperty("http.proxyHost", host);
        props.setProperty("http.proxyPort", port);

        props.store(ofs, "User preferences");
    }

    public void onSelectingSettingsTab() throws IOException {

        if(configFile == null) {

            configFile = new File("twitter4j.properties");
            InputStream inputStream = new FileInputStream(configFile);
            Properties props = new Properties();

            props.load(inputStream);
            consumerKeyTextField.setText(props.getProperty("oauth.consumerKey"));
            consumerKeySecretTextField.setText(props.getProperty("oauth.consumerSecret"));
            accessTokenTextField.setText(props.getProperty("oauth.accessToken"));
            accessTokenSecretTextField.setText(props.getProperty("oauth.accessTokenSecret"));
        }
    }

    public void onSelectingDatabaseTab() {
        displayTweetsSavedInDatabase();
    }


    private void displayTweetsSavedInDatabase(){

        id.setCellValueFactory(new PropertyValueFactory("id"));
        user.setCellValueFactory(new PropertyValueFactory("username"));
        tweet.setCellValueFactory(new PropertyValueFactory("tweet"));
        created.setCellValueFactory(new PropertyValueFactory("date"));
        keyword.setCellValueFactory(new PropertyValueFactory("keyword"));
        annotation.setCellValueFactory(new PropertyValueFactory("annotation"));
        wordsCount.setCellValueFactory(new PropertyValueFactory("wordsCount"));

        tweetsInDatabaseTableView.setItems((ObservableList<TweetEntityBeans>) DaoFactory.getInstance().all());
    }

    private void updateRateInStatusBar() {
        try
        {
            RateLimitStatus status = TweetServiceImpl.getInstance().getRemainingSearchQueries();
            this.queriesStatusLabel.setText("You still have " + status.getRemaining() + " possible queries from " + status.getLimit() + ". It resets in : " + status.getSecondsUntilReset() / 60 + " minutes");
        }
        catch (TwitterException e){this.queriesStatusLabel.setText("*****");}


    }
}
