/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.view.create;

import io.github.kevinpita.comicstore.model.AuthorDto;
import io.github.kevinpita.comicstore.service.AuthorService;
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
        boolean newAuthor =
                AuthorService.createAuthor(
                        inputAuthorName.getText(), inputAuthorLastName.getText());
        if (newAuthor) {
            saveButton.getScene().getWindow().hide();
        }
    }

    @FXML
    void delete() {
        saveButton.getScene().getWindow().hide();
    }
}
