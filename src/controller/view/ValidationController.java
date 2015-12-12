package controller.view;

import domain.Tweet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.chart.*;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import services.algorithms.classification.*;
import services.algorithms.classification.test.BayesFrequencyTest;
import services.algorithms.classification.test.BayesTest;
import services.twitter.TweetServiceImpl;
import services.twitter.VocabularyServiceImpl;

import java.util.LinkedList;
import java.util.List;

public class ValidationController
{
    @FXML private Spinner validationSetting;
    @FXML private VBox chartsVBox;
    private BarChart barChart;
    private int numberOfTweets;

    public void initialize()
    {
        this.validationSetting.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(3, 100));
        this.validationSetting.setEditable(true);
    }

    public void initializeBarChart()
    {
        CategoryAxis feelingAxis = new CategoryAxis();
        feelingAxis.setLabel("Algorithm");

        int foldsNumber = (int) this.validationSetting.getValue();
        NumberAxis tweetsNumberAxis = new NumberAxis();
        tweetsNumberAxis.setLabel("Error Rate");

        this.barChart = new BarChart<>(feelingAxis, tweetsNumberAxis);
        this.barChart.setTitle("");
        this.barChart.setBarGap(3);
        this.barChart.setCategoryGap(20);

        this.barChart.setLegendSide(Side.BOTTOM);
        this.barChart.setLegendVisible(false);

        this.numberOfTweets = TweetServiceImpl.getInstance().getAll().size();

        XYChart.Series data = new XYChart.Series();
        data.setName("Algorithms");
        data.getData().add(new XYChart.Data("KNN", validateKNN(foldsNumber)));
        data.getData().add(new XYChart.Data("Bayes Uni", validateBayes(foldsNumber, 1, Algorithm.BAYES)));
        data.getData().add(new XYChart.Data("Bayes Bi", validateBayes(foldsNumber, 2, Algorithm.BAYES)));
        data.getData().add(new XYChart.Data("Bayes Fq Uni", validateBayes(foldsNumber, 1, Algorithm.FREQUENCY_BAYES)));
        data.getData().add(new XYChart.Data("Bayes Fq Bi", validateBayes(foldsNumber, 2, Algorithm.FREQUENCY_BAYES)));
        data.getData().add(new XYChart.Data("Dictionary", validateGlossary(foldsNumber)));


        this.barChart.getData().addAll(data);

        this.chartsVBox.getChildren().clear();
        this.chartsVBox.setVgrow(barChart, Priority.ALWAYS);
        this.chartsVBox.getChildren().add(barChart);
    }

    private float validateKNN(int numberOfGroups)
    {
        int step = this.numberOfTweets / numberOfGroups;
        List<List<Tweet>> all = this.getTweetsByGroup(numberOfGroups);
        List<Tweet> learningSet = new LinkedList<>();

        int goodAnnotation = 0;
        for (int i = 0; i < numberOfGroups; i++)
        {
            for (int j = 0; j < numberOfGroups; j++)
                if (i != j)
                    learningSet.addAll(all.get(j));

            goodAnnotation += this.compareResults(all.get(i), KNN.compute(learningSet, all.get(i), (int) Math.sqrt(all.get(i).size()), step));
            learningSet.clear();
        }

        return (float) goodAnnotation / (float) numberOfGroups;
    }

    private float validateGlossary(int numberOfGroups)
    {
        List<List<Tweet>> all = this.getTweetsByGroup(numberOfGroups);
        List<Tweet> learningSet = new LinkedList<Tweet>();

        int goodAnnotation = 0;
        for (int i = 0; i < numberOfGroups; i++)
        {
            for (int j = 0; j < numberOfGroups; j++)
                if (i != j)
                    learningSet.addAll(all.get(j));

            goodAnnotation += compareResults(all.get(i), Glossary.compute(all.get(i)));
            learningSet.clear();
        }

        return (float) goodAnnotation / (float) numberOfGroups;
    }

    private float validateBayes(int numberOfGroups, int ngrams, Algorithm algorithm)
    {
        List<List<Tweet>> all = this.getTweetsByGroup(numberOfGroups);
        List<Tweet> learningSet = new LinkedList<Tweet>();

        int goodAnnotation = 0;
        for (int i = 0; i < numberOfGroups; i++)
        {
            for (int j = 0; j < numberOfGroups; j++)
                if (i != j)
                    learningSet.addAll(all.get(j));

            if (algorithm == Algorithm.BAYES)
                goodAnnotation += this.compareResults(all.get(i), BayesTest.test(all.get(i), VocabularyServiceImpl.getInstance().buildAllVocabulary(ngrams, learningSet), ngrams));
            else
                goodAnnotation += this.compareResults(all.get(i), BayesFrequencyTest.test(all.get(i), VocabularyServiceImpl.getInstance().buildAllVocabulary(ngrams, learningSet), ngrams));

            learningSet.clear();
        }

        return (float) goodAnnotation / (float) numberOfGroups;
    }

    private List<List<Tweet>> getTweetsByGroup(int numberOfGroups)
    {
        List<List<Tweet>> response = new LinkedList<List<Tweet>>();

        for (int i = 0, step = this.numberOfTweets / numberOfGroups; i < numberOfGroups; i++)
            response.add(TweetServiceImpl.getInstance().getBetween(i * step, step));

        return response;
    }

    public int compareResults(List<Tweet> annotated, List<Tweet> newAnnotation)
    {
        int differentResult = 0;

        for (Tweet t : annotated)
            if (! newAnnotation.contains(t))
                differentResult++;

        return differentResult;
    }

    public void onClickValidate(ActionEvent actionEvent) {
        this.initializeBarChart();
    }
}
