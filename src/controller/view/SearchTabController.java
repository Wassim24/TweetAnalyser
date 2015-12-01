package controller.view;

import domain.Tweet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import services.algorithms.classification.*;
import services.twitter.TweetServiceImpl;
import twitter4j.RateLimitStatus;
import twitter4j.TwitterException;

import java.util.List;

public class SearchTabController
{
    @FXML private TextField keywordsTextField;
    @FXML private ListView<Tweet> foundTweetsListView;
    @FXML private Label queriesStatusLabel;
    @FXML private ComboBox queryAlgorithm;
    @FXML private Spinner<Integer> algorithmSettings;
    private List<Tweet> queriedTweets;

    public void onKeyPressSearchField()
    {
        /*if (keyEvent.getCode() == KeyCode.ENTER)
            onClickSearchForTweetsBtn();*/
    }

    public void onClickSearchForTweetsBtn()
    {
        try
        {
            this.foundTweetsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            this.foundTweetsListView.setCellFactory(param -> new RadioListCell());

            this.queriedTweets = TweetServiceImpl.getInstance().search(this.keywordsTextField.getText());

            this.onChangeAlgorithm();
        }
        catch (TwitterException ignored) {}

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

    public void applyAlgorithm(List<Tweet> unannotedTweets)
    {
        if (unannotedTweets.isEmpty())
            return;

        ObservableList<Tweet> annotedTweets =  FXCollections.observableArrayList();

        switch ((Algorithm)this.queryAlgorithm.getValue())
        {
            case DICTIONARY:
                annotedTweets.addAll(Glossary.compute(unannotedTweets));
                break;

            case KNN:
                annotedTweets.addAll(KNN.compute(unannotedTweets, algorithmSettings.getValue()));
                break;

            case BAYES:
                annotedTweets.addAll(Bayes.compute(unannotedTweets, algorithmSettings.getValue()));
                break;

            case FREQUENCY_BAYES:
                annotedTweets.addAll(BayesFrequency.compute(unannotedTweets, algorithmSettings.getValue()));
                break;

            case NONE:
                annotedTweets.addAll(unannotedTweets);
                break;

            default:
                annotedTweets.addAll(unannotedTweets);
                break;
        }

        this.foundTweetsListView.getItems().clear();
        this.foundTweetsListView.setItems(annotedTweets);
    }

    public void onChangeAlgorithm()
    {
        if (this.queryAlgorithm.getValue() == Algorithm.KNN || this.queryAlgorithm.getValue() == Algorithm.BAYES)
            algorithmSettings.setDisable(false);
        else
            algorithmSettings.setDisable(true);

        if (this.queriedTweets != null)
            applyAlgorithm(this.queriedTweets);
    }

    public void initialize()
    {
        this.queryAlgorithm.setItems(FXCollections.observableArrayList(Algorithm.values()));
        this.queryAlgorithm.setValue(Algorithm.DICTIONARY);

        algorithmSettings.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100));
        algorithmSettings.setEditable(true);
    }
}
