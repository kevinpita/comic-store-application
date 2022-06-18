/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.util;

import javafx.scene.control.Alert;
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
}
