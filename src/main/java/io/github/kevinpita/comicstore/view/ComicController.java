/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;

public class ComicController {

    @FXML private AnchorPane comicPane;
    @FXML private Label comicTtle;
    @FXML private ImageView comicImage;

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
        comicImage.setImage(image);
        System.out.println(image.getHeight());

        comicTtle.setText(title);
        Tooltip tooltip = new Tooltip(title);
        tooltip.setFont(new Font(16));
        Tooltip.install(comicPane, tooltip);
    }
}
