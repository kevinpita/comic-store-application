/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.view;

import io.github.kevinpita.comicstore.service.CollectionService;
import java.util.function.Predicate;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CollectionListController {
    @FXML private FlowPane collectionFlowPane;

    private Predicate<Node> createPredicate(String searchText) {
        return collection -> {
            CollectionController controller =
                    CollectionService.getInstance().getNodeMap().get(collection);
            if (searchText == null || searchText.isEmpty()) return true;
            try {
                return controller.getTitle().toLowerCase().contains(searchText.toLowerCase());
            } catch (Exception e) {
                return false;
            }
        };
    }

    public void initialize() {
        ObservableList<Node> collectionList =
                CollectionService.getInstance().getCollectionsAsNodes();
        Scene scene = MainWindow.mainScene;
        FilteredList<Node> filteredData =
                new FilteredList<>(FXCollections.observableArrayList(collectionList));
        TextField textField = (TextField) scene.lookup("#searchBar");
        textField
                .textProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            filteredData.setPredicate(createPredicate(newValue));
                        });
        Bindings.bindContent(collectionFlowPane.getChildren(), filteredData);
    }
}
