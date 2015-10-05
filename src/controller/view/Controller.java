package controller.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class Controller {

    @FXML
    private ListView foundTweetsListView;
    @FXML
    private Label statusLabel;
    @FXML
    private TextField keywordsTextField;

    public void clearListViewFromResults(ActionEvent actionEvent) {
    }

    public void searchForTweets(ActionEvent actionEvent) {
    }
}
