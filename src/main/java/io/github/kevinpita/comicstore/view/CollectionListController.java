/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.view;

import io.github.kevinpita.comicstore.model.CollectionDto;
import io.github.kevinpita.comicstore.model.ComicDto;
import io.github.kevinpita.comicstore.service.CollectionService;
import io.github.kevinpita.comicstore.service.ComicService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.IOException;
import java.rmi.server.ExportException;
import java.util.List;

@Slf4j
public class CollectionListController {
    @FXML private FlowPane collectionFlowPane;

    public void initialize() {
        // fill comic list on window initialization
        CollectionService.getInstance().fillCollections();
        // get all comics from filled list
        List<CollectionDto> collections = CollectionService.getInstance().getCollections();

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
                        collectionFlowPane.getChildren().add(loader.load());
                    } catch (Exception e) {
                        log.error(ExceptionUtils.getStackTrace(e));
                    }
                });
    }
}
