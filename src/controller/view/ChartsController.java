package controller.view;

import domain.Annotation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import services.dao.TweetDaoFactory;

public class ChartsController {

    @FXML private VBox chartsVBox;
    @FXML private ComboBox chartsCombobox;

    private PieChart pieChart;
    private BarChart barChart;


    public void initializeBarChart() {

        CategoryAxis feelingAxis = new CategoryAxis();
        feelingAxis.setLabel("Feeling");

        NumberAxis tweetsNumberAxis = new NumberAxis();
        tweetsNumberAxis.setLabel("Number of tweets");

        barChart = new BarChart<>(feelingAxis, tweetsNumberAxis);
        barChart.setTitle("Tweets feelings chart");
        barChart.setBarGap(3);
        barChart.setCategoryGap(20);

        barChart.setLegendSide(Side.BOTTOM);
        barChart.setLegendVisible(false);

        XYChart.Series data = new XYChart.Series();
        data.setName("Positive");
        data.getData().add(new XYChart.Data("Positive", TweetDaoFactory.getInstance().getAll(Annotation.POSITIF).size()));
        data.getData().add(new XYChart.Data("Neutral", TweetDaoFactory.getInstance().getAll(Annotation.NEUTRE).size()));
        data.getData().add(new XYChart.Data("Negative", TweetDaoFactory.getInstance().getAll(Annotation.NEGATIF).size()));

        barChart.getData().addAll(data);

        chartsVBox.getChildren().clear();
        chartsVBox.setVgrow(barChart, Priority.ALWAYS);
        chartsVBox.getChildren().add(barChart);

    }

    public void initializePieChart() {

        pieChart = new PieChart();
        pieChart.setTitle("Tweets feelings chart");
        pieChart.setLegendSide(Side.BOTTOM);

        ObservableList<PieChart.Data> data = FXCollections.observableArrayList(
            new PieChart.Data("Positive", TweetDaoFactory.getInstance().getAll(Annotation.POSITIF).size()),
            new PieChart.Data("Neutral", TweetDaoFactory.getInstance().getAll(Annotation.NEUTRE).size()),
            new PieChart.Data("Negative", TweetDaoFactory.getInstance().getAll(Annotation.NEGATIF).size())
        );

        pieChart.setData(data);

        chartsVBox.getChildren().clear();
        chartsVBox.getChildren().add(pieChart);
        chartsVBox.setVgrow(pieChart, Priority.ALWAYS);
    }

    public void initialize() {
        initializePieChart();
    }

    public void onChangeChart() {

        switch (chartsCombobox.getValue().toString())
        {
            case "Bar Chart" :
                initializeBarChart();
                break;
            default:
                initializePieChart();
                break;

        }
    }
}
