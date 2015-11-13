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
import services.algorithms.classification.Glossary;
import services.algorithms.classification.KNN;
import services.dao.DictionaryDaoFactory;
import services.dao.TweetDaoFactory;
import services.dao.VocabularyDaoFactory;
import services.twitter.TweetServiceImpl;
import twitter4j.RateLimitStatus;
import twitter4j.TwitterException;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class MainViewController
{
    @FXML
    private TextField keywordsTextField;
    @FXML
    private ListView<Tweet> foundTweetsListView;
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
    private TableView<Tweet> tweetsInDatabaseTableView;
    @FXML
    private TableColumn<Tweet, String> id;
    @FXML
    private TableColumn<Tweet, String> user;
    @FXML
    private TableColumn<Tweet, String> tweet;
    @FXML
    private TableColumn<Tweet, Date> created;
    @FXML
    private TableColumn<Tweet, Annotation> annotation;
    @FXML
    private TableColumn<Tweet, String> keyword;
    @FXML
    private TableColumn<Tweet, Integer> wordsCount;
    @FXML
    private Label queriesStatusLabel;
    @FXML
    private ComboBox dataBaseComboBox;




    private List<Tweet> queriedTweets;
    @FXML
    private ComboBox queryAlgorithm;

    private File configFile = null;

    public void onKeyPressSearchField(KeyEvent keyEvent)
    {
        if (keyEvent.getCode() == KeyCode.ENTER)
            onClickSearchForTweetsBtn();
    }

    public void onClickSearchForTweetsBtn()
    {
        try
        {
            this.queriedTweets = TweetServiceImpl.getInstance().search(this.keywordsTextField.getText());
            this.onChangeAlgorithm();
        }
        catch (TwitterException ignored)
        {
        }

        updateRateInStatusBar();
    }

    public void onClickClearTweetsListBtn()
    {
        this.foundTweetsListView.getItems().clear();
    }

    public void onClickSaveAllTweetsBtn()
    {
        TweetServiceImpl.getInstance().addAll(this.foundTweetsListView.getItems());
    }

    public void onClickSaveSelectedTweetsBtn()
    {
        List<Tweet> tweetsToAnnote = new ArrayList<>();
        List<Tweet> tweetsToSave = new ArrayList<>();


        for (Tweet tweet : this.foundTweetsListView.getSelectionModel().getSelectedItems())
            if(tweet.getAnnotationValue() == -2) tweetsToAnnote.add(tweet); else tweetsToSave.add(tweet);


        if (tweetsToAnnote.size() != 0)
        {
            tweetsToSave.addAll(KNN.compute(tweetsToAnnote, 10));
            //TweetServiceImpl.getInstance().addAll(tweetsToSave);

            //tweetsToSave.addAll(new Glossary(tweetsToAnnote).compute());

        }//else TweetServiceImpl.getInstance().addAll(tweetsToSave);
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

    public void onSelectingDatabaseTab()
    {
        this.displayTweetsSavedInDatabase();
    }

    private void displayTweetsSavedInDatabase()
    {

        TableColumn id = new TableColumn<Tweet, String>("ID");
        TableColumn user = new TableColumn<Tweet, String>("User");
        TableColumn tweet = new TableColumn<Tweet, String>("Tweet");
        TableColumn created = new TableColumn<Tweet, Date>("Date");
        TableColumn keyword = new TableColumn<Tweet, Annotation>("Annotation");
        TableColumn annotation = new TableColumn<Tweet, String>("Keyword");
        TableColumn wordsCount = new TableColumn<Tweet, Integer>("Words Count");

        id.setCellValueFactory(new PropertyValueFactory("id"));
        user.setCellValueFactory(new PropertyValueFactory("username"));
        tweet.setCellValueFactory(new PropertyValueFactory("tweet"));
        created.setCellValueFactory(new PropertyValueFactory("date"));
        keyword.setCellValueFactory(new PropertyValueFactory("keyword"));
        annotation.setCellValueFactory(new PropertyValueFactory("annotation"));
        wordsCount.setCellValueFactory(new PropertyValueFactory("wordsCount"));

        tweetsInDatabaseTableView.getColumns().clear();
        tweetsInDatabaseTableView.getColumns().addAll(id, user, tweet, created, keyword, annotation, wordsCount);

        this.tweetsInDatabaseTableView.setItems((ObservableList<Tweet>) TweetDaoFactory.getInstance().all());

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

        this.tweetsInDatabaseTableView.setItems((ObservableList<Dictionary>) DictionaryDaoFactory.getInstance().getAll());
    }

    private void displayVocabularySavedInDatabase()
    {

        TableColumn id = new TableColumn<Vocabulary, Integer>("ID");
        TableColumn word = new TableColumn<Vocabulary, String>("Word");
        TableColumn posocc = new TableColumn<Vocabulary, Integer>("Positive Occurences");
        TableColumn negocc= new TableColumn<Vocabulary, Integer> ("Negative Occurences");
        TableColumn neuocc = new TableColumn<Vocabulary, Integer>("Neutral Occurences");

        id.setCellValueFactory(new PropertyValueFactory("id"));
        word.setCellValueFactory(new PropertyValueFactory("word"));
        posocc.setCellValueFactory(new PropertyValueFactory("posocc"));
        negocc.setCellValueFactory(new PropertyValueFactory("negocc"));
        neuocc.setCellValueFactory(new PropertyValueFactory("neuocc"));

        tweetsInDatabaseTableView.getColumns().clear();
        tweetsInDatabaseTableView.getColumns().addAll(id, word, posocc, negocc, neuocc);

        this.tweetsInDatabaseTableView.setItems((ObservableList<Vocabulary>) VocabularyDaoFactory.getInstance().get());
    }

    private void updateRateInStatusBar()
    {
        try
        {
            RateLimitStatus status = TweetServiceImpl.getInstance().getRemainingSearchQueries();
            this.queriesStatusLabel.setText("You still have " + status.getRemaining() + " possible queries from " + status.getLimit() + ". It resets in : " + status.getSecondsUntilReset() / 60 + " minutes");
        }
        catch (TwitterException e)
        {
            this.queriesStatusLabel.setText("The Rate Limit Status is unavailable");
        }
    }

    public void onSelectingTableCombobox() {

        if(dataBaseComboBox.getValue().toString().equals("Tweets"))
            this.displayTweetsSavedInDatabase();
        else if(dataBaseComboBox.getValue().toString().equals("Dictionary"))
            this.displayDictionarySavedInDatabase();
        else
            this.displayVocabularySavedInDatabase();
    }

    public void onChangeAlgorithm()
    {
        if (this.queriedTweets == null || this.queriedTweets.isEmpty())
            return;

        this.foundTweetsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.foundTweetsListView.setCellFactory(param -> new RadioListCell());
        ObservableList<Tweet> annotedTweet = FXCollections.observableArrayList();

        List<Tweet> copy = new ArrayList<Tweet>(this.queriedTweets.size());
        for (Tweet t : this.queriedTweets)
            copy.add(t.clone());

        switch ((Algorithm)this.queryAlgorithm.getValue())
        {
            case DICTIONARY:
                annotedTweet.addAll(Glossary.compute(copy));
                break;

            case KNN:
                annotedTweet.addAll(KNN.compute(copy));
                break;

            case NONE:
            default:
                annotedTweet.addAll(copy);
                break;
        }

        this.foundTweetsListView.setItems(annotedTweet);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        this.queryAlgorithm.setItems(FXCollections.observableArrayList(Algorithm.values()));
        this.queryAlgorithm.setValue(Algorithm.DICTIONARY);
    }
}