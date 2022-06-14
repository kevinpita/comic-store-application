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

public class CollectionController {

    @FXML private AnchorPane collectionPane;
    @FXML private Label collectionTitle;
    @FXML private Rectangle collectionImage;

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

    public String getTitle() {
        return title;
    }

    @FXML
    public void initialize() {
        collectionImage.setFill(new ImagePattern(image, 0, 0, 1, 1, true));
        collectionTitle.setText(title);

        Tooltip tooltip = new Tooltip(title);
        tooltip.setFont(new Font(16));
        Tooltip.install(collectionPane, tooltip);
    }
}
