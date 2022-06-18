/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.view.create;

import io.github.kevinpita.comicstore.model.CollectionDto;
import io.github.kevinpita.comicstore.model.table.ComicTable;
import io.github.kevinpita.comicstore.service.CollectionService;
import io.github.kevinpita.comicstore.service.ComicService;
import io.github.kevinpita.comicstore.util.CustomAlert;
import io.github.kevinpita.comicstore.util.ImageUtil;
import io.github.kevinpita.comicstore.util.i18n;
import io.github.kevinpita.comicstore.view.MainController;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

@Slf4j
public class CollectionData {
    @FXML private GridPane parentPane;
    @FXML private Circle circlePicture;
    @FXML private TextField inputCollectionName;
    @FXML private TextField inputCollectionPublisher;
    @FXML private TextArea txtAreaDescription;
    @FXML private Button removeButton;

    @FXML private TableView<ComicTable> comicListTableCollection;
    @FXML private TableColumn<ComicTable, Integer> tableColumnComicId;
    @FXML private TableColumn<ComicTable, String> tableColumnComicTitle;
    @FXML private Button saveButton;
    private Path imagePath;
    @Setter private CollectionDto collectionDto;

    public void lateInit() {
        if (setupImage()) return;

        setupTable();

        inputCollectionName.setText(collectionDto.getName());
        inputCollectionPublisher.setText(collectionDto.getPublisher());
        txtAreaDescription.setText(collectionDto.getDescription());

        Platform.runLater(() -> parentPane.requestFocus());
        removeButton.setDisable(false);
    }

    @FXML
    private void cancel() {
        circlePicture.getScene().getWindow().hide();
    }

    @FXML
    private void save() {
        boolean error;
        String name = inputCollectionName.getText().strip();
        String publisher = inputCollectionPublisher.getText().strip();
        String description = txtAreaDescription.getText().strip();

        error = checkError(name, publisher, description);

        if (error) {
            CustomAlert.showAlert(
                    i18n.getString("formError"), i18n.getString("collectionFormErrorMessage"));
            return;
        }

        if (checkSameObject(name, publisher, description)) return;

        int createdStatus;
        if (collectionDto == null) {
            createdStatus =
                    CollectionService.createCollection(name, publisher, description, imagePath);
        } else {
            createdStatus =
                    CollectionService.updateCollection(
                            collectionDto.getId(), name, publisher, description, imagePath);
        }

        if (createdStatus == 2) {
            reloadCollectionList();
            CustomAlert.showInfo(i18n.getString("newCollectionAlert"));
            inputCollectionPublisher.getScene().getWindow().hide();
        } else if (createdStatus == 0) {
            inputCollectionName.getStyleClass().add("errorField");
            CustomAlert.showAlert(i18n.getString("duplicatedCollectionFormErrorMessage"));
        } else {
            CustomAlert.showAlert(i18n.getString("createCollectionError"));
            inputCollectionPublisher.getScene().getWindow().hide();
        }
    }

    @FXML
    private void delete() {
        if (collectionDto == null) {
            return;
        }
        boolean deleteResult = CollectionService.deleteCollection(collectionDto.getId());
        if (deleteResult) {
            reloadCollectionList();
            CustomAlert.showInfo(i18n.getString("deleteCollectionAlert"));
            inputCollectionPublisher.getScene().getWindow().hide();
            return;
        }
        CustomAlert.showAlert(i18n.getString("collectionFormDeleteErrorMessage"));
    }

    @FXML
    private void selectNewPicture() {
        imagePath = ImageUtil.selectImage(circlePicture, imagePath);
    }

    private void reloadCollectionList() {
        CollectionService.getInstance().getCollectionsAsNodes();
        FXMLLoader fxmlLoader = MainController.getFxmlLoader("collection-list");
        try {
            MainController.getMainPane().setCenter(fxmlLoader.load());
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
            CustomAlert.showAlert(i18n.getString("errorScreenLoad"));
        }
    }

    private boolean checkError(String name, String publisher, String description) {
        boolean error = false;
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
        return error;
    }

    private boolean checkSameObject(String name, String publisher, String description) {
        if (collectionDto == null) return false;
        if (name.equals(collectionDto.getName())
                && publisher.equals(collectionDto.getPublisher())
                && description.equals(collectionDto.getDescription())
                && imagePath == null) {
            inputCollectionPublisher.getScene().getWindow().hide();
            return true;
        }
        return false;
    }

    private void setupTable() {
        comicListTableCollection.setDisable(false);

        tableColumnComicId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnComicTitle.setCellValueFactory(new PropertyValueFactory<>("title"));

        comicListTableCollection.setItems(
                ComicService.getComicTableList(collectionDto.getComics()));
    }

    private boolean setupImage() {
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
            return true;
        }
        return false;
    }
}
