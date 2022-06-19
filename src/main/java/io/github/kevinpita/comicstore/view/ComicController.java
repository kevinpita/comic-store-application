/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.view;

import io.github.kevinpita.comicstore.model.ComicDto;
import java.util.Objects;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import lombok.Getter;
import lombok.Setter;

public class ComicController {

    @FXML private AnchorPane comicPane;
    @FXML private Label comicTitle;
    @FXML private Rectangle comicImage;

    private Image image;
    @Getter @Setter private String title;

    @Setter @Getter private ComicDto comic;
    @FXML private AnchorPane parentPane;

    @FXML
    private void initialize() {
        Platform.runLater(() -> parentPane.requestFocus());
    }

    public void setImage(String imageUrl) {
        image = new Image(imageUrl);
        if (image.isError()) {
            image =
                    new Image(
                            Objects.requireNonNull(
                                    this.getClass()
                                            .getResourceAsStream(
                                                    "/io/github/kevinpita/comicstore/view/images/nopicture.png")));
        }
    }

    public void lateInit() {
        comicImage.setFill(new ImagePattern(image));
        comicTitle.setText(title);

        Tooltip tooltip = new Tooltip(title);
        tooltip.setFont(new Font(16));
        Tooltip.install(comicPane, tooltip);
    }

    @FXML
    private void openComicEditWindow() {
        MainController.openComicWindow(this.comic);
    }
}
