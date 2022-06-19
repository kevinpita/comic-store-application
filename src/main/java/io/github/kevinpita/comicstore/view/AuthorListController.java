/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.view;

import io.github.kevinpita.comicstore.model.table.AuthorTable;
import io.github.kevinpita.comicstore.service.AuthorService;
import io.github.kevinpita.comicstore.util.i18n;
import java.util.function.Predicate;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

public class AuthorListController {
    @FXML private TableColumn<AuthorTable, String> authorNameTableColumn;
    @FXML private TableColumn<AuthorTable, Integer> comicCountTableColumn;
    @FXML private TableColumn<AuthorTable, String> authorLastNameTableColumn;
    @FXML private TableView<AuthorTable> table;
    @FXML private BorderPane parentPane;

    @FXML
    private void initialize() {
        Platform.runLater(() -> parentPane.requestFocus());

        bindTableColumns();

        ObservableList<AuthorTable> authors = AuthorService.getInstance().getAuthorsTable();
        FilteredList<AuthorTable> filteredData = new FilteredList<>(authors);
        SortedList<AuthorTable> sortedData = new SortedList<>(filteredData);

        bindSearchBar(filteredData);

        table.setItems(sortedData);
        // enable table sort
        sortedData.comparatorProperty().bind(table.comparatorProperty());

        editAuthorWithDoubleClick();
    }

    private void bindTableColumns() {
        authorNameTableColumn.textProperty().bind(i18n.getStringBinding("authorName"));
        authorLastNameTableColumn.textProperty().bind(i18n.getStringBinding("authorLastName"));
        comicCountTableColumn.textProperty().bind(i18n.getStringBinding("authorComicCount"));

        authorNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        authorLastNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        comicCountTableColumn.setCellValueFactory(new PropertyValueFactory<>("createdComics"));
    }

    private void bindSearchBar(FilteredList<AuthorTable> filteredData) {
        MainController.getSearchBar()
                .textProperty()
                .addListener(
                        (ignored, oldValue, newValue) -> {
                            filteredData.setPredicate(createPredicate(newValue));
                        });
    }

    private void editAuthorWithDoubleClick() {
        table.setRowFactory(
                tv -> {
                    TableRow<AuthorTable> row = new TableRow<>();
                    row.setOnMouseClicked(
                            event -> {
                                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                                    AuthorTable rowData = row.getItem();
                                    editAuthor(rowData);
                                }
                            });
                    return row;
                });
    }

    private void editAuthor(AuthorTable rowData) {
        MainController.openAuthorWindow(rowData.getDto());
    }

    private Predicate<AuthorTable> createPredicate(String searchText) {
        return author -> {
            if (searchText == null || searchText.isEmpty()) return true;
            return (author.getName() + " " + author.getLastName())
                    .toLowerCase()
                    .contains(searchText.toLowerCase());
        };
    }
}
