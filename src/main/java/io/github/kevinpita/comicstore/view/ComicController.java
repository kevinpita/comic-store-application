/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class ComicController {

    @FXML private AnchorPane comicPane;
    @FXML private Label comicTtle;
    @FXML private ImageView comicImage;

    public void initialize() {
        Tooltip.install(comicPane, new Tooltip(comicTtle.getText()));
    }
}
