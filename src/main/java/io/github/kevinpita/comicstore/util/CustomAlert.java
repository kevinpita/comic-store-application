/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.util;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.Window;

public class CustomAlert {
    public static void showAlert(String headerText, String contentText, Window window) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(i18n.getString("errorTitle"));
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        if (window != null) {
            alert.initOwner(window);
        }
        alert.showAndWait();
    }

    public static void showAlert(String headerText, Window window) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(i18n.getString("errorTitle"));
        alert.setHeaderText(headerText);
        alert.setContentText("");
        if (window != null) {
            alert.initOwner(window);
        }
        alert.showAndWait();
    }

    public static void showConnectingAlert(Window window) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(i18n.getString("errorTitle"));
        alert.setHeaderText(i18n.getString("connectingError"));
        alert.setContentText("");
        if (window != null) {
            alert.initOwner(window);
        }
        alert.showAndWait();
    }

    public static void showInfo(String headerText, Window window) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(i18n.getString("infoTitle"));
        alert.setHeaderText(headerText);
        alert.setContentText("");
        if (window != null) {
            alert.initOwner(window);
        }
        alert.showAndWait();
    }

    public static boolean askDelete(Window window) {
        Alert alert =
                new Alert(
                        Alert.AlertType.CONFIRMATION,
                        i18n.getString("confirmDelete"),
                        ButtonType.YES,
                        ButtonType.NO);

        alert.setTitle(i18n.getString("deleteTitle"));

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
        alert.initOwner(window);
        Optional<ButtonType> value = alert.showAndWait();

        // if user didn't click yes, don't change language
        if (value.isEmpty() || value.get() != ButtonType.YES) {
            return false;
        }
        return true;
    }
}
