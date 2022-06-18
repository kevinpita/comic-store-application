/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.view.create;

import io.github.kevinpita.comicstore.model.AuthorDto;
import io.github.kevinpita.comicstore.model.table.AuthorComicTable;
import io.github.kevinpita.comicstore.service.AuthorService;
import io.github.kevinpita.comicstore.util.CustomAlert;
import io.github.kevinpita.comicstore.util.i18n;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lombok.Setter;

public class AuthorComicData {
    @FXML private ComboBox<AuthorDto> inputAuthorName;
    @FXML private AnchorPane parentAnchorPane;
    @FXML private TextField inputAuthorRole;
    @FXML private Button saveButton;
    @Setter private AuthorComicTable authorComicTable;
    @Setter private List<String> roles;
    @Setter private TableView<AuthorComicTable> table;

    public void lateInit() {
        ObservableList<AuthorDto> authors = AuthorService.getInstance().getAuthors();

        if (authors.size() == 0) {
            inputAuthorName.setDisable(true);
            saveButton.setDisable(true);
            return;
        }

        inputAuthorName.setItems(authors);

        if (authorComicTable == null) {
            inputAuthorName.setValue(authors.get(0));
            return;
        }

        inputAuthorRole.setText(authorComicTable.getRole());
        inputAuthorName.setValue(authorComicTable.getCreator());
    }

    @FXML
    private void save(ActionEvent actionEvent) {
        String authorRole = inputAuthorRole.getText().strip();
        if (authorRole.isEmpty() || authorRole.length() > 50) {
            inputAuthorRole.getStyleClass().add("errorField");
            CustomAlert.showAlert(
                    i18n.getString("formError"),
                    i18n.getString("authorComicFormErrorMessage"),
                    parentAnchorPane.getScene().getWindow());
            return;
        } else {
            inputAuthorRole.getStyleClass().remove("errorField");
        }

        if (roles.contains(authorRole)
                && inputAuthorName.getSelectionModel().equals(authorComicTable.getCreator())) {
            inputAuthorRole.getStyleClass().add("errorField");
            CustomAlert.showAlert(
                    i18n.getString("formError"),
                    i18n.getString("authorComicFormErrorMessageContains"),
                    parentAnchorPane.getScene().getWindow());
            return;
        }

        if (authorComicTable == null) {
            authorComicTable = new AuthorComicTable(0, authorRole, inputAuthorName.getValue());
            table.getItems().add(authorComicTable);
        } else {
            authorComicTable.setCreator(inputAuthorName.getValue());
            authorComicTable.setRole(inputAuthorRole.getText());
        }
        CustomAlert.showInfo(
                i18n.getString("newAuthorAlert"), parentAnchorPane.getScene().getWindow());
        inputAuthorRole.getScene().getWindow().hide();
    }
}
