/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.view;

import io.github.kevinpita.comicstore.configuration.Configuration;
import io.github.kevinpita.comicstore.configuration.Resolution;
import io.github.kevinpita.comicstore.model.AuthorDto;
import io.github.kevinpita.comicstore.model.CollectionDto;
import io.github.kevinpita.comicstore.model.ComicDto;
import io.github.kevinpita.comicstore.util.CustomAlert;
import io.github.kevinpita.comicstore.util.i18n;
import io.github.kevinpita.comicstore.view.create.AuthorData;
import io.github.kevinpita.comicstore.view.create.CollectionData;
import io.github.kevinpita.comicstore.view.create.ComicData;
import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

@Slf4j
public class MainController {
    @FXML private Button settingsButton;
    @FXML private Tooltip settingsButtonTooltip;
    @FXML private Tooltip helpButton;
    @FXML private Tooltip languageButton;
    @FXML private Button listCollectionButton;
    @FXML private Button createComicButton;
    @FXML private Button listComicButton;
    @FXML private Button createAuthorButton;
    @FXML private Button listReportButton;
    @FXML private Button listAuthorButton;
    @FXML private Button createCollectionButton;
    @FXML private Label createButton;
    @FXML private Label listButton;
    @FXML private Label searchLabel;
    @FXML private BorderPane listParentPane;
    @FXML private TextField txtFieldSearchBar;
    @FXML private Tooltip removeSearchText;
    @FXML private Button removeSearchTextButton;

    private Button currentMenuButton;
    @Getter private static BorderPane mainPane;
    @Getter private static TextField searchBar;

    @FXML
    private void initialize() {
        // set current button to list collection button as it is the default menu
        currentMenuButton = listComicButton;

        // set static variables value
        mainPane = listParentPane;
        searchBar = txtFieldSearchBar;

        // load comic list as default
        loadCenterScreen("comic-list");

        // settings button animation
        rotateSettingsButton();

        // set bindings for i18n strings
        setStringBindings();
    }

    @FXML
    private void askForLanguageChange() {
        Alert alert =
                new Alert(
                        Alert.AlertType.CONFIRMATION,
                        i18n.getString("languageChangeConfirm"),
                        ButtonType.YES,
                        ButtonType.NO);

        alert.setTitle(i18n.getString("languageChangeTitle"));

        alert.setHeaderText(null);
        alert.setGraphic(null);

        // Set NO button as default
        DialogPane pane = alert.getDialogPane();
        for (ButtonType t : alert.getButtonTypes()) {

            Button b = (Button) pane.lookupButton(t);
            if (t == ButtonType.NO) {
                b.setDefaultButton(true);
                b.setText(i18n.getString("no"));
            } else {
                b.setDefaultButton(false);
                b.setText(i18n.getString("yes"));
            }
        }

        // center alert on parent window
        alert.initOwner(settingsButton.getScene().getWindow());
        Optional<ButtonType> value = alert.showAndWait();

        // if user didn't click yes, don't change language
        if (value.isEmpty() || value.get() != ButtonType.YES) {
            return;
        }

        // if current language is Spanish, change to Galician
        if (Locale.getDefault().equals(new Locale("es", "ES"))) {
            Locale.setDefault(new Locale("gl", "ES"));
        } else {
            Locale.setDefault(new Locale("es", "ES"));
        }

        // write new default language into config file
        Configuration.setLanguage(Locale.getDefault());
        Configuration.writeConfiguration();

        // reload binded strings
        i18n.update();
    }

    @FXML
    private void changeButton(ActionEvent event) {
        // get clicked source
        Button clickedButton = (Button) event.getSource();

        setCenterFXML(clickedButton);
    }

    private void setCenterFXML(Button clickedButton) {
        // if clicked button is the same as current button, don't do anything
        if (clickedButton.equals(currentMenuButton)) {
            return;
        }

        removeSearchText();

        // add selected style to clicked button
        clickedButton.getStyleClass().add("selected");
        // remove hover style from clicked button
        clickedButton.getStyleClass().remove("hoverButton");

        // remove selected style from previous selected button
        currentMenuButton.getStyleClass().remove("selected");
        // add hover style to previous selected button
        currentMenuButton.getStyleClass().add("hoverButton");

        // set current button to clicked button
        currentMenuButton = clickedButton;

        // get clicked button name
        String fxmlFile =
                currentMenuButton.getId().split("list")[1].split("Button")[0].toLowerCase();

        String hintTranslation = "";
        switch (fxmlFile) {
            case "comic":
                hintTranslation = "searchBarHint";
                break;
            case "collection":
                hintTranslation = "searchBarHintCollection";
                break;
            case "author":
                hintTranslation = "searchBarHintAuthor";
                break;
            case "report":
                hintTranslation = "searchBarHintReport";
                // as report is a special case, search bar is not needed
                txtFieldSearchBar.setDisable(true);
                removeSearchTextButton.setDisable(true);
                break;
        }
        // bind search bar hint text to i18n
        txtFieldSearchBar.promptTextProperty().bind(i18n.getStringBinding(hintTranslation));

        // enable search bar if coming from report scene
        if (!fxmlFile.equals("report") && txtFieldSearchBar.isDisable()) {
            txtFieldSearchBar.setDisable(false);
            removeSearchTextButton.setDisable(false);
        }

        // load new scene
        loadCenterScreen(fxmlFile + "-list");
    }

    @FXML
    private void openSettings() {
        try {
            // load configuration FXML
            FXMLLoader fxmlLoader = getFxmlLoader("configuration");
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();

            stage.setScene(scene);
            stage.setTitle(i18n.getString("configurationTitle"));

            // put configuration FXML in the center of the parent stage
            Bounds mainBounds = currentMenuButton.getScene().getRoot().getLayoutBounds();
            stage.setX(
                    currentMenuButton.getScene().getWindow().getX()
                            + (mainBounds.getWidth() - Resolution.CONFIGURATION.getWIDTH()) / 2);
            stage.setY(
                    currentMenuButton.getScene().getWindow().getY()
                            + (mainBounds.getHeight() - Resolution.CONFIGURATION.getHEIGHT()) / 2);

            stage.setResizable(false);
            stage.initOwner(currentMenuButton.getScene().getWindow());

            // make configuration screen modal
            stage.initModality(Modality.WINDOW_MODAL);

            stage.showAndWait();
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
            CustomAlert.showAlert(
                    i18n.getString("errorScreenLoad"), removeSearchText.getScene().getWindow());
        }
    }

    @FXML
    private void openAuthorCreator() {
        removeSearchText();
        setCenterFXML(listAuthorButton);
        openAuthorWindow(null);
    }

    @FXML
    private void openCollectionCreator() {
        removeSearchText();
        setCenterFXML(listCollectionButton);
        openCollectionWindow(null);
    }

    @FXML
    private void openComicCreator() {
        removeSearchText();
        setCenterFXML(listComicButton);
        openComicWindow(null);
    }

    @FXML
    private void removeSearchText() {
        txtFieldSearchBar.setText("");
    }

    public static void openAuthorWindow(AuthorDto author) {
        try {
            // load configuration FXML
            FXMLLoader fxmlLoader = getFxmlLoader("create/author-data");
            // load fxml to be able to get its controller
            Parent root = fxmlLoader.load();

            // get fxml controller and set author
            AuthorData authorData = fxmlLoader.getController();
            authorData.setAuthorDto(author);

            // reload added data
            authorData.lateInit();

            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.setScene(scene);
            stage.setTitle(i18n.getString("authorCreatorTitle"));

            // put configuration FXML in the center of the parent window
            BorderPane mainPane = getMainPane();
            Bounds mainBounds = mainPane.getScene().getRoot().getLayoutBounds();
            stage.setX(
                    mainPane.getScene().getWindow().getX()
                            + (mainBounds.getWidth() - Resolution.AUTHOR.getWIDTH()) / 2);
            stage.setY(
                    mainPane.getScene().getWindow().getY()
                            + (mainBounds.getHeight() - Resolution.AUTHOR.getHEIGHT()) / 2);

            stage.setResizable(false);
            stage.initOwner(mainPane.getScene().getWindow());
            // make configuration screen modal
            stage.initModality(Modality.WINDOW_MODAL);

            stage.showAndWait();
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
            CustomAlert.showAlert(
                    i18n.getString("errorScreenLoad"), mainPane.getScene().getWindow());
        }
    }

