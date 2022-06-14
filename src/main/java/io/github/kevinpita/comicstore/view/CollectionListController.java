/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.view;

import io.github.kevinpita.comicstore.model.CollectionDto;
import io.github.kevinpita.comicstore.service.CollectionService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

@Slf4j
public class CollectionListController {
    @FXML
    private FlowPane collectionFlowPane;
    private Map<Node, CollectionController> nodeControllerMap = new HashMap<>();

    private Predicate<Node> createPredicate(String searchText) {
        return collection -> {
            CollectionController controller = nodeControllerMap.get(collection);
            if (searchText == null || searchText.isEmpty()) return true;
            return controller.getTitle().toLowerCase().contains(searchText.toLowerCase());
        };
    }

    public void initialize() {
        List<CollectionDto> collections = CollectionService.getInstance().getCollections();

        List<Node> collectionList = new ArrayList<>();
        // for each comic create an instance
        collections.forEach(
                collection -> {
                    FXMLLoader loader =
                            new FXMLLoader(
                                    getClass()
                                            .getResource(
                                                    "/io/github/kevinpita/comicstore/view/collection.fxml"));
                    CollectionController controller = new CollectionController();
                    loader.setController(controller);
                    controller.setImage(collection.getImageUrl());
                    controller.setTitle(collection.getName());
                    try {
                        Node node = loader.load();
                        collectionList.add(node);
                        nodeControllerMap.put(node, controller);
                    } catch (Exception e) {
                        log.error(ExceptionUtils.getStackTrace(e));
                    }
                });
        Scene scene = MainWindow.mainScene;
        FilteredList<Node> filteredData = new FilteredList<>(FXCollections.observableArrayList(collectionList));
        TextField textField = (TextField) scene.lookup("#searchBar");
        textField.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    filteredData.setPredicate(createPredicate(newValue));
                });
        Bindings.bindContent(collectionFlowPane.getChildren(), filteredData);
    }
}
