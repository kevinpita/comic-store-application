/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.view.create;

import io.github.kevinpita.comicstore.model.ComicDto;
import io.github.kevinpita.comicstore.util.i18n;
import java.io.File;
import java.nio.file.Path;
import java.util.Objects;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ComicData {
    @FXML private GridPane parentPane;
    @FXML private Circle circlePicture;
    @FXML private Button removeButton;

    private Path imagePath;
    @Setter private ComicDto comicDto;
    @FXML private Button newAuthorComic;
    @FXML private TableColumn authorComicRoleTable;
    @FXML private TableColumn comicCopyPurchaseTable;
    @FXML private Button deleteComicCopy;
    @FXML private ComboBox collectionPublisher;
    @FXML private TableView comicCopyTable;
    @FXML private Button editAuthorComic;
    @FXML private Button deleteAuthorComic;
    @FXML private TextArea collectionDescription;
    @FXML private TableColumn comicCopyCoverTable;
    @FXML private TableView authorComicTable;
    @FXML private Button cancelButton;
    @FXML private Button newComicCopy;
    @FXML private TextField creatorAuthorMenu;
    @FXML private Button editComicCopy;
    @FXML private Button saveButton;
    @FXML private TextField comicIssueNumber;
    @FXML private TableColumn comicCopyStateTable;
    @FXML private TableColumn authorComicNameTable;
    @FXML private TableColumn comicCopyPriceTable;

    public void lateInit() {
        Image image;
        try {
            image = new Image(comicDto.getImageUrl());
            if (image.isError()) {
                throw new Exception();
            }
        } catch (Exception e) {
            image =
                    new Image(
                            Objects.requireNonNull(
                                    this.getClass()
                                            .getResourceAsStream(
                                                    "/io/github/kevinpita/comicstore/view/images/comic.png")));
            ;
        }

        circlePicture.setFill(new ImagePattern(image));
        if (comicDto == null) {
            Platform.runLater(() -> parentPane.requestFocus());
            return;
        }
        //        comicListTableCollection.setDisable(false);
        //
        //        tableColumnComicId.setCellValueFactory(new PropertyValueFactory<ComicTable,
        // Integer>("id"));
        //        tableColumnComicTitle.setCellValueFactory(
        // new PropertyValueFactory<ComicTable, String>("title"))
        //
        //        comicListTableCollection.setItems(
        //                ComicService.getComicTableList(comicDto.g.getComics()));
        //
        //        inputCollectionName.setText(collectionDto.getName());
        //        inputCollectionPublisher.setText(collectionDto.getPublisher());
        //        txtAreaDescription.setText(collectionDto.getDescription());
        //
        //        Platform.runLater(() -> parentPane.requestFocus());
        //        removeButton.setDisable(false);
    }

    @FXML
    public void cancel() {
        circlePicture.getScene().getWindow().hide();
    }

    @FXML
    void save() {
        //        boolean error = false;
        //        String name = inputCollectionName.getText().strip();
        //        String publisher = inputCollectionPublisher.getText().strip();
        //        String description = txtAreaDescription.getText().strip();
        //
        //        if (name.isBlank() || name.length() > 255) {
        //            inputCollectionName.getStyleClass().add("errorField");
        //            error = true;
        //        } else {
        //            inputCollectionName.getStyleClass().remove("errorField");
        //        }
        //
        //        if (publisher.isBlank() || publisher.length() > 255) {
        //            inputCollectionPublisher.getStyleClass().add("errorField");
        //            error = true;
        //        } else {
        //            inputCollectionPublisher.getStyleClass().remove("errorField");
        //        }
        //
        //        if (description.length() > 512) {
        //            txtAreaDescription.getStyleClass().add("errorField");
        //            error = true;
        //        } else {
        //            txtAreaDescription.getStyleClass().remove("errorField");
        //        }
        //
        //        if (error) {
        //            CustomAlert.showAlert(
        //                    i18n.getString("formError"),
        // i18n.getString("collectionFormErrorMessage"));
        //            return;
        //        }
        //
        //        int createdStatus;
        //        if (comicDto == null) {
        //            createdStatus =
        //                    CollectionService.createCollection(name, publisher, description,
        // imagePath);
        //        } else {
        //            if (name.equals(comi.getName())
        //                    && publisher.equals(collectionDto.getPublisher())
        //                    && description.equals(collectionDto.getDescription())
        //                    && imagePath == null) {
        //                inputCollectionPublisher.getScene().getWindow().hide();
        //                return;
        //            }
        //            createdStatus =
        //                    CollectionService.updateCollection(
        //                            collectionDto.getId(), name, publisher, description,
        // imagePath);
        //        }
        //        if (createdStatus == 2) {
        //            CollectionService.getInstance().getCollectionsAsNodes();
        //            FXMLLoader fxmlLoader =
        //                    new FXMLLoader(
        //                            MainWindow.class.getResource(
        //
        // "/io/github/kevinpita/comicstore/view/collection-list.fxml"),
        //                            i18n.getResourceBundle());
        //            try {
        //                reloadedPane.setCenter(fxmlLoader.load());
        //            } catch (IOException e) {
        //                log.error("Error loading collection list view",
        // ExceptionUtils.getStackTrace(e));
        //            }
        //            CustomAlert.showInfo(i18n.getString("newCollectionAlert"));
        //        } else if (createdStatus == 0) {
        //            inputCollectionName.getStyleClass().add("errorField");
        //            CustomAlert.showAlert(i18n.getString("duplicatedCollectionFormErrorMessage"));
        //        } else {
        //            CustomAlert.showAlert(i18n.getString("createCollectionError"));
        //        }
        //
        //        inputCollectionPublisher.getScene().getWindow().hide();
    }

    @FXML
    void delete() {
        //        if (collectionDto == null) {
        //            return;
        //        }
        //        boolean deleteResult = CollectionService.deleteCollection(collectionDto.getId());
        //        if (deleteResult) {
        //            CollectionService.getInstance().getCollectionsAsNodes();
        //            FXMLLoader fxmlLoader =
        //                    new FXMLLoader(
        //                            MainWindow.class.getResource(
        //
        // "/io/github/kevinpita/comicstore/view/collection-list.fxml"),
        //                            i18n.getResourceBundle());
        //            try {
        //                reloadedPane.setCenter(fxmlLoader.load());
        //            } catch (IOException e) {
        //                log.error("Error loading collection list view",
        // ExceptionUtils.getStackTrace(e));
        //            }
        //            CustomAlert.showInfo(i18n.getString("deleteCollectionAlert"));
        //            inputCollectionPublisher.getScene().getWindow().hide();
        //            return;
        //        }
        //        CustomAlert.showAlert(i18n.getString("collectionFormDeleteErrorMessage"));
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
