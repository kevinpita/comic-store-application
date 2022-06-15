/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.view.create;

import io.github.kevinpita.comicstore.model.CollectionDto;
import io.github.kevinpita.comicstore.service.CollectionService;
import io.github.kevinpita.comicstore.util.CustomAlert;
import io.github.kevinpita.comicstore.util.i18n;
import io.github.kevinpita.comicstore.view.MainWindow;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CollectionData {
    @FXML private GridPane parentPane;
    @FXML private Circle circlePicture;
    @FXML private TextField inputCollectionName;
    @FXML private TextField inputCollectionPublisher;
    @FXML private TextArea txtAreaDescription;
    @FXML private Button removeButton;

    private Path imagePath;
    CollectionDto collectionDto;
    BorderPane reloadedPane;

    public CollectionData(CollectionDto collectionDto, BorderPane reloadedPane) {
        this.collectionDto = collectionDto;
        this.reloadedPane = reloadedPane;
    }

    public void initialize() {
        Image image;
        try {
            image = new Image(collectionDto.getImageUrl());
            if (image.isError()) {
                throw new Exception();
            }
        } catch (Exception e) {
            image =
                    new Image(
                            Objects.requireNonNull(
                                    this.getClass()
                                            .getResourceAsStream(
                                                    "/io/github/kevinpita/comicstore/view/images/collection.png")));
            ;
        }

        circlePicture.setFill(new ImagePattern(image));
        if (collectionDto == null) {
            Platform.runLater(() -> parentPane.requestFocus());
            return;
        }

        inputCollectionName.setText(collectionDto.getName());
        inputCollectionPublisher.setText(collectionDto.getPublisher());
        txtAreaDescription.setText(collectionDto.getDescription());

        Platform.runLater(() -> parentPane.requestFocus());
        removeButton.setDisable(false);
    }

    @FXML
    public void cancel() {
        circlePicture.getScene().getWindow().hide();
    }

    @FXML
    void save() {
        boolean error = false;
        String name = inputCollectionName.getText().strip();
        String publisher = inputCollectionPublisher.getText().strip();
        String description = txtAreaDescription.getText().strip();

        if (name.isBlank() || name.length() > 255) {
            inputCollectionName.getStyleClass().add("errorField");
            error = true;
        } else {
            inputCollectionName.getStyleClass().remove("errorField");
        }

        if (publisher.isBlank() || publisher.length() > 255) {
            inputCollectionPublisher.getStyleClass().add("errorField");
            error = true;
        } else {
            inputCollectionPublisher.getStyleClass().remove("errorField");
        }

        if (description.length() > 512) {
            txtAreaDescription.getStyleClass().add("errorField");
            error = true;
        } else {
            txtAreaDescription.getStyleClass().remove("errorField");
        }

        if (error) {
            CustomAlert.showAlert(
                    i18n.getString("formError"), i18n.getString("collectionFormErrorMessage"));
            return;
        }

        int createdStatus;
        if (collectionDto == null) {
            createdStatus =
                    CollectionService.createCollection(name, publisher, description, imagePath);
        } else {
            if (name.equals(collectionDto.getName())
                    && publisher.equals(collectionDto.getPublisher())
                    && description.equals(collectionDto.getDescription())
                    && imagePath == null) {
                inputCollectionPublisher.getScene().getWindow().hide();
                return;
            }
            createdStatus =
                    CollectionService.updateCollection(
                            collectionDto.getId(), name, publisher, description, imagePath);
        }
        if (createdStatus == 2) {
            CollectionService.getInstance().getCollectionsAsNodes();
            FXMLLoader fxmlLoader =
                    new FXMLLoader(
                            MainWindow.class.getResource(
                                    "/io/github/kevinpita/comicstore/view/collection-list.fxml"),
                            i18n.getResourceBundle());
            try {
                reloadedPane.setCenter(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            CustomAlert.showInfo(i18n.getString("newCollectionAlert"));
        } else if (createdStatus == 0) {
            inputCollectionName.getStyleClass().add("errorField");
            CustomAlert.showAlert(i18n.getString("duplicatedCollectionFormErrorMessage"));
        } else {
            CustomAlert.showAlert(i18n.getString("createCollectionError"));
        }

        inputCollectionPublisher.getScene().getWindow().hide();
    }

    @FXML
    void delete() {
        if (collectionDto == null) {
            return;
        }
        boolean deleteResult = CollectionService.deleteCollection(collectionDto.getId());
        if (deleteResult) {
            CustomAlert.showInfo(i18n.getString("deleteCollectionAlert"));
            inputCollectionPublisher.getScene().getWindow().hide();
            return;
        }
        CustomAlert.showAlert(i18n.getString("collectionFormDeleteErrorMessage"));
    }

    @FXML
    public void selectNewPicture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(i18n.getString("selectPictureTitle"));
        fileChooser
                .getExtensionFilters()
                .add(
                        new FileChooser.ExtensionFilter(
                                i18n.getString("selectPictureFilter"), "*.png", "*.jpg"));
        File selectedFile = fileChooser.showOpenDialog(circlePicture.getScene().getWindow());
        if (selectedFile == null) {
            return;
        }

        Image image = new Image(selectedFile.toPath().toUri().toString());
        if (!image.isError()) {
            this.imagePath = selectedFile.toPath();
        }
        if (this.imagePath != null) {
            circlePicture.setFill(new ImagePattern(image));
        }
    }
}
