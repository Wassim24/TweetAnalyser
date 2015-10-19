package controller.view;

import domain.TweetEntityBeans;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import services.twitter.TweetServiceImpl;
import twitter4j.RateLimitStatus;
import twitter4j.TwitterException;

public class MainViewController
{

    @FXML
    private Label statusLabel;
    @FXML
    private TextField keywordsTextField;


    @FXML
    private ListView<TweetEntityBeans> foundTweetsListView;

    @FXML
    public void onKeyPressKeywords(KeyEvent keyEvent)
    {
        if (keyEvent.getCode() == KeyCode.ENTER)
            searchForTweets();
    }

    @FXML
    public void searchForTweets()
    {
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

    @FXML
    public void clearListViewFromResults()
    {
        this.foundTweetsListView.getItems().clear();
    }

    @FXML
    public void onClickSaveAllTweetsBtn()
    {
        TweetServiceImpl.getInstance().addAll(this.foundTweetsListView.getItems());
    }

    private void updateRateInStatusBar()
    {
        String remainingQueriesStatus;

        try
        {
            RateLimitStatus status = TweetServiceImpl.getInstance().getRemainingSearchQueries();
            remainingQueriesStatus = "You still have " + status.getRemaining() + " possible queries from " + status.getLimit() + ". It resets in : " + status.getSecondsUntilReset() / 60 + " minutes";
        }
        catch (TwitterException e)
        {
            remainingQueriesStatus = "*****";
        }

        this.statusLabel.setText(remainingQueriesStatus);
    }

}
