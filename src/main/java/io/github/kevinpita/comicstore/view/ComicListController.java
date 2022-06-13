/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.view;

import io.github.kevinpita.comicstore.model.ComicDto;
import io.github.kevinpita.comicstore.service.ComicService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import java.io.IOException;
import java.util.List;

public class ComicListController {
    @FXML private FlowPane comicFlowPane;

    public void initialize() {
        ComicService.getInstance().fillComics();
        List<ComicDto> comics = ComicService.getInstance().getComics();

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
                        System.out.println("Loading comic: " + comic.getFullTitle());
                        comicFlowPane.getChildren().add(loader.load());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
