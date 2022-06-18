/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.view;

import io.github.kevinpita.comicstore.model.AuthorDto;
import io.github.kevinpita.comicstore.model.CollectionDto;
import io.github.kevinpita.comicstore.service.AuthorService;
import io.github.kevinpita.comicstore.service.CollectionService;
import io.github.kevinpita.comicstore.util.i18n;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class ReportListController {
    @javafx.fxml.FXML private ComboBox<CollectionDto> comboReportComicCollection;
    @javafx.fxml.FXML private Button btnReportCollection;
    @javafx.fxml.FXML private Button btnReportComicCollection;
    @javafx.fxml.FXML private ComboBox<AuthorDto> comboReportComicAuthor;
    @javafx.fxml.FXML private Button btnReportComicAuthor;
    @javafx.fxml.FXML private Button btnReportComic;

    @FXML
    private void initialize() {
        setStringBindings();

        // get collection observable list
        List<CollectionDto> collections = CollectionService.getInstance().getCollections();
        ObservableList<CollectionDto> collectionObservableList =
                FXCollections.observableArrayList(collections);

        // get author observable list
        List<AuthorDto> authors = AuthorService.getInstance().getAuthors();
        ObservableList<AuthorDto> authorObservableList = FXCollections.observableArrayList(authors);

        // set combo box items
        comboReportComicCollection.setItems(collectionObservableList);
        comboReportComicAuthor.setItems(authorObservableList);

        // if combo box is not empty, set first item as default
        // if combo is empty, disable combo
        if (comboReportComicCollection.getItems().size() > 0) {
            comboReportComicCollection.getSelectionModel().select(0);
        } else {
            btnReportComicCollection.setDisable(true);
            btnReportComicCollection.setDisable(true);
        }

        if (comboReportComicAuthor.getItems().size() > 0) {
            comboReportComicAuthor.getSelectionModel().select(0);
        } else {
            btnReportComicAuthor.setDisable(true);
            comboReportComicAuthor.setDisable(true);
        }
    }

    private void setStringBindings() {
        btnReportComic.textProperty().bind(i18n.getStringBinding("comicReport"));
        btnReportCollection.textProperty().bind(i18n.getStringBinding("collectionReport"));
        btnReportComicAuthor.textProperty().bind(i18n.getStringBinding("comicReportAuthor"));
        btnReportComicCollection
                .textProperty()
                .bind(i18n.getStringBinding("comicReportCollection"));
    }
}
