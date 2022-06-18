/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.service;

import io.github.kevinpita.comicstore.model.ComicAuthorDto;
import io.github.kevinpita.comicstore.model.table.ComicAuthorTable;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AuthorComicService {
    public static ObservableList<ComicAuthorTable> getAuthorComicTableList(
            List<ComicAuthorDto> comics) {
        ObservableList<ComicAuthorTable> comicTableList = FXCollections.observableArrayList();
        comics.forEach(
                comic ->
                        comicTableList.add(
                                new ComicAuthorTable(
                                        comic.getRole(), comic.getCreator().toString())));
        return comicTableList;
    }
}
