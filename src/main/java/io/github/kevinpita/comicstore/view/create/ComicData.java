/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.view.create;

import io.github.kevinpita.comicstore.configuration.Resolution;
import io.github.kevinpita.comicstore.model.CollectionDto;
import io.github.kevinpita.comicstore.model.ComicCopyDto;
import io.github.kevinpita.comicstore.model.ComicDto;
import io.github.kevinpita.comicstore.model.table.AuthorComicTable;
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
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

@Slf4j
public class ComicData {
    @FXML private Button newAuthorComic;
    @FXML private TableColumn<AuthorComicTable, String> authorComicRoleTable;
    @FXML private TableColumn<AuthorComicTable, String> authorComicNameTable;
    @FXML private TableColumn<ComicCopyTable, String> comicCopyPurchaseTable;
    @FXML private Button deleteComicCopy;
    @FXML private ComboBox<CollectionDto> collectionPublisher;
    @FXML private TableView<ComicCopyTable> comicCopyTableView;
    @FXML private Button editAuthorComic;
    @FXML private Button deleteAuthorComic;
    @FXML private TableColumn<ComicCopyTable, String> comicCopyCoverTable;
    @FXML private Circle circlePicture;
    @FXML private TableView<AuthorComicTable> authorComicTableView;
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
    private ObservableList<AuthorComicTable> authorComicTableList;

    public void lateInit() {
        fillComicAuthorTableList();
        fillComicCopyTableList();

        setupTable(authorComicTableView, deleteAuthorComic, editAuthorComic);
        setupTable(comicCopyTableView, deleteComicCopy, editComicCopy);

        setCollectionPublisher();

        setupAuthorTableData();
        setupCopyTableData();

        if (setupImage()) return;

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
    private void editComicCopy() {
        ComicCopyTable comicCopyTableRow = comicCopyTableView.getSelectionModel().getSelectedItem();
        if (comicCopyTableView == null) {
            editComicCopy.setDisable(true);
            removeButton.setDisable(true);
            return;
        }
        openNewComicCopy(comicCopyTableRow.toDto());
    }

    @FXML
    private void editAuthorComic() {
        AuthorComicTable comicCopyTableRow =
                authorComicTableView.getSelectionModel().getSelectedItem();
        if (comicCopyTableView == null) {
            editComicCopy.setDisable(true);
            removeButton.setDisable(true);
            return;
        }
        openNewAuthorComic(comicCopyTableRow);
    }

    @FXML
    private void cancel() {
        circlePicture.getScene().getWindow().hide();
    }

    @FXML
    private void save() {}

    @FXML
    private void delete() {}

    @FXML
    private void selectNewPicture() {
        imagePath = ImageUtil.selectImage(circlePicture, imagePath);
    }

    @FXML
    private void newComicCopy() {
        openNewComicCopy(null);
    }

    @FXML
    private void newAuthorComic() {
        openNewAuthorComic(null);
    }

    @FXML
    private void deleteAuthorComic() {
        AuthorComicTable authorComicTableRow =
                authorComicTableView.getSelectionModel().getSelectedItem();
        if (authorComicTableRow == null) {
            deleteAuthorComic.setDisable(true);
            return;
        }
        authorComicTableView.getItems().remove(authorComicTableRow);
        authorComicTableView.getSelectionModel().select(null);
    }

    @FXML
    private void deleteComicCopy() {}

    private void openNewComicCopy(ComicCopyDto comicCopyDto) {
        FXMLLoader loader = MainController.getFxmlLoader("/create/comic-copy-data");
        Parent root;
        try {
            root = loader.load();
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return;
        }
        ComicCopyData controller = loader.getController();

        controller.lateInit();

        Scene scene = new Scene(root);
        Stage stage = new Stage();

        stage.setScene(scene);
        stage.setTitle(i18n.getString("comicCopyTitle"));

        // put configuration FXML in the center of the parent window
        Bounds mainBounds = parentPane.getScene().getRoot().getLayoutBounds();
        stage.setX(
                parentPane.getScene().getWindow().getX()
                        + (mainBounds.getWidth() - Resolution.COMIC_COPY.getWIDTH()) / 2);
        stage.setY(
                parentPane.getScene().getWindow().getY()
                        + (mainBounds.getHeight() - Resolution.COMIC_COPY.getHEIGHT()) / 2);

        stage.setResizable(false);
        stage.initOwner(parentPane.getScene().getWindow());
        // make configuration screen modal
        stage.initModality(Modality.WINDOW_MODAL);

        stage.showAndWait();
    }

    private void openNewAuthorComic(AuthorComicTable authorComicElement) {
        FXMLLoader loader = MainController.getFxmlLoader("/create/author-comic-data");
        Parent root;
        try {
            root = loader.load();
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return;
        }
        AuthorComicData controller = loader.getController();

        controller.setAuthorComicTable(authorComicElement);
        controller.setRoles(
                authorComicTableList.stream()
                        .map(AuthorComicTable::getRole)
                        .collect(Collectors.toList()));
        controller.setTable(authorComicTableView);

        controller.lateInit();

        Scene scene = new Scene(root);
        Stage stage = new Stage();

        stage.setScene(scene);
        stage.setTitle(i18n.getString("authorCreatorTitle"));

        // put configuration FXML in the center of the parent window
        Bounds mainBounds = parentPane.getScene().getRoot().getLayoutBounds();
        stage.setX(
                parentPane.getScene().getWindow().getX()
                        + (mainBounds.getWidth() - Resolution.AUTHOR_COMIC.getWIDTH()) / 2);
        stage.setY(
                parentPane.getScene().getWindow().getY()
                        + (mainBounds.getHeight() - Resolution.AUTHOR_COMIC.getHEIGHT()) / 2);

        stage.setResizable(false);
        stage.initOwner(parentPane.getScene().getWindow());
        // make configuration screen modal
        stage.initModality(Modality.WINDOW_MODAL);

        stage.showAndWait();
    }

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
                                                copy.getId(),
                                                copy.getCover(),
                                                copy.getState(),
                                                copy.getPrice(),
                                                copy.getPurchaseDate());

                                comicCopyTableList.add(comicCopyTable);
                            });
        }
    }

    public void fillComicAuthorTableList() {
        if (authorComicTableList == null) {
            authorComicTableList = FXCollections.observableArrayList();
        }
        authorComicTableList.clear();
        if (comicDto != null) {
            comicDto.getComicCreators()
                    .forEach(
                            comicCreator -> {
                                AuthorComicTable authorComicTable =
                                        new AuthorComicTable(
                                                comicCreator.getId(),
                                                comicCreator.getRole(),
                                                comicCreator.getCreator());
                                authorComicTableList.add(authorComicTable);
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
            CustomAlert.showAlert(
                    i18n.getString("errorScreenLoad"), deleteComicCopy.getScene().getWindow());
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

        authorComicTableView.setItems(authorComicTableList);
    }

    private void setupCopyTableData() {
        comicCopyStateTable.setCellValueFactory(new PropertyValueFactory<>("state"));
        comicCopyPriceTable.setCellValueFactory(new PropertyValueFactory<>("price"));
        comicCopyCoverTable.setCellValueFactory(new PropertyValueFactory<>("cover"));
        comicCopyPurchaseTable.setCellValueFactory(new PropertyValueFactory<>("purchase"));

        comicCopyTableView.setItems(comicCopyTableList);
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
