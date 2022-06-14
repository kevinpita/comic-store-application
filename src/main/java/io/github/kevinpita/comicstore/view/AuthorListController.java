/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.view;

import io.github.kevinpita.comicstore.model.AuthorTable;
import io.github.kevinpita.comicstore.model.CreatorDto;
import io.github.kevinpita.comicstore.service.AuthorService;
import io.github.kevinpita.comicstore.util.i18n;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
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

        List<CreatorDto> authors = AuthorService.getInstance().getAuthors();

        List<AuthorTable> authorsList = new ArrayList<>();
        authors.forEach(
                author -> {
                    SimpleStringProperty name = new SimpleStringProperty(author.getName());
                    SimpleStringProperty lastName = new SimpleStringProperty(author.getLastName());
                    SimpleIntegerProperty createdComicNumber =
                            new SimpleIntegerProperty(author.getCreatedComics());
                    AuthorTable authorTable =
                            AuthorTable.builder()
                                    .name(name)
                                    .lastName(lastName)
                                    .createdComics(createdComicNumber)
                                    .build();
                    authorsList.add(authorTable);
                });

        Scene scene = MainWindow.mainScene;
        FilteredList<AuthorTable> filteredData =
                new FilteredList<>(FXCollections.observableArrayList(authorsList));
        TextField textField = (TextField) scene.lookup("#searchBar");
        textField
                .textProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            filteredData.setPredicate(createPredicate(newValue));
                        });
        table.setItems(filteredData);
    }
}
