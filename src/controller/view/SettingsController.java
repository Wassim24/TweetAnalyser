package controller.view;

import domain.Annotation;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import services.twitter.DictionaryServiceImpl;

import java.io.*;
import java.util.Properties;

public class SettingsController
{
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


        if (this.configFile == null)
        {
            this.configFile = new File("twitter4j.properties");
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

    public void initialize() {}

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
}
