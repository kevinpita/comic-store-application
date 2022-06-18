/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.view.create;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class ComicCopyData {
    @javafx.fxml.FXML private TextField inputAuthorRole1;
    @javafx.fxml.FXML private TextField inputCreateState;
    @javafx.fxml.FXML private TextField inputCreateCover;
    @javafx.fxml.FXML private AnchorPane parentAnchorPane;
    @javafx.fxml.FXML private DatePicker datePicker;
    @javafx.fxml.FXML private Button saveButton;
    @javafx.fxml.FXML private TextField inputCreatePrice;

    public void lateInit() {}

    @javafx.fxml.FXML
    public void save(ActionEvent actionEvent) {}
}
