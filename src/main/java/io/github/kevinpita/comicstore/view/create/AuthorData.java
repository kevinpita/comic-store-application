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
import lombok.Setter;

public class AuthorData {
    @FXML private Button saveButton;
    @FXML private Button removeButton;
    @Setter private AuthorDto authorDto;
    @FXML private TextField inputAuthorName;
    @FXML private TextField inputAuthorLastName;

    @FXML private AnchorPane parentAnchorPane;

    @FXML
    private void cancel() {
        saveButton.getScene().getWindow().hide();
    }

    @FXML
    private void save() {
        boolean error = false;
        String name = inputAuthorName.getText().strip();
        String lastName = inputAuthorLastName.getText().strip();

        error = checkFields(name, lastName);

        if (error) {
            CustomAlert.showAlert(
                    i18n.getString("formError"), i18n.getString("authorFormErrorMessage"));
            return;
        }

        if (checkSameObject(name, lastName)) return;

        boolean created;
        if (authorDto == null) {
            created = AuthorService.createAuthor(name, lastName);
        } else {
            created = AuthorService.updateAuthor(authorDto.getId(), name, lastName);
        }

        CustomAlert.showInfo(
                i18n.getString(created ? "newAuthorAlert" : "authorFormCreateErrorMessage"));

        saveButton.getScene().getWindow().hide();
    }

    @FXML
    private void delete() {
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

    private boolean checkSameObject(String name, String lastName) {
        if (name.equals(authorDto.getName()) && lastName.equals(authorDto.getLastName())) {
            saveButton.getScene().getWindow().hide();
            return true;
        }
        return false;
    }

    private boolean checkFields(String name, String lastName) {
        Pattern pattern = Pattern.compile("^([a-zA-Z(\\s)?]+){1,50}$", Pattern.CASE_INSENSITIVE);
        boolean result = false;
        if (!pattern.matcher(name).matches()) {
            inputAuthorName.getStyleClass().add("errorField");
            result = true;
        } else {
            inputAuthorName.getStyleClass().remove("errorField");
        }

        if (!pattern.matcher(lastName).matches()) {
            inputAuthorLastName.getStyleClass().add("errorField");
            result = true;
        } else {
            inputAuthorLastName.getStyleClass().remove("errorField");
        }
        return result;
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
