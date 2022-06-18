/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.view;

import java.util.Objects;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class ComicController {

    @FXML private AnchorPane comicPane;
    @FXML private Label comicTitle;
    @FXML private Rectangle comicImage;

    private Image image;
    private String title;

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

    public void setTitle(String title) {
        this.title = title;
    }

    public void lateInit() {
        comicImage.setFill(new ImagePattern(image));
        comicTitle.setText(title);

        Tooltip tooltip = new Tooltip(title);
        tooltip.setFont(new Font(16));
        Tooltip.install(comicPane, tooltip);
    }
}
