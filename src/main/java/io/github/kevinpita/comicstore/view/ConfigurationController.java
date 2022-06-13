/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.view;

import io.github.kevinpita.comicstore.configuration.Configuration;
import io.github.kevinpita.comicstore.configuration.UrlPath;
import io.github.kevinpita.comicstore.util.i18n;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

@Slf4j
public class ConfigurationController {
    @FXML private Button configSave;
    @FXML private TextField configServer;
    @FXML private TextField configPassword;

    public void initialize() {
        configServer.setText(Configuration.getApiUrl());
        configPassword.setText(Configuration.getAuthToken());
    }

    @FXML
    public boolean checkConfig() {
        boolean validationResult = validate();
        Alert alert;
        if (validationResult) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(i18n.getString("validServerTitle"));
            alert.setHeaderText(i18n.getString("validServerHeader"));
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(i18n.getString("invalidServerTitle"));
            alert.setHeaderText(i18n.getString("invalidServerHeader"));
            alert.setContentText(i18n.getString("invalidServerContent"));
        }
        // center the dialog on the parent window
        alert.initOwner(configSave.getScene().getWindow());
        alert.show();
        return validationResult;
    }

    @FXML
    public void cancelConfigEdit() {
        configPassword.getScene().getWindow().hide();
    }

    @FXML
    public void saveConfigEdit() {
        if (!checkConfig()) {
            return;
        }
        Configuration.setApiUrl(configServer.getText());
        Configuration.setAuthToken(configPassword.getText());
        Configuration.writeConfiguration();
        configPassword.getScene().getWindow().hide();
    }

    public boolean validate() {
        String server = configServer.getText();
        String password = configPassword.getText();

        if (server.isBlank()) {
            return false;
        }

        // remove trailing slash if any
        if (server.endsWith("/")) {
            server = server.substring(0, server.length() - 1);
        }

        return checkServer(server, password);
    }

    private boolean checkServer(String server, String password) {
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(URI.create(server + UrlPath.VALIDATION.getPath()))
                            .header("Authorization", password)
                            .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (isServerValid(response)) return true;
        } catch (Exception ignored) {
            log.error(ExceptionUtils.getStackTrace(ignored));
        }
        return false;
    }

    private boolean isServerValid(HttpResponse<String> response) {
        return response.statusCode() == 200
                && response.body().contains("\"message\":\"validated\"");
    }
}
