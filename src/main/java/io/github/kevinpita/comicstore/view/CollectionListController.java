/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.view;

import io.github.kevinpita.comicstore.service.CollectionService;
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
public class CollectionListController {
    @FXML private FlowPane collectionFlowPane;
    @FXML private BorderPane parentPane;

    // check if search text matches the collection name
    private Predicate<Node> createPredicate(String searchText) {
        return collection -> {
            CollectionController controller =
                    CollectionService.getInstance().getNodeControllerMap().get(collection);
            if (searchText == null || searchText.isEmpty()) return true;
            try {
                return controller.getTitle().toLowerCase().contains(searchText.toLowerCase());
            } catch (Exception e) {
                return false;
            }
        };
    }

    @FXML
    private void initialize() {
        Platform.runLater(() -> parentPane.requestFocus());

        // get observable list of collections
        ObservableList<Node> collectionList =
                CollectionService.getInstance().getCollectionsAsNodes();
        // create filtered list of collections
        FilteredList<Node> filteredData =
                new FilteredList<>(FXCollections.observableArrayList(collectionList));

        // bind search bar to filtered list
        MainController.getSearchBar()
                .textProperty()
                .addListener(
                        (ignored, oldValue, newValue) -> {
                            filteredData.setPredicate(createPredicate(newValue));
                        });
        Bindings.bindContent(collectionFlowPane.getChildren(), filteredData);
    }
}
