/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.view.create;

import io.github.kevinpita.comicstore.model.AuthorDto;
import io.github.kevinpita.comicstore.service.AuthorService;
import io.github.kevinpita.comicstore.util.CustomAlert;
import io.github.kevinpita.comicstore.util.i18n;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.util.regex.Pattern;

public class AuthorData {
    @FXML private Button saveButton;
    @FXML private Button removeButton;
    AuthorDto authorDto;
    @FXML private TextField inputAuthorName;
    @FXML private TextField inputAuthorLastName;

    @FXML private AnchorPane parentAnchorPane;

    public AuthorData(AuthorDto authorDto) {
        this.authorDto = authorDto;
    }

    public void initialize() {
        if (authorDto == null) {
            return;
        }

        inputAuthorName.setText(authorDto.getName());
        inputAuthorLastName.setText(authorDto.getLastName());

        Platform.runLater(() -> parentAnchorPane.requestFocus());
        removeButton.setDisable(false);
    }

    @FXML
    public void cancel() {
        saveButton.getScene().getWindow().hide();
    }

    @FXML
    void save() {
        boolean error = false;
        String name = inputAuthorName.getText().strip();
        String lastName = inputAuthorLastName.getText().strip();
        Pattern pattern = Pattern.compile("^([a-zA-Z]+){1,50}$", Pattern.CASE_INSENSITIVE);

        if (!pattern.matcher(name).matches()) {
            inputAuthorName.getStyleClass().add("errorField");
            error = true;
        } else {
            inputAuthorName.getStyleClass().remove("errorField");
        }

        if (!pattern.matcher(lastName).matches()) {
            inputAuthorLastName.getStyleClass().add("errorField");
            error = true;
        } else {
            inputAuthorLastName.getStyleClass().remove("errorField");
        }

        if (error) {
            CustomAlert.showAlert(i18n.getString("formError"), i18n.getString("authorFormErrorMessage"));
            return;
        }

        boolean newAuthor =
                AuthorService.createAuthor(
                        inputAuthorName.getText(), inputAuthorLastName.getText());
        if (newAuthor) {
            CustomAlert.showInfo(i18n.getString("newAuthorAlert"));
            saveButton.getScene().getWindow().hide();
        }
    }

    @FXML
    void delete() {
        saveButton.getScene().getWindow().hide();
    }
}
