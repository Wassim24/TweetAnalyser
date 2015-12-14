package controller.view;

import domain.Annotation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import services.twitter.DictionaryServiceImpl;
import services.twitter.VocabularyServiceImpl;

import java.io.*;
import java.util.Properties;

public class SettingsController
{
    @FXML private VBox vboxSettings;
    @FXML private ComboBox vocabularyGramsCombobox;
    @FXML private ScrollPane scrollPaneSettings;
    @FXML private ComboBox annotationDictionary;
    @FXML private TextField consumerKeyTextField;
    @FXML private TextField consumerKeySecretTextField;
    @FXML private TextField accessTokenTextField;
    @FXML private TextField accessTokenSecretTextField;
    @FXML private TextField proxyHostTextField;
    @FXML private TextField proxyPortTextField;
    @FXML private GridPane dictionarySettings;

    private File configFile = null;
    private File dictionary = null;

    public void onClickResetSettingsBtn() throws IOException
    {
        saveProperties("", "", "", "", "", "");

        this.consumerKeyTextField.clear();
        this.consumerKeySecretTextField.clear();
        this.accessTokenTextField.clear();
        this.accessTokenSecretTextField.clear();
        this.proxyHostTextField.clear();
        this.proxyPortTextField.clear();
    }

    public void onClickSaveSettingsBtn() throws IOException
    {
        saveProperties(this.consumerKeyTextField.getText(), this.consumerKeySecretTextField.getText(), this.accessTokenTextField.getText(), this.accessTokenSecretTextField.getText(), this.proxyHostTextField.getText(), this.proxyPortTextField.getText());
    }

    private void saveProperties(String consumer, String consumerSec, String token, String tokenSec, String host, String port) throws IOException
    {
        Properties props = new Properties();
        props.setProperty("oauth.consumerKey", consumer);
        props.setProperty("oauth.consumerSecret", consumerSec);
        props.setProperty("oauth.accessToken", token);
        props.setProperty("oauth.accessTokenSecret", tokenSec);
        props.setProperty("http.proxyHost", host);
        props.setProperty("http.proxyPort", port);
        props.store(new FileOutputStream(new File("twitter4j.properties")), "User preferences");
    }

    public void onSelectingSettingsTab() throws IOException
    {
        scrollPaneSettings.viewportBoundsProperty().addListener((observable, oldValue, newValue) -> {vboxSettings.setMinWidth(scrollPaneSettings.getWidth() - 17);});

        vocabularyGramsCombobox.getItems().addAll("0 Gramme", "1 Gramme", "2 Grammes");
        vocabularyGramsCombobox.setValue("1 Gramme");

        if (this.configFile == null)
        {
            this.configFile = new File("twitter4j.properties");

            if ( !configFile.exists() )
                if ( !configFile.createNewFile() )
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Oh Snap ! Something went wrong...");
                    alert.setContentText("I was unable to create the configuration file ! May be rights problem ?");
                    alert.setHeaderText(null);
                    alert.showAndWait();
                }


            InputStream inputStream = new FileInputStream(this.configFile);
            Properties props = new Properties();
            props.load(inputStream);

            this.consumerKeyTextField.setText(props.getProperty("oauth.consumerKey"));
            this.consumerKeySecretTextField.setText(props.getProperty("oauth.consumerSecret"));
            this.accessTokenTextField.setText(props.getProperty("oauth.accessToken"));
            this.accessTokenSecretTextField.setText(props.getProperty("oauth.accessTokenSecret"));
            this.proxyHostTextField.setText(props.getProperty("http.proxyHost"));
            this.proxyPortTextField.setText(props.getProperty("http.proxyPort"));
        }
    }

    public void initialize()
    {

    }

    public void loadDictionary() {
        FileChooser dictionaryLocator = new FileChooser();
        dictionaryLocator.setTitle("Locate a dictionary");
        dictionaryLocator.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        dictionary = dictionaryLocator.showOpenDialog(dictionarySettings.getScene().getWindow());
    }

    public void importDictionary()
    {
        if (dictionary == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Oh Snap ! Something went wrong...");
            alert.setContentText("You have to choose a file before importing !");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
        else
        {
            if (annotationDictionary.getValue().toString().equals("Positive Dictionary"))
                DictionaryServiceImpl.getInstance().addFromFile(dictionary, Annotation.POSITIF);
            else
                DictionaryServiceImpl.getInstance().addFromFile(dictionary, Annotation.NEGATIF);

        }
    }

    public void buildVocabulary()
    {
        int ngram = Integer.parseInt(vocabularyGramsCombobox.getValue().toString().charAt(0) + "");

        if(ngram == 1)
            VocabularyServiceImpl.getInstance().buildAllVocabulary(1);
        else if (ngram == 2)
            VocabularyServiceImpl.getInstance().buildAllVocabulary(2);
        else
            VocabularyServiceImpl.getInstance().buildAllVocabulary(2);
    }
}
