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

public class ChartsController
{
    @FXML private VBox chartsVBox;
    @FXML private ComboBox chartsCombobox;
    private PieChart pieChart;
    private BarChart barChart;

    public void initialize() {
        this.initializePieChart();
    }

    public void initializeBarChart()
    {
        CategoryAxis feelingAxis = new CategoryAxis();
        feelingAxis.setLabel("Feeling");

        NumberAxis tweetsNumberAxis = new NumberAxis();
        tweetsNumberAxis.setLabel("Number of tweets");

        this.barChart = new BarChart<>(feelingAxis, tweetsNumberAxis);
        this.barChart.setTitle("Tweets feelings chart");
        this.barChart.setBarGap(3);
        this.barChart.setCategoryGap(20);

        this.barChart.setLegendSide(Side.BOTTOM);
        this.barChart.setLegendVisible(false);

        XYChart.Series data = new XYChart.Series();
        data.setName("Positive");
        data.getData().add(new XYChart.Data("Positive", TweetDaoFactory.getInstance().getAll(Annotation.POSITIF).size()));
        data.getData().add(new XYChart.Data("Neutral", TweetDaoFactory.getInstance().getAll(Annotation.NEUTRE).size()));
        data.getData().add(new XYChart.Data("Negative", TweetDaoFactory.getInstance().getAll(Annotation.NEGATIF).size()));

        this.barChart.getData().addAll(data);

        this.chartsVBox.getChildren().clear();
        this.chartsVBox.setVgrow(barChart, Priority.ALWAYS);
        this.chartsVBox.getChildren().add(barChart);
    }

    public void initializePieChart()
    {
        this.pieChart = new PieChart();
        this.pieChart.setTitle("Tweets feelings chart");
        this.pieChart.setLegendSide(Side.BOTTOM);

        ObservableList<PieChart.Data> data = FXCollections.observableArrayList(
            new PieChart.Data("Positive", TweetDaoFactory.getInstance().getAll(Annotation.POSITIF).size()),
            new PieChart.Data("Neutral", TweetDaoFactory.getInstance().getAll(Annotation.NEUTRE).size()),
            new PieChart.Data("Negative", TweetDaoFactory.getInstance().getAll(Annotation.NEGATIF).size())
        );

        this.pieChart.setData(data);

        this.chartsVBox.getChildren().clear();
        this.chartsVBox.getChildren().add(pieChart);
        this.chartsVBox.setVgrow(pieChart, Priority.ALWAYS);
    }

    public void onChangeChart()
    {
        switch (this.chartsCombobox.getValue().toString())
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
