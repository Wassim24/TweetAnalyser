package controller.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import domain.TweetEntityBeans;
import services.twitter.TweetServiceImpl;
import twitter4j.RateLimitStatus;
import twitter4j.TwitterException;

public class MainViewController
{
    @FXML
    private ListView<TweetEntityBeans> foundTweetsListView;
    @FXML
    private Label statusLabel;
    @FXML
    private TextField keywordsTextField;

    @FXML
    public void onKeyPressKeywords(KeyEvent keyEvent)
    {
        if (keyEvent.getCode() == KeyCode.ENTER)
            searchForTweets(new ActionEvent());
    }

    @FXML
    public void searchForTweets(ActionEvent actionEvent)
    {
        this.foundTweetsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        try
        {
            ObservableList<TweetEntityBeans> foundTweets = FXCollections.observableArrayList();
            foundTweets.addAll(TweetServiceImpl.getInstance().search(this.keywordsTextField.getText()));
            this.foundTweetsListView.setItems(foundTweets);
        }
        catch (TwitterException e)
        {
        }

        updateRateInStatusBar();
    }

    @FXML
    public void clearListViewFromResults(ActionEvent actionEvent)
    {
        this.foundTweetsListView.getItems().clear();
    }

    @FXML
    public void onClickSaveAllTweetsBtn(ActionEvent actionEvent)
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
