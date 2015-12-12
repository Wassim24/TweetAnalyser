package controller.view;

import domain.Annotation;
import domain.Tweet;
import domain.Vocabulary;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import services.algorithms.classification.Bayes;
import services.algorithms.classification.BayesFrequency;
import services.algorithms.classification.Glossary;
import services.algorithms.classification.KNN;
import services.dao.TweetDaoFactory;
import services.twitter.VocabularyServiceImpl;

import java.util.LinkedList;
import java.util.List;

public class ValidationController
{
    @FXML private Spinner validationSetting;
    @FXML private VBox chartsVBox;
    private BarChart barChart;

    public void initialize() {

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

        XYChart.Series data = new XYChart.Series();
        data.setName("Algorithms");
        data.getData().add(new XYChart.Data("KNN", validateKNN(foldsNumber)));
        data.getData().add(new XYChart.Data("Bayes", validateBayes(foldsNumber, 1)));
        data.getData().add(new XYChart.Data("Bayes 1", validateBayes(foldsNumber, 2)));
        data.getData().add(new XYChart.Data("Bayes Fq", validateBayesFrequency(foldsNumber, 1)));
        data.getData().add(new XYChart.Data("Bayes Fq 1", validateBayesFrequency(foldsNumber, 2)));
        data.getData().add(new XYChart.Data("Dictionary", validateGlossary(foldsNumber)));


        this.barChart.getData().addAll(data);

        this.chartsVBox.getChildren().clear();
        this.chartsVBox.setVgrow(barChart, Priority.ALWAYS);
        this.chartsVBox.getChildren().add(barChart);
    }

    private float validateKNN(int numberOfGroups)
    {
        int total = TweetDaoFactory.getInstance().getAll().size();
        int goodAnnotation = 0;
        int step = total / numberOfGroups;
        List<List<Tweet>> all = new LinkedList<>();
        List<Tweet> learningSet = new LinkedList<>();
        List<Annotation> annotations = new LinkedList<>();

        for (int i = 0; i < numberOfGroups; i++) {all.add(TweetDaoFactory.getInstance().get(i*step, step));}

        for (int i = 0; i < numberOfGroups; i++) {

            for (int j = 0; j < numberOfGroups; j++)
                if (i == j) continue;
                else learningSet.addAll(all.get(j));

            all.get(i).forEach(tweet -> annotations.add(tweet.getAnnotation()));
            goodAnnotation += compareResults(KNN.validate(all.get(i), learningSet, (int) Math.sqrt(all.get(i).size()), step), annotations, step);
            annotations.clear();
            learningSet.clear();
        }

        return (float) goodAnnotation / (float) numberOfGroups;
    }

    private float validateGlossary(int numberOfGroups)
    {
        int total = TweetDaoFactory.getInstance().getAll().size();
        int goodAnnotation = 0;
        int step = total / numberOfGroups;

        List<List<Tweet>> all = new LinkedList<>();
        List<Tweet> learningSet = new LinkedList<>();
        List<Annotation> annotations = new LinkedList<>();

        for (int i = 0; i < numberOfGroups; i++) {all.add(TweetDaoFactory.getInstance().get(i*step, step));}

        for (int i = 0; i < numberOfGroups; i++) {
            for (int j = 0; j < numberOfGroups; j++)
                if (i == j) continue;
                else learningSet.addAll(all.get(j));

            all.get(i).forEach(tweet -> annotations.add(tweet.getAnnotation()));
            goodAnnotation += compareResults(Glossary.validate(all.get(i)), annotations, step);
            annotations.clear();
            learningSet.clear();
        }

        return (float) goodAnnotation / (float) numberOfGroups;
    }

    private float validateBayes(int numberOfGroups, int ngrams)
    {
        int total = TweetDaoFactory.getInstance().getAll().size();
        int goodAnnotation = 0;
        int step = total / numberOfGroups;
        List<List<Tweet>> all = new LinkedList<>();
        List<Tweet> learningSet = new LinkedList<>();
        List<Annotation> annotations = new LinkedList<>();

        List<Vocabulary> vocabularyLearning;

        for (int i = 0; i < numberOfGroups; i++) {all.add(TweetDaoFactory.getInstance().get(i*step, step));}

        for (int i = 0; i < numberOfGroups; i++) {

            for (int j = 0; j < numberOfGroups; j++)
                if (i == j) continue;
                else learningSet.addAll(all.get(j));

            all.get(i).forEach(tweet -> annotations.add(tweet.getAnnotation()));
            vocabularyLearning = VocabularyServiceImpl.getInstance().buildAllVocabulary(ngrams, learningSet);
            goodAnnotation += compareResults(Bayes.validate(all.get(i), vocabularyLearning, ngrams), annotations, step);
            annotations.clear();
            learningSet.clear();
        }

        return (float) goodAnnotation / (float) numberOfGroups;
    }

    private float validateBayesFrequency(int numberOfGroups, int ngrams)
    {
        int total = TweetDaoFactory.getInstance().getAll().size();
        int goodAnnotation = 0;
        int step = total / numberOfGroups;
        List<List<Tweet>> all = new LinkedList<>();
        List<Tweet> learningSet = new LinkedList<>();
        List<Annotation> annotations = new LinkedList<>();

        List<Vocabulary> vocabularyLearning;

        for (int i = 0; i < numberOfGroups; i++) {all.add(TweetDaoFactory.getInstance().get(i*step, step));}

        for (int i = 0; i < numberOfGroups; i++) {

            for (int j = 0; j < numberOfGroups; j++)
                if (i == j) continue;
                else learningSet.addAll(all.get(j));

            all.get(i).forEach(tweet -> annotations.add(tweet.getAnnotation()));
            vocabularyLearning = VocabularyServiceImpl.getInstance().buildAllVocabulary(ngrams, learningSet);
            goodAnnotation += compareResults(BayesFrequency.validate(all.get(i), vocabularyLearning, ngrams), annotations, step);
            annotations.clear();
            learningSet.clear();
        }

        return (float) goodAnnotation / (float) numberOfGroups;
    }

    public int compareResults(List<Tweet> annotated, List<Annotation> annotations, int step)
    {
        int goodAnnotation = 0;

        for (int i = 0; i < step; i++) {
            if(annotations.get(i) == annotated.get(i).getAnnotation())
                goodAnnotation++;
        }

        return goodAnnotation;
    }

    public void onClickValidate(ActionEvent actionEvent) {
        this.initializeBarChart();
    }

    public void onKeyPressedValidate(Event event) {
    }
}
