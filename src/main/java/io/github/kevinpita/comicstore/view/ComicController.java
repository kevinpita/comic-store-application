/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class ComicController {

    @FXML private AnchorPane comicPane;
    @FXML private Label comicTtle;
    @FXML private ImageView comicImage;

    private Image image;
    private String title;

    public void setImage(String imageUrl) {
        Image c = new Image(imageUrl);
        try {
            throw c.getException();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @FXML
    public void initialize() {
        comicImage.setImage(image);
        System.out.println(image.getHeight());

        comicTtle.setText(title);
        Tooltip.install(comicPane, new Tooltip(title));
    }
}
