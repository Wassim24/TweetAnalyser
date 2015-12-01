package controller.view;

import domain.Annotation;
import domain.Tweet;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class RadioListCell extends ListCell<Tweet>
{
    @Override
    public void updateItem(Tweet obj, boolean empty)
    {
        super.updateItem(obj, empty);
        if (empty || obj == null)
        {
            super.setText(null);
            super.setGraphic(null);
        }
        else
        {
            ToggleGroup radioButtonsFeelingsGroup = new ToggleGroup();
            RadioButton negative = new RadioButton();
            RadioButton neutral = new RadioButton();
            RadioButton positive = new RadioButton();
            Label tweet = new Label();

            // Adding the radio buttons to a ToggleGroup to avoid multiple checking
            negative.setToggleGroup(radioButtonsFeelingsGroup);
            neutral.setToggleGroup(radioButtonsFeelingsGroup);
            positive.setToggleGroup(radioButtonsFeelingsGroup);
            // Settings the css class names for the radio buttons
            negative.getStyleClass().add("anoteNegativeRadio");
            neutral.getStyleClass().add("anoteNeutralRadio");
            positive.getStyleClass().add("anotePositiveRadio");

            switch (obj.getAnnotationValue())
            {
                case -1:
                    negative.getStyleClass().add("anoteNegativeRadio-selected");
                    break;
                case 0:
                    neutral.getStyleClass().add("anoteNeutralRadio-selected");
                    break;
                case 1:
                    positive.getStyleClass().add("anotePositiveRadio-selected");
                    break;
            }

            // Adding the tweet to the label and a css class name
            tweet.setText(obj.getTweet()); tweet.getStyleClass().add("foundTweetContent");

            // Used to generate action listeners for the radio buttons
            negative.setOnAction(event -> {
                obj.setAnnotation(Annotation.NEGATIF);
                // Remove classes from unselected radios
                neutral.getStyleClass().remove("anoteNeutralRadio-selected");
                positive.getStyleClass().remove("anotePositiveRadio-selected");

                // Add "-selected" class to the selected button
                negative.getStyleClass().add("anoteNegativeRadio-selected");
            });

            neutral.setOnAction(event -> {
                obj.setAnnotation(Annotation.NEUTRE);

                // Remove classes from unselected radios
                negative.getStyleClass().remove("anoteNegativeRadio-selected");
                positive.getStyleClass().remove("anotePositiveRadio-selected");

                // Add "-selected" class to the selected button
                neutral.getStyleClass().add("anoteNeutralRadio-selected");
            });

            positive.setOnAction(event -> {
                obj.setAnnotation(Annotation.POSITIF);

                // Remove classes from unselected radios
                neutral.getStyleClass().remove("anoteNeutralRadio-selected");
                negative.getStyleClass().remove("anoteNegativeRadio-selected");

                // Add "-selected" class to the selected button
                positive.getStyleClass().add("anotePositiveRadio-selected");
            });


            // Used to set the graphics of the list view
            // Adding everything to the layout
            super.setGraphic(new HBox(negative, neutral, positive, tweet));
        }
    }
}