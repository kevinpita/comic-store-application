/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.view;

import io.github.kevinpita.comicstore.utils.i18n;
import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

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

    private Button currentMenuButton;
    @FXML private BorderPane listParentPane;

    public void initialize() {
        currentMenuButton = listComicButton;
        rotateSettingsButton();
        setStringBindings();
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
    }

    private void rotateSettingsButton() {
        RotateTransition rotation = new RotateTransition(Duration.seconds(0.5), settingsButton);
        rotation.setCycleCount(1);
        rotation.setByAngle(360);

        settingsButton.setOnMouseEntered(e -> rotation.play());
    }

    @FXML
    public void askForLanguageChange() {
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
            ((Button) pane.lookupButton(t)).setDefaultButton(t == ButtonType.NO);
        }

        // center alert on parent window
        alert.initOwner(settingsButton.getScene().getWindow());
        Optional<ButtonType> value = alert.showAndWait();

        if (value.isEmpty() || value.get() != ButtonType.YES) {
            return;
        }

        if (Locale.getDefault().equals(new Locale("es", "ES"))) {
            Locale.setDefault(new Locale("gl", "ES"));
        } else {
            Locale.setDefault(new Locale("es", "ES"));
        }
        i18n.update();
    }

    @FXML
    public void changeButton(ActionEvent event) throws IOException {
        Button clickedButton = (Button) event.getSource();
        if (clickedButton.equals(currentMenuButton)) {
            return;
        }
        clickedButton.getStyleClass().add("selected");
        clickedButton.getStyleClass().remove("hoverButton");
        currentMenuButton.getStyleClass().remove("selected");
        currentMenuButton.getStyleClass().add("hoverButton");
        currentMenuButton = clickedButton;

        String fxmlFile =
                currentMenuButton.getId().split("list")[1].split("Button")[0].toLowerCase();
        FXMLLoader fxmlLoader =
                new FXMLLoader(
                        MainWindow.class.getResource(
                                String.format(
                                        "/io/github/kevinpita/comicstore/view/%s-list.fxml",
                                        fxmlFile)),
                        i18n.getResourceBundle());
        listParentPane.setCenter(fxmlLoader.load());
    }
}
