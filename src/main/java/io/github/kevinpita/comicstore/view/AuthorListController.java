/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.view;

import io.github.kevinpita.comicstore.model.AuthorTable;
import io.github.kevinpita.comicstore.model.CreatorDto;
import io.github.kevinpita.comicstore.service.AuthorService;
import io.github.kevinpita.comicstore.util.i18n;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class AuthorListController {
    @FXML
    private TableColumn authorNameTableColumn;
    @FXML
    private TableColumn comicCountTableColumn;
    @FXML
    private TableColumn authorLastNameTableColumn;
    @FXML
    private TableView table;

    public void initialize() {
        authorNameTableColumn.textProperty().bind(i18n.getStringBinding("authorName"));
        authorLastNameTableColumn.textProperty().bind(i18n.getStringBinding("authorLastName"));
        comicCountTableColumn.textProperty().bind(i18n.getStringBinding("authorComicCount"));

        authorNameTableColumn.setCellValueFactory(new PropertyValueFactory<AuthorTable, String>("name"));
        authorLastNameTableColumn.setCellValueFactory(new PropertyValueFactory<AuthorTable, String>("lastName"));
        comicCountTableColumn.setCellValueFactory(new PropertyValueFactory<AuthorTable, Integer>("createdComics"));


        AuthorService.getInstance().fillAuthors();
        List<CreatorDto> authors = AuthorService.getInstance().getAuthors();

        authors.forEach(
                author -> {
                    SimpleStringProperty name = new SimpleStringProperty(author.getName());
                    SimpleStringProperty lastName = new SimpleStringProperty(author.getLastName());
                    SimpleIntegerProperty createdComicNumber = new SimpleIntegerProperty(author.getCreatedComics());
                    AuthorTable authorTable = AuthorTable.builder()
                            .name(name)
                            .lastName(lastName)
                            .createdComics(createdComicNumber)
                            .build();
                    table.getItems().add(authorTable);
                });


    }
}
