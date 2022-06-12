/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.view;

import io.github.kevinpita.comicstore.utils.i18n;
import java.io.IOException;
import java.util.Objects;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainWindow extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        // Load fxml file with resource bundle
        FXMLLoader fxmlLoader =
                new FXMLLoader(
                        MainWindow.class.getResource(
                                "/io/github/kevinpita/comicstore/view/main-view.fxml"),
                        i18n.getResourceBundle());

        Scene scene = new Scene(fxmlLoader.load());

        // set app icon
        stage.getIcons()
                .add(
                        new Image(
                                Objects.requireNonNull(
                                        MainWindow.class.getResourceAsStream(
                                                "/io/github/kevinpita/comicstore/view/images/icon.png"))));

        // set minimum size
        stage.setMinHeight(720);
        stage.setMinWidth(1150);

        // set title using resource bundle
        stage.titleProperty().bind(i18n.getStringBinding("title"));

        // attach loaded FXML to the stage
        stage.setScene(scene);
        // show stage
        stage.show();

        // Center the stage on the middle of the screen
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primaryScreenBounds.getWidth() - stage.getMinWidth()) / 2);
        stage.setY((primaryScreenBounds.getHeight() - stage.getMinHeight()) / 2);
    }
}
