/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.service;

import io.github.kevinpita.comicstore.model.ComicCopyDto;
import io.github.kevinpita.comicstore.model.table.ComicCopyTable;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ComicCopyService {
    public static ObservableList<ComicCopyTable> getComicCopyTableList(List<ComicCopyDto> comics) {
        ObservableList<ComicCopyTable> comicTableList = FXCollections.observableArrayList();
        comics.forEach(
                comic ->
                        comicTableList.add(
                                new ComicCopyTable(
                                        comic.getCover(),
                                        comic.getState(),
                                        comic.getPrice(),
                                        comic.getPurchaseDate())));
        return comicTableList;
    }
}
