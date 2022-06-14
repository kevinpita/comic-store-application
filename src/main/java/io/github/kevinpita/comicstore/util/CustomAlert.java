/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.util;

import javafx.scene.control.Alert;

public class CustomAlert {
    public static void showAlert(String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(i18n.getString("errorTitle"));
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    public static void showAlert(String headerText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(i18n.getString("errorTitle"));
        alert.setHeaderText(headerText);
        alert.setContentText("");
        alert.showAndWait();
    }

    public static void showConnectingAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(i18n.getString("errorTitle"));
        alert.setHeaderText(i18n.getString("connectingError"));
        alert.setContentText("");
        alert.showAndWait();
    }

    public static void showInfo(String headerText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(i18n.getString("infoTitle"));
        alert.setHeaderText(headerText);
        alert.setContentText("");
        alert.showAndWait();
    }
}
