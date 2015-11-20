package controller.view;

import domain.Tweet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import services.algorithms.classification.*;
import services.twitter.TweetServiceImpl;
import twitter4j.RateLimitStatus;
import twitter4j.TwitterException;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class SearchTabController implements javafx.fxml.Initializable
{
    @FXML private TextField keywordsTextField;
    @FXML private ListView<Tweet> foundTweetsListView;
    @FXML private Label queriesStatusLabel;
    @FXML private ComboBox queryAlgorithm;
    private List<Tweet> queriedTweets;

    public void onKeyPressSearchField(KeyEvent keyEvent)
    {
        /*if (keyEvent.getCode() == KeyCode.ENTER)
            onClickSearchForTweetsBtn();*/
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
        TweetServiceImpl.getInstance().addAll(this.foundTweetsListView.getSelectionModel().getSelectedItems());
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
            this.queriesStatusLabel.setText("The Rate Limit Status is unavailable.");
        }
    }

    public void onChangeAlgorithm()
    {
        if (this.queriedTweets == null || this.queriedTweets.isEmpty())
            return;

        this.foundTweetsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.foundTweetsListView.setCellFactory(param -> new RadioListCell());

        List<Tweet> copy = new ArrayList<Tweet>(this.queriedTweets.size());
        for (Tweet t : this.queriedTweets)
            copy.add(t.clone());

        ObservableList<Tweet> annotedTweet = FXCollections.observableArrayList();
        switch ((Algorithm)this.queryAlgorithm.getValue())
        {
            case DICTIONARY:
                annotedTweet.addAll(Glossary.compute(copy));
                break;

            case KNN:
                annotedTweet.addAll(KNN.compute(copy));
                break;

            case SIMPLE_BAYES:
                annotedTweet.addAll(Bayes.compute(copy));
                break;

            case FREQUENCY_BAYES:
                annotedTweet.addAll(BayesFrequency.compute(copy));
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
