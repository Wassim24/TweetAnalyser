package controller.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import services.twitter.TweetServiceImpl;
import twitter4j.RateLimitStatus;
import twitter4j.TwitterException;

import java.util.stream.Collectors;

public class MainViewController {

    @FXML
    private ListView foundTweetsListView;
    @FXML
    private Label statusLabel;
    @FXML
    private TextField keywordsTextField;

    public void displayFoundTweetsOnListView()
    {
        initiateFoundTweetsListView();

        try
        {
            ObservableList foundTweets = FXCollections.observableArrayList();
            foundTweets.addAll(TweetServiceImpl.getInstance().search(this.keywordsTextField.getText()).stream().map(status -> "@" + status.getUser().getScreenName() + " : " + status.getText()).collect(Collectors.toList()));
            foundTweetsListView.setItems(foundTweets);
        }
        catch (TwitterException e)
        {
        }

        updateRateInStatusBar();
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

        statusLabel.setText(remainingQueriesStatus);
    }

    public void searchForTweets(ActionEvent actionEvent)
    {
        displayFoundTweetsOnListView();
    }
    public void initiateFoundTweetsListView () { this.foundTweetsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); }
    public void clearListViewFromResults(ActionEvent actionEvent)
    {
        foundTweetsListView.getItems().clear();
    }

}
