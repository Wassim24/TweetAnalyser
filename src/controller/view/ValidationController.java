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
import services.algorithms.classification.test.BayesFrequencyMixteTest;
import services.algorithms.classification.test.BayesFrequencyTest;
import services.algorithms.classification.test.BayesMixteTest;
import services.algorithms.classification.test.BayesTest;
import services.twitter.TweetServiceImpl;
import services.twitter.VocabularyServiceImpl;

import java.util.LinkedList;
import java.util.List;

public class ValidationController
{
    @FXML private Spinner<Integer> validationSetting;
    @FXML private VBox chartsVBox;
    private BarChart barChart;
    private int numberOfTweets;

    public void initialize()
    {
        this.validationSetting.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(3, 100));
        this.validationSetting.setEditable(true);
    }

    private void initializeBarChart()
    {
        int foldsNumber = this.validationSetting.getValue().intValue();

        CategoryAxis feelingAxis = new CategoryAxis();
        feelingAxis.setLabel("Algorithm");

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
        data.getData().add(new XYChart.Data("Bayes Mixte", validateBayes(foldsNumber, 2, Algorithm.BAYES_MIXTE)));
        data.getData().add(new XYChart.Data("Bayes Fq Uni", validateBayes(foldsNumber, 1, Algorithm.FREQUENCY_BAYES)));
        data.getData().add(new XYChart.Data("Bayes Fq Bi", validateBayes(foldsNumber, 2, Algorithm.FREQUENCY_BAYES)));
        data.getData().add(new XYChart.Data("Bayes Fq Mixte", validateBayes(foldsNumber, 2, Algorithm.FREQUENCY_BAYES_MIXTE)));
        data.getData().add(new XYChart.Data("Dictionary", validateGlossary(foldsNumber)));

        this.barChart.getData().addAll(data);

        this.chartsVBox.getChildren().clear();
        this.chartsVBox.setVgrow(barChart, Priority.ALWAYS);
        this.chartsVBox.getChildren().add(barChart);
    }

    private void lineValidation()
    {
        this.numberOfTweets = TweetServiceImpl.getInstance().getAll().size();

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of Month");

        //creating the chart
        LineChart<Number,Number> lineChart =
                new LineChart<>(xAxis, yAxis);

        lineChart.setTitle("Stock Monitoring, 2010");


        XYChart.Series dico = new XYChart.Series();
        dico.setName("Dictionary");

        XYChart.Series knn = new XYChart.Series();
        knn.setName("KNN");

        XYChart.Series bayes = new XYChart.Series();
        bayes.setName("Bayes");

        XYChart.Series bayesbi = new XYChart.Series();
        bayesbi.setName("Bayes bi");

        XYChart.Series bayesmixt = new XYChart.Series();
        bayesmixt.setName("Bayes Mixte");

        XYChart.Series bayesfreq = new XYChart.Series();
        bayesfreq.setName("Bayes Fq");

        XYChart.Series bayesfreq2 = new XYChart.Series();
        bayesfreq2.setName("Bayes Fq Bi");

        XYChart.Series bayesfreqMixt = new XYChart.Series();
        bayesfreqMixt.setName("BAyes Fq Mixte");

        for (int i = 3; i < 10; i++) {

            System.out.println(i);


            dico.getData().add(new XYChart.Data(i, validateGlossary(i)));
            knn.getData().add(new XYChart.Data(i, validateKNN(i)));
            bayes.getData().add(new XYChart.Data(i, validateBayes(i, 1, Algorithm.BAYES)));
            bayesbi.getData().add(new XYChart.Data(i, validateBayes(i, 2, Algorithm.BAYES)));
            bayesmixt.getData().add(new XYChart.Data(i, validateBayes(i, 2, Algorithm.BAYES_MIXTE)));
            bayesfreq.getData().add(new XYChart.Data(i, validateBayes(i, 1, Algorithm.FREQUENCY_BAYES)));
            bayesfreq2.getData().add(new XYChart.Data(i, validateBayes(i, 2, Algorithm.FREQUENCY_BAYES)));
            bayesfreqMixt.getData().add(new XYChart.Data(i, validateBayes(i, 2, Algorithm.FREQUENCY_BAYES_MIXTE)));

        }


        lineChart.getData().addAll(dico, knn, bayes
                ,bayesbi
                ,bayesmixt
                ,bayesfreq
                ,bayesfreq2,
                bayesfreqMixt );

        this.chartsVBox.getChildren().add(lineChart);
    }

    private float validateKNN(int numberOfGroups)
    {
        int step = this.numberOfTweets / numberOfGroups;
        List<List<Tweet>> all = this.getTweetsByGroup(numberOfGroups);
        List<Tweet> learningSet = new LinkedList<>();

        float goodAnnotation = 0;
        for (int i = 0; i < numberOfGroups; i++)
        {
            for (int j = 0; j < numberOfGroups; j++)
                if (i != j)
                    learningSet.addAll(all.get(j));

            goodAnnotation += (this.compareResults(all.get(i), KNN.compute(learningSet, all.get(i), (int) Math.sqrt(learningSet.size()), step)) / (float) all.get(i).size()) * 100;
            learningSet.clear();
        }

        return (float) goodAnnotation / (float) numberOfGroups;
    }

    private float validateGlossary(int numberOfGroups)
    {
        List<List<Tweet>> all = this.getTweetsByGroup(numberOfGroups);
        List<Tweet> learningSet = new LinkedList<Tweet>();

        float goodAnnotation = 0;
        for (int i = 0; i < numberOfGroups; i++)
        {
            for (int j = 0; j < numberOfGroups; j++)
                if (i != j)
                    learningSet.addAll(all.get(j));

            goodAnnotation += (compareResults(all.get(i), Glossary.compute(all.get(i))) / (float) all.get(i).size()) * 100;
            learningSet.clear();
        }

        return (float) goodAnnotation / (float) numberOfGroups;
    }

    private float validateBayes(int numberOfGroups, int ngrams, Algorithm algorithm)
    {
        List<List<Tweet>> all = this.getTweetsByGroup(numberOfGroups);
        List<Tweet> learningSet = new LinkedList<Tweet>();

        float goodAnnotation = 0;
        for (int i = 0; i < numberOfGroups; i++)
        {
            for (int j = 0; j < numberOfGroups; j++)
                if (i != j)
                    learningSet.addAll(all.get(j));

            switch (algorithm) {

                case BAYES:
                    goodAnnotation += ( this.compareResults(all.get(i), BayesTest.test(all.get(i), VocabularyServiceImpl.getInstance().buildAllVocabulary(ngrams, learningSet), ngrams)) / (float) all.get(i).size() ) * 100;
                    break;

                case BAYES_MIXTE:
                    goodAnnotation += ( this.compareResults(all.get(i), BayesMixteTest.test(all.get(i), VocabularyServiceImpl.getInstance().buildAllVocabulary(ngrams, learningSet), ngrams)) / (float) all.get(i).size() ) * 100;
                    break;

                case FREQUENCY_BAYES_MIXTE:
                    goodAnnotation += ( this.compareResults(all.get(i), BayesFrequencyMixteTest.test(all.get(i), VocabularyServiceImpl.getInstance().buildAllVocabulary(ngrams, learningSet), ngrams)) / (float) all.get(i).size() ) * 100;
                    break;

                default:
                    goodAnnotation += ( this.compareResults(all.get(i), BayesFrequencyTest.test(all.get(i), VocabularyServiceImpl.getInstance().buildAllVocabulary(ngrams, learningSet), ngrams)) / (float) all.get(i).size() ) * 100;
                    break;
            }

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

    private int compareResults(List<Tweet> annotated, List<Tweet> newAnnotation)
    {
        int differentResult = 0;

        for (Tweet t : annotated)
            if (! newAnnotation.contains(t))
                differentResult++;

        return differentResult;
    }

    public void onClickValidate() {
        this.initializeBarChart();
    }
}