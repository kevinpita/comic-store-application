/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.view;

import io.github.kevinpita.comicstore.service.ComicService;
import java.util.function.Predicate;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ComicListController {
    @FXML private FlowPane comicFlowPane;
    @FXML private BorderPane parentPane;

    private Predicate<Node> createPredicate(String searchText) {
        return collection -> {
            ComicController controller =
                    ComicService.getInstance().getNodeControllerMap().get(collection);
            if (searchText == null || searchText.isEmpty()) return true;
            try {
                return checkTitle(controller, searchText);
            } catch (Exception e) {
                return false;
            }
        };
    }

    private boolean checkTitle(ComicController comic, String search) {
        boolean containsTitle = comic.getTitle().toLowerCase().contains(search.toLowerCase());
        boolean containsCollection =
                comic.getComic()
                        .getCollection()
                        .getName()
                        .toLowerCase()
                        .contains(search.toLowerCase());

        return containsTitle || containsCollection;
    }

    @FXML
    private void initialize() {
        Platform.runLater(() -> parentPane.requestFocus());

        // get observable list of comics
        ObservableList<Node> comicList = ComicService.getInstance().getCollectionsAsNodes();
        // create filtered list of comics
        FilteredList<Node> filteredData =
                new FilteredList<>(FXCollections.observableArrayList(comicList));

        // bind search bar to filtered list
        MainController.getSearchBar()
                .textProperty()
                .addListener(
                        (ignored, oldValue, newValue) -> {
                            filteredData.setPredicate(createPredicate(newValue));
                        });
        Bindings.bindContent(comicFlowPane.getChildren(), filteredData);
    }
}
