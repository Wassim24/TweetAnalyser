package controller.view;

import domain.Tweet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import services.algorithms.classification.*;
import services.twitter.TweetServiceImpl;
import twitter4j.RateLimitStatus;
import twitter4j.TwitterException;

import java.util.List;

public class SearchTabController
{
    @FXML private VBox searchLayout;
    @FXML private TextField keywordsTextField;
    @FXML private ListView<Tweet> foundTweetsListView;
    @FXML private Label queriesStatusLabel;
    @FXML private ComboBox queryAlgorithm;
    @FXML private Spinner<Integer> algorithmSettings;
    private List<Tweet> queriedTweets;

    private int algoSettingValue;

    public void initialize()
    {
        this.queryAlgorithm.setItems(FXCollections.observableArrayList(Algorithm.values()));
        this.queryAlgorithm.setValue(Algorithm.DICTIONARY);

        this.algorithmSettings.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100));
        this.algorithmSettings.setEditable(true);
    }

    public void onKeyPressSearchField(KeyEvent keyEvent)
    {
        if (keyEvent.getCode() == KeyCode.ENTER)
            this.onClickSearchForTweetsBtn();
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

        this.updateRateInStatusBar();
    }

    public void onClickDisplayCharts()
    {
        Stage charts = new Stage();
        VBox chartsVBox = new VBox();
        PieChart pieChart = new PieChart();
        Scene chartsScene = new Scene(chartsVBox);

        pieChart.getStylesheets().add("css/bootstrap.css");

        pieChart.setTitle("Tweets feelings chart");
        pieChart.setLegendSide(Side.BOTTOM);

        int feelings[] = countFeelingsInFoundTweets();

        ObservableList<PieChart.Data> data = FXCollections.observableArrayList(
                new PieChart.Data("Positive", feelings[0]),
                new PieChart.Data("Neutral", feelings[1]),
                new PieChart.Data("Negative", feelings[2])
        );

        pieChart.setData(data);

        chartsVBox.getChildren().clear();
        chartsVBox.getChildren().add(pieChart);
        VBox.setVgrow(pieChart, Priority.ALWAYS);

        charts.initModality(Modality.WINDOW_MODAL);
        charts.setScene(chartsScene);
        charts.show();
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

    public void applyAlgorithm(List<Tweet> unannotedTweets, int algoSettingValue)
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
                annotedTweets.addAll(KNN.compute(unannotedTweets, algoSettingValue));
                break;

            case BAYES:
                annotedTweets.addAll(Bayes.compute(unannotedTweets, algoSettingValue));
                break;

            case FREQUENCY_BAYES:
                annotedTweets.addAll(BayesFrequency.compute(unannotedTweets, algoSettingValue));
                break;

            case NONE:
            default:
                annotedTweets.addAll(unannotedTweets);
                break;
        }

        this.foundTweetsListView.getItems().clear();
        this.foundTweetsListView.setItems(annotedTweets);
    }

    public void onChangeAlgorithm()
    {
        algoSettingValue = algorithmSettings.getValue();

        if (this.queryAlgorithm.getValue() == Algorithm.KNN || this.queryAlgorithm.getValue() == Algorithm.BAYES || this.queryAlgorithm.getValue() == Algorithm.FREQUENCY_BAYES)
        {
            this.algorithmSettings.setDisable(false);
            this.algorithmSettings.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100));

            if (this.queryAlgorithm.getValue() == Algorithm.BAYES || this.queryAlgorithm.getValue() == Algorithm.FREQUENCY_BAYES)
                this.algorithmSettings.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 2));
        }
        else
            this.algorithmSettings.setDisable(true);

        if (this.queriedTweets != null)
            this.applyAlgorithm(this.queriedTweets, algoSettingValue);
    }

    private int[] countFeelingsInFoundTweets()
    {
        int feelings[] = {0, 0, 0};

        if(this.queriedTweets == null)
            return feelings;

        this.queriedTweets.forEach(
            tweet ->
            {
                switch (tweet.getAnnotation())
                {
                    case POSITIF:
                        feelings[0]++;
                        break;
                    case NEUTRE:
                        feelings[1]++;
                        break;
                    case NEGATIF:
                    default:
                        feelings[2]++;
                        break;
                }
        });

        return feelings;
    }
}
