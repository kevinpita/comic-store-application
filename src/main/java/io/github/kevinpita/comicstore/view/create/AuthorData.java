/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.view.create;

import io.github.kevinpita.comicstore.model.AuthorDto;
import io.github.kevinpita.comicstore.service.AuthorService;
import io.github.kevinpita.comicstore.util.CustomAlert;
import io.github.kevinpita.comicstore.util.i18n;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class AuthorData {
    @FXML private Button saveButton;
    @FXML private Button removeButton;
    AuthorDto authorDto;
    @FXML private TextField inputAuthorName;
    @FXML private TextField inputAuthorLastName;

    @FXML private AnchorPane parentAnchorPane;

    public void setAuthor(AuthorDto authorDto) {
        this.authorDto = authorDto;
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
        Pattern pattern = Pattern.compile("^([a-zA-Z(\\s)?]+){1,50}$", Pattern.CASE_INSENSITIVE);

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
            CustomAlert.showAlert(
                    i18n.getString("formError"), i18n.getString("authorFormErrorMessage"));
            return;
        }

        boolean created;
        if (authorDto == null) {
            created = AuthorService.createAuthor(name, lastName);
        } else {
            if (name.equals(authorDto.getName()) && lastName.equals(authorDto.getLastName())) {
                saveButton.getScene().getWindow().hide();
                return;
            }
            created = AuthorService.updateAuthor(authorDto.getId(), name, lastName);
        }
        if (created) {
            CustomAlert.showInfo(i18n.getString("newAuthorAlert"));
        } else {
            CustomAlert.showAlert(i18n.getString("authorFormCreateErrorMessage"));
        }

        saveButton.getScene().getWindow().hide();
    }

    @FXML
    void delete() {
        if (authorDto == null) {
            return;
        }
        boolean deleteResult = AuthorService.deleteAuthor(authorDto.getId());
        if (deleteResult) {
            CustomAlert.showInfo(i18n.getString("deleteAuthorAlert"));
            saveButton.getScene().getWindow().hide();
            return;
        }
        CustomAlert.showAlert(i18n.getString("authorFormDeleteErrorMessage"));
    }

    public void lateInit() {
        if (authorDto == null) {
            Platform.runLater(() -> parentAnchorPane.requestFocus());
            return;
        }

        String lastName = authorDto.getLastName();
        lastName = lastName.split(Pattern.quote("("))[0];

        inputAuthorName.setText(authorDto.getName());
        inputAuthorLastName.setText(lastName);

        Platform.runLater(() -> parentAnchorPane.requestFocus());
        removeButton.setDisable(false);
    }
}
