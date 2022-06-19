/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.view;

import io.github.kevinpita.comicstore.model.AuthorDto;
import io.github.kevinpita.comicstore.model.CollectionDto;
import io.github.kevinpita.comicstore.service.AuthorService;
import io.github.kevinpita.comicstore.service.CollectionService;
import io.github.kevinpita.comicstore.service.ComicService;
import io.github.kevinpita.comicstore.util.CustomAlert;
import io.github.kevinpita.comicstore.util.i18n;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.commons.lang3.exception.ExceptionUtils;

@Slf4j
public class ReportListController {
    @javafx.fxml.FXML private ComboBox<CollectionDto> comboReportComicCollection;
    @javafx.fxml.FXML private Button btnReportCollection;
    @javafx.fxml.FXML private Button btnReportComicCollection;
    @javafx.fxml.FXML private ComboBox<AuthorDto> comboReportComicAuthor;
    @javafx.fxml.FXML private Button btnReportComicAuthor;
    @javafx.fxml.FXML private Button btnReportComic;

    @FXML
    private void initialize() {
        setStringBindings();

        // get collection observable list
        List<CollectionDto> collections = CollectionService.getInstance().getCollections();
        ObservableList<CollectionDto> collectionObservableList =
                FXCollections.observableArrayList(collections);

        // get author observable list
        List<AuthorDto> authors = AuthorService.getInstance().getAuthors();
        ObservableList<AuthorDto> authorObservableList = FXCollections.observableArrayList(authors);

        // set combo box items
        comboReportComicCollection.setItems(collectionObservableList);
        comboReportComicAuthor.setItems(authorObservableList);

        // if combo box is not empty, set first item as default
        // if combo is empty, disable combo
        if (comboReportComicCollection.getItems().size() > 0) {
            comboReportComicCollection.getSelectionModel().select(0);
        } else {
            btnReportComicCollection.setDisable(true);
            btnReportComicCollection.setDisable(true);
        }

        if (comboReportComicAuthor.getItems().size() > 0) {
            comboReportComicAuthor.getSelectionModel().select(0);
        } else {
            btnReportComicAuthor.setDisable(true);
            comboReportComicAuthor.setDisable(true);
        }
    }

    private void setStringBindings() {
        btnReportComic.textProperty().bind(i18n.getStringBinding("comicReport"));
        btnReportCollection.textProperty().bind(i18n.getStringBinding("collectionReport"));
        btnReportComicAuthor.textProperty().bind(i18n.getStringBinding("comicReportAuthor"));
        btnReportComicCollection
                .textProperty()
                .bind(i18n.getStringBinding("comicReportCollection"));
    }

    @FXML
    private void openCollectionReport() {
        try {
            InputStream in =
                    ReportListController.class.getResourceAsStream(
                            "/io/github/kevinpita/comicstore/reports/es/collection.jrxml");
            JRBeanCollectionDataSource comics =
                    new JRBeanCollectionDataSource(
                            new ArrayList<>(CollectionService.getInstance().getCollections()));
            Map<String, Object> parameters = new HashMap<>();
            JasperReport jasperReport = JasperCompileManager.compileReport(in);
            JasperPrint jasperPrint =
                    JasperFillManager.fillReport(jasperReport, parameters, comics);
            JasperViewer.viewReport(jasperPrint, false);

        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            CustomAlert.showAlert(
                    i18n.getString("errorShowingReport"),
                    btnReportCollection.getScene().getWindow());
        }
    }

    @FXML
    private void openComicReport() {
        try {
            InputStream in =
                    ReportListController.class.getResourceAsStream(
                            "/io/github/kevinpita/comicstore/reports/es/comic.jrxml");
            JRBeanCollectionDataSource comics =
                    new JRBeanCollectionDataSource(
                            new ArrayList<>(ComicService.getInstance().getComics()));
            Map<String, Object> parameters = new HashMap<>();
            JasperReport jasperReport = JasperCompileManager.compileReport(in);
            JasperPrint jasperPrint =
                    JasperFillManager.fillReport(jasperReport, parameters, comics);
            JasperViewer.viewReport(jasperPrint, false);

        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            CustomAlert.showAlert(
                    i18n.getString("errorShowingReport"),
                    btnReportCollection.getScene().getWindow());
        }
    }

    @FXML
    private void openCollectionComicReport() {
        try {
            CollectionDto collection =
                    comboReportComicCollection.getSelectionModel().getSelectedItem();
            JRBeanCollectionDataSource comics =
                    new JRBeanCollectionDataSource(
                            ComicService.getInstance().getComics().stream()
                                    .filter(
                                            (comic) ->
                                                    comic.getCollection()
                                                            .getName()
                                                            .equals(collection.getName()))
                                    .collect(Collectors.toList()));
            openFilteredReport(comics, collection.toString());

        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            CustomAlert.showAlert(
                    i18n.getString("errorShowingReport"),
                    btnReportCollection.getScene().getWindow());
        }
    }

    @FXML
    private void openAuthorComicReport() {
        try {
            AuthorDto author = comboReportComicAuthor.getSelectionModel().getSelectedItem();
            JRBeanCollectionDataSource comics =
                    new JRBeanCollectionDataSource(
                            ComicService.getInstance().getComics().stream()
                                    .filter(
                                            (comic) ->
                                                    comic.getComicCreators().stream()
                                                            .anyMatch(
                                                                    (comicCreator) ->
                                                                            comicCreator
                                                                                    .getCreator()
                                                                                    .equals(
                                                                                            author)))
                                    .collect(Collectors.toList()));
            openFilteredReport(comics, author.toString());

        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            CustomAlert.showAlert(
                    i18n.getString("errorShowingReport"),
                    btnReportCollection.getScene().getWindow());
        }
    }

    private void openFilteredReport(JRBeanCollectionDataSource comics, String parameterField)
            throws JRException {
        InputStream in =
                ReportListController.class.getResourceAsStream(
                        "/io/github/kevinpita/comicstore/reports/es/comic_filtered.jrxml");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("filter", parameterField);
        JasperReport jasperReport = JasperCompileManager.compileReport(in);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, comics);
        JasperViewer.viewReport(jasperPrint, false);
    }
}
