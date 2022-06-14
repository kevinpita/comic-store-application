/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.view;

import io.github.kevinpita.comicstore.model.ComicDto;
import io.github.kevinpita.comicstore.service.ComicService;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

@Slf4j
public class ComicListController {
    @FXML private FlowPane comicFlowPane;

    public void initialize() {
        // get all comics from filled list
        List<ComicDto> comics = ComicService.getInstance().getComics();

        // for each comic create an instance
        comics.forEach(
                comic -> {
                    FXMLLoader loader =
                            new FXMLLoader(
                                    getClass()
                                            .getResource(
                                                    "/io/github/kevinpita/comicstore/view/comic.fxml"));
                    ComicController controller = new ComicController();
                    loader.setController(controller);
                    controller.setImage(comic.getImageUrl());
                    controller.setTitle(comic.getFullTitle());
                    try {
                        comicFlowPane.getChildren().add(loader.load());
                    } catch (Exception e) {
                        log.error(ExceptionUtils.getStackTrace(e));
                    }
                });
    }
}
