/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.view.create;

import io.github.kevinpita.comicstore.model.CollectionDto;
import io.github.kevinpita.comicstore.model.ComicDto;
import io.github.kevinpita.comicstore.model.table.ComicAuthorTable;
import io.github.kevinpita.comicstore.model.table.ComicCopyTable;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
public class ComicData {
    @FXML private Button newAuthorComic;
    @FXML private TableColumn<ComicAuthorTable, String> authorComicRoleTable;
    @FXML private TableColumn<ComicAuthorTable, String> authorComicNameTable;
    @FXML private TableColumn<ComicCopyTable, String> comicCopyPurchaseTable;
    @FXML private Button deleteComicCopy;
    @FXML private ComboBox<CollectionDto> collectionPublisher;
    @FXML private TableView<ComicCopyTable> comicCopyTable;
    @FXML private Button editAuthorComic;
    @FXML private Button deleteAuthorComic;
    @FXML private TableColumn<ComicCopyTable, String> comicCopyCoverTable;
    @FXML private Circle circlePicture;
    @FXML private TableView<ComicAuthorTable> authorComicTable;
    @FXML private Button cancelButton;
    @FXML private Button newComicCopy;
    @FXML private Button removeButton;
    @FXML private TextField creatorAuthorMenu;
    @FXML private GridPane parentPane;
    @FXML private Button editComicCopy;
    @FXML private Button saveButton;
    @FXML private TextField comicIssueNumber;
    @FXML private TableColumn<ComicCopyTable, String> comicCopyStateTable;
    @FXML private TableColumn<ComicCopyTable, Double> comicCopyPriceTable;
    @FXML private TextArea comicDescription;
    private Path imagePath;
    @Setter private ComicDto comicDto;

    private ObservableList<ComicCopyTable> comicCopyTableList;
    private ObservableList<ComicAuthorTable> comicAuthorTableList;

    public void lateInit() {
        setupTable(authorComicTable, deleteAuthorComic, editAuthorComic);
        setupTable(comicCopyTable, deleteComicCopy, editComicCopy);
        setCollectionPublisher();

        if (setupImage()) return;

        setupAuthorTableData();
        setupCopyTableData();

        creatorAuthorMenu.setText(comicDto.getTitle());
        comicIssueNumber.setText(comicDto.getIssueNumber() + "");
        comicDescription.setText(comicDto.getDescription());

        Platform.runLater(() -> parentPane.requestFocus());
        removeButton.setDisable(false);
    }

    private void setCollectionPublisher() {
        ObservableList<CollectionDto> collectionDtoList =
                CollectionService.getInstance().getCollections();

        if (collectionDtoList.size() == 0) {
            collectionPublisher.setDisable(true);
            return;
        }

        collectionPublisher.setItems(collectionDtoList);
        collectionPublisher.setValue(collectionDtoList.get(0));
    }

    @FXML
    private void cancel() {
        circlePicture.getScene().getWindow().hide();
    }

