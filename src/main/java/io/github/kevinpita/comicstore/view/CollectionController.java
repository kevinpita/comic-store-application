/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;

public class CollectionController {

    @FXML private AnchorPane collectionPane;
    @FXML private Label collectionTitle;
    @FXML private ImageView collectionImage;

    private Image image;
    private String title;

    public void setImage(String imageUrl) {
        image = new Image(imageUrl);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @FXML
    public void initialize() {
        collectionImage.setImage(image);
        collectionTitle.setText(title);

        Tooltip tooltip = new Tooltip(title);
        tooltip.setFont(new Font(16));
        Tooltip.install(collectionPane, tooltip);
    }
}
