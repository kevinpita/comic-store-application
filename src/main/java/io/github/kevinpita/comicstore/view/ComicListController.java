/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.view;

import io.github.kevinpita.comicstore.model.ComicDto;
import io.github.kevinpita.comicstore.service.ComicService;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.FlowPane;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

@Slf4j
public class ComicListController {
    @FXML private FlowPane comicFlowPane;

    @FXML
    private void initialize() {
        // get all comics from filled list
        List<ComicDto> comics = ComicService.getInstance().getComics();

        // for each comic create an instance
        for (ComicDto comic : comics) {
            FXMLLoader loader = MainController.getFxmlLoader("comic");
            Parent root;
            try {
                root = loader.load();
            } catch (Exception e) {
                log.error(ExceptionUtils.getStackTrace(e));
                continue;
            }

            ComicController comicController = loader.getController();

            comicController.setComic(comic);
            comicController.setImage(comic.getImageUrl());
            comicController.setTitle(comic.getFullTitle());

            comicController.lateInit();

            comicFlowPane.getChildren().add(root);
        }
    }
}