    @FXML
    private void save() {
        //        boolean error;
        //        String name = inputCollectionName.getText().strip();
        //        String publisher = inputCollectionPublisher.getText().strip();
        //        String description = txtAreaDescription.getText().strip();
        //
        //        error = checkError(name, publisher, description);
        //
        //        if (error) {
        //            CustomAlert.showAlert(
        //                    i18n.getString("formError"),
        // i18n.getString("collectionFormErrorMessage"));
        //            return;
        //        }
        //
        //        if (checkSameObject(name, publisher, description)) return;
        //
        //        int createdStatus;
        //        if (collectionDto == null) {
        //            createdStatus =
        //                    CollectionService.createCollection(name, publisher, description,
        // imagePath);
        //        } else {
        //            createdStatus =
        //                    CollectionService.updateCollection(
        //                            collectionDto.getId(), name, publisher, description,
        // imagePath);
        //        }
        //
        //        if (createdStatus == 2) {
        //            reloadCollectionList();
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
    private void delete() {
        //        if (collectionDto == null) {
        //            return;
        //        }
        //        boolean deleteResult = CollectionService.deleteCollection(collectionDto.getId());
        //        if (deleteResult) {
        //            reloadCollectionList();
        //            CustomAlert.showInfo(i18n.getString("deleteCollectionAlert"));
        //            inputCollectionPublisher.getScene().getWindow().hide();
        //            return;
        //        }
        //        CustomAlert.showAlert(i18n.getString("collectionFormDeleteErrorMessage"));
    }

    @FXML
    private void selectNewPicture() {
        imagePath = ImageUtil.selectImage(circlePicture, imagePath);
    }

    @FXML
    private void newComicCopy() {}

    @FXML
    private void newAuthorComic() {}

    public void fillComicCopyTableList() {
        if (comicCopyTableList == null) {
            comicCopyTableList = FXCollections.observableArrayList();
        }
        comicCopyTableList.clear();
        if (comicDto != null) {
            comicDto.getCopies()
                    .forEach(
                            copy -> {
                                ComicCopyTable comicCopyTable =
                                        new ComicCopyTable(
                                                copy.getCover(),
                                                copy.getState(),
                                                copy.getPrice(),
                                                copy.getPurchaseDate());

                                comicCopyTableList.add(comicCopyTable);
                            });
        }
    }

    public void fillComicAuthorTableList() {
        if (comicAuthorTableList == null) {
            comicAuthorTableList = FXCollections.observableArrayList();
        }
        comicAuthorTableList.clear();
        if (comicDto != null) {
            comicDto.getComicCreators()
                    .forEach(
                            comicCreator -> {
                                ComicAuthorTable comicAuthorTable =
                                        new ComicAuthorTable(
                                                comicCreator.getRole(),
                                                comicCreator.getCreator().toString());
                                comicAuthorTableList.add(comicAuthorTable);
                            });
        }
    }

    private void reloadCollectionList() {
        ComicService.getInstance().getCollectionsAsNodes();
        FXMLLoader fxmlLoader = MainController.getFxmlLoader("comic-list");
        try {
            MainController.getMainPane().setCenter(fxmlLoader.load());
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
            CustomAlert.showAlert(i18n.getString("errorScreenLoad"));
        }
    }

    private boolean checkError(String name, String publisher, String description) {
        //        boolean error = false;
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
        //        return error;
        return false;
    }

    private boolean checkSameObject(String name, String publisher, String description) {
        //        if (name.equals(collectionDto.getName())
        //                && publisher.equals(collectionDto.getPublisher())
        //                && description.equals(collectionDto.getDescription())
        //                && imagePath == null) {
        //            inputCollectionPublisher.getScene().getWindow().hide();
        //            return true;
        //        }
        return false;
    }

    private void setupTable(TableView<?> table, Button delete, Button edit) {
        table.getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (obs, oldSelection, newSelection) -> {
                            delete.setDisable(newSelection == null);
                            edit.setDisable(newSelection == null);
                        });
    }

    private void setupAuthorTableData() {
        authorComicNameTable.setCellValueFactory(new PropertyValueFactory<>("name"));
        authorComicRoleTable.setCellValueFactory(new PropertyValueFactory<>("role"));

        fillComicAuthorTableList();
        authorComicTable.setItems(comicAuthorTableList);
    }

    private void setupCopyTableData() {
        comicCopyStateTable.setCellValueFactory(new PropertyValueFactory<>("state"));
        comicCopyPriceTable.setCellValueFactory(new PropertyValueFactory<>("price"));
        comicCopyCoverTable.setCellValueFactory(new PropertyValueFactory<>("cover"));
        comicCopyPurchaseTable.setCellValueFactory(new PropertyValueFactory<>("purchase"));

        fillComicCopyTableList();
        comicCopyTable.setItems(comicCopyTableList);
    }

    private boolean setupImage() {
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
        }

        circlePicture.setFill(new ImagePattern(image));
        if (comicDto == null) {
            Platform.runLater(() -> parentPane.requestFocus());
            return true;
        }
        return false;
    }
}
