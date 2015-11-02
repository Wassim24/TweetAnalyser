package controller.view;

import domain.Annotation;
import domain.TweetEntityBeans;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

public class RadioListCell extends ListCell<TweetEntityBeans>
{
    @Override
    public void updateItem(TweetEntityBeans obj, boolean empty)
    {
        super.updateItem(obj, empty);


        if (empty)
        {
            setText(null);
            setGraphic(null);
        }
        else
        {
            ToggleGroup radioButtonsFeelingsGroup = new ToggleGroup();
            RadioButton negative = new RadioButton();
            RadioButton neutral = new RadioButton();
            RadioButton positive = new RadioButton();
            Label tweet = new Label();

            // Settings the css class names for the radio buttons
            negative.getStyleClass().add("anoteNegativeRadio");
            neutral.getStyleClass().add("anoteNeutralRadio");
            positive.getStyleClass().add("anotePositiveRadio");

            switch (obj.getAnnotation())
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

            // Adding the radio buttons to a ToggleGroup to avoid multiple checking
            negative.setToggleGroup(radioButtonsFeelingsGroup);
            neutral.setToggleGroup(radioButtonsFeelingsGroup);
            positive.setToggleGroup(radioButtonsFeelingsGroup);

            // Adding the tweet to the label and a css class name
            tweet.setText(obj.getTweet()); tweet.getStyleClass().add("foundTweetContent");

            // Adding everything to the layout
            HBox layoutForFeelingsTweet = new HBox();
            layoutForFeelingsTweet.getChildren().addAll(negative, neutral, positive, tweet);

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
            setGraphic(layoutForFeelingsTweet);
        }
    }

    /**
     * @param obj of type TweetEntityBeans
     * @implNote Used to generate the action listeners for the radio buttons
     */
    private void generateActions(TweetEntityBeans obj)
    {
    }
}