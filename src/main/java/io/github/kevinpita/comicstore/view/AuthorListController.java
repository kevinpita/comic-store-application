/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.view;

import io.github.kevinpita.comicstore.model.AuthorTable;
import io.github.kevinpita.comicstore.service.AuthorService;
import io.github.kevinpita.comicstore.util.i18n;
import java.util.function.Predicate;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class AuthorListController {
    @FXML private TableColumn authorNameTableColumn;
    @FXML private TableColumn comicCountTableColumn;
    @FXML private TableColumn authorLastNameTableColumn;
    @FXML private TableView table;

    private Predicate<AuthorTable> createPredicate(String searchText) {
        return author -> {
            if (searchText == null || searchText.isEmpty()) return true;
            return (author.getName() + " " + author.getLastName())
                    .toLowerCase()
                    .contains(searchText.toLowerCase());
        };
    }

    public void initialize() {

        authorNameTableColumn.textProperty().bind(i18n.getStringBinding("authorName"));
        authorLastNameTableColumn.textProperty().bind(i18n.getStringBinding("authorLastName"));
        comicCountTableColumn.textProperty().bind(i18n.getStringBinding("authorComicCount"));

        authorNameTableColumn.setCellValueFactory(
                new PropertyValueFactory<AuthorTable, String>("name"));
        authorLastNameTableColumn.setCellValueFactory(
                new PropertyValueFactory<AuthorTable, String>("lastName"));
        comicCountTableColumn.setCellValueFactory(
                new PropertyValueFactory<AuthorTable, Integer>("createdComics"));

        ObservableList<AuthorTable> authors = AuthorService.getInstance().getAuthorsTable();

        Scene scene = MainWindow.mainScene;
        FilteredList<AuthorTable> filteredData = new FilteredList<>(authors);
        TextField textField = (TextField) scene.lookup("#searchBar");
        textField
                .textProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            filteredData.setPredicate(createPredicate(newValue));
                        });
        SortedList<AuthorTable> sortedData = new SortedList<>(filteredData);
        table.setItems(sortedData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());

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
        MainController.openAuthorWindow(rowData.getDto(), table);
    }
}
