/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.util;

import java.io.File;
import java.nio.file.Path;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

public class ImageUtil {
    public static Path selectImage(Circle circlePicture, Path imagePath) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(i18n.getString("selectPictureTitle"));
        fileChooser
                .getExtensionFilters()
                .add(
                        new FileChooser.ExtensionFilter(
                                i18n.getString("selectPictureFilter"), "*.png", "*.jpg"));
        File selectedFile = fileChooser.showOpenDialog(circlePicture.getScene().getWindow());
        if (selectedFile == null) {
            return imagePath;
        }

        Image image = new Image(selectedFile.toPath().toUri().toString());
        if (!image.isError()) {
            imagePath = selectedFile.toPath();
        }
        if (imagePath != null) {
            circlePicture.setFill(new ImagePattern(image));
        }

        return imagePath;
    }
}