    public static void openCollectionWindow(CollectionDto collection) {

        try {
            // load configuration FXML
            FXMLLoader fxmlLoader = getFxmlLoader("create/collection-data");
            // load fxml to be able to get its controller
            Parent root = fxmlLoader.load();

            // get fxml controller and set collection
            CollectionData collectionData = fxmlLoader.getController();
            collectionData.setCollectionDto(collection);

            // reload added data
            collectionData.lateInit();

            Scene scene = new Scene(root);
            Stage stage = new Stage();

            // set stage and title
            stage.setScene(scene);
            stage.setTitle(i18n.getString("collectionCreatorTitle"));

            // put configuration FXML in the center of the parent window
            stage.setHeight(Resolution.COLLECTION.getHEIGHT());
            stage.setWidth(Resolution.COLLECTION.getWIDTH());
            stage.setMinHeight(Resolution.COLLECTION.getHEIGHT());
            stage.setMinWidth(Resolution.COLLECTION.getWIDTH());

            // center stage on parent stage
            BorderPane mainPane = getMainPane();
            Bounds mainBounds = mainPane.getScene().getRoot().getLayoutBounds();
            stage.setX(
                    mainPane.getScene().getWindow().getX()
                            + (mainBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY(
                    mainPane.getScene().getWindow().getY()
                            + (mainBounds.getHeight() - stage.getHeight()) / 2);

            stage.initOwner(mainPane.getScene().getWindow());

            // make configuration screen modal
            stage.initModality(Modality.WINDOW_MODAL);

            stage.showAndWait();
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
            CustomAlert.showAlert(
                    i18n.getString("errorScreenLoad"), mainPane.getScene().getWindow());
        }
    }

    public static void openComicWindow(ComicDto comic) {
        try {
            // load configuration FXML
            FXMLLoader fxmlLoader = getFxmlLoader("create/comic-data");

            // load fxml to be able to get its controller
            Parent root = fxmlLoader.load();

            // get fxml controller and set comic data
            ComicData comicData = fxmlLoader.getController();
            comicData.setComicDto(comic);

            // reloaded added data
            comicData.lateInit();

            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.setScene(scene);
            stage.setTitle(i18n.getString("collectionCreatorTitle"));

            // put configuration FXML in the center of the parent window
            stage.setHeight(Resolution.COMIC.getHEIGHT());
            stage.setWidth(Resolution.COMIC.getWIDTH());
            stage.setMinHeight(Resolution.COMIC.getHEIGHT());
            stage.setMinWidth(Resolution.COMIC.getWIDTH());

            // center stage on parent stage
            BorderPane mainPane = getMainPane();
            Bounds mainBounds = mainPane.getScene().getRoot().getLayoutBounds();
            stage.setX(
                    mainPane.getScene().getWindow().getX()
                            + (mainBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY(
                    mainPane.getScene().getWindow().getY()
                            + (mainBounds.getHeight() - stage.getHeight()) / 2);

            stage.initOwner(mainPane.getScene().getWindow());
            // make configuration screen modal
            stage.initModality(Modality.WINDOW_MODAL);

            stage.showAndWait();
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }

    public static FXMLLoader getFxmlLoader(String name) {
        return new FXMLLoader(
                MainWindow.class.getResource(
                        "/io/github/kevinpita/comicstore/view/" + name + ".fxml"),
                i18n.getResourceBundle());
    }

    // load given fxml name in border pane center position
    private void loadCenterScreen(String name) {
        FXMLLoader fxmlLoader = getFxmlLoader(name);
        try {
            listParentPane.setCenter(fxmlLoader.load());
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
            CustomAlert.showAlert(
                    i18n.getString("errorScreenLoad"), removeSearchText.getScene().getWindow());
        }
    }

    private void setStringBindings() {
        settingsButtonTooltip.textProperty().bind(i18n.getStringBinding("settingsButton.tooltip"));
        helpButton.textProperty().bind(i18n.getStringBinding("helpButton.tooltip"));
        languageButton.textProperty().bind(i18n.getStringBinding("translateButton.tooltip"));

        listComicButton.textProperty().bind(i18n.getStringBinding("comicButton"));
        listCollectionButton.textProperty().bind(i18n.getStringBinding("collectionButton"));
        listAuthorButton.textProperty().bind(i18n.getStringBinding("authorButton"));
        listReportButton.textProperty().bind(i18n.getStringBinding("reportButton"));

        createComicButton.textProperty().bind(i18n.getStringBinding("comicButton"));
        createCollectionButton.textProperty().bind(i18n.getStringBinding("collectionButton"));
        createAuthorButton.textProperty().bind(i18n.getStringBinding("authorButton"));

        createButton.textProperty().bind(i18n.getStringBinding("createButton"));
        listButton.textProperty().bind(i18n.getStringBinding("listButton"));

        searchLabel.textProperty().bind(i18n.getStringBinding("searchLabel"));
        txtFieldSearchBar.promptTextProperty().bind(i18n.getStringBinding("searchBarHint"));
        removeSearchText.textProperty().bind(i18n.getStringBinding("removeSearchText"));
    }

    private void rotateSettingsButton() {
        // rotate settings button 360 degrees in 0.5 seconds
        RotateTransition rotation = new RotateTransition(Duration.seconds(0.5), settingsButton);
        rotation.setCycleCount(1);
        rotation.setByAngle(360);

        // play rotate animation
        settingsButton.setOnMouseEntered(e -> rotation.play());
    }
}
