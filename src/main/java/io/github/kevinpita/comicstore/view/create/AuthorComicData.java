/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.view.create;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class AuthorComicData {
    @FXML private ComboBox<?> inputAuthorName;
    @FXML private AnchorPane parentAnchorPane;
    @FXML private TextField inputAuthorLastName;
    @FXML private Button removeButton;
    @FXML private Button saveButton;

    @FXML
    public void save(ActionEvent actionEvent) {}

    @FXML
    public void delete(ActionEvent actionEvent) {}
}
