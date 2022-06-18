/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.service;

import io.github.kevinpita.comicstore.model.AuthorComicDto;
import io.github.kevinpita.comicstore.model.table.AuthorComicTable;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AuthorComicService {
    public static ObservableList<AuthorComicTable> getAuthorComicTableList(
            List<AuthorComicDto> comics) {
        ObservableList<AuthorComicTable> comicTableList = FXCollections.observableArrayList();
        comics.forEach(
                authorComic ->
                        comicTableList.add(
                                new AuthorComicTable(
                                        authorComic.getId(),
                                        authorComic.getRole(),
                                        authorComic.getCreator())));
        return comicTableList;
    }
}
