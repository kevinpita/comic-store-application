/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.service;

import static io.github.kevinpita.comicstore.service.CollectionService.uploadImage;

import com.google.gson.Gson;
import io.github.kevinpita.comicstore.configuration.ReturnStatus;
import io.github.kevinpita.comicstore.configuration.UrlPath;
import io.github.kevinpita.comicstore.model.ComicDto;
import io.github.kevinpita.comicstore.model.data.ComicListDto;
import io.github.kevinpita.comicstore.model.data.NewComicDto;
import io.github.kevinpita.comicstore.model.table.ComicTable;
import io.github.kevinpita.comicstore.util.CustomAlert;
import io.github.kevinpita.comicstore.util.RequestUtil;
import io.github.kevinpita.comicstore.view.ComicController;
import io.github.kevinpita.comicstore.view.MainController;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

// comic singleton
@Slf4j
public class ComicService {
    private static ComicService instance;
    private ObservableList<ComicDto> comics;
    private ObservableList<Node> nodes;
    @Getter private Map<Node, ComicController> nodeControllerMap = new HashMap<>();

    private ComicService() {}

    public static ComicService getInstance() {
        if (instance == null) {
            instance = new ComicService();
        }
        return instance;
    }

    public static ObservableList<ComicTable> getComicTableList(List<ComicDto> comics) {
        ObservableList<ComicTable> comicTableList = FXCollections.observableArrayList();
        comics.forEach(
                comic -> comicTableList.add(new ComicTable(comic.getId(), comic.getTitle())));
        return comicTableList;
    }

    public ObservableList<ComicDto> getComics() {
        if (comics == null) {
            comics = FXCollections.observableArrayList();
        }
        fillComics();
        return comics;
    }

    public ObservableList<Node> getCollectionsAsNodes() {
        if (nodes == null) {
            nodes = FXCollections.observableArrayList();
        }

        nodes.clear();
        nodeControllerMap.clear();

        for (ComicDto comic : getComics()) {
            FXMLLoader loader = MainController.getFxmlLoader("comic");

            Node node;
            try {
                node = loader.load();
            } catch (IOException e) {
                log.error(ExceptionUtils.getStackTrace(e));
                continue;
            }

            ComicController comicController = loader.getController();

            comicController.setComic(comic);
            comicController.setImage(comic.getImageUrl());
            comicController.setTitle(comic.getFullTitle());

            comicController.lateInit();

            nodes.add(node);
            nodeControllerMap.put(node, comicController);
        }

        return nodes;
    }

    public void fillComics() {
        String url = UrlPath.COMIC.getUrl();

        HttpClient client = HttpClient.newHttpClient();
        try {

            HttpRequest request = RequestUtil.createRequest(url).build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            Gson gson = RequestUtil.getGson();

            if (response.statusCode() != 200) {
                return;
            }
            comics.clear();
            comics.setAll(gson.fromJson(response.body(), ComicListDto.class).getData());
        } catch (Exception logged) {
            log.error(ExceptionUtils.getStackTrace(logged));
            // TODO: connecting alert
            CustomAlert.showConnectingAlert(null);
        }
    }

    public ReturnStatus createComic(ComicDto comicDto, Path image) {
        String url = UrlPath.COMIC.getUrl();

        HttpClient client = HttpClient.newHttpClient();
        String json = RequestUtil.getGson().toJson(comicDto);
        try {
            HttpRequest request =
                    RequestUtil.createRequest(url)
                            .POST(HttpRequest.BodyPublishers.ofString(json))
                            .header("Content-Type", "application/json")
                            .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 409) {
                return ReturnStatus.DUPLICATED;
            }
            if (response.statusCode() != 201) {
                return ReturnStatus.ERROR;
            }

            int id =
                    RequestUtil.getGson()
                            .fromJson(response.body(), NewComicDto.class)
                            .getData()
                            .getId();
            if (image != null) {
                uploadImage(UrlPath.UPLOAD_COMIC_IMAGE.getUrl(), image, id);
            }
            return ReturnStatus.SUCCESS;
        } catch (Exception logged) {
            log.error(ExceptionUtils.getStackTrace(logged));
            CustomAlert.showConnectingAlert(null);
        }
        return ReturnStatus.ERROR;
    }

    public ReturnStatus updateComic(ComicDto comicDto, Path image) {
        String url = UrlPath.COMIC.getUrl();

        HttpClient client = HttpClient.newHttpClient();
        String json = RequestUtil.getGson().toJson(comicDto);
        try {
            HttpRequest request =
                    RequestUtil.createRequest(url)
                            .PUT(HttpRequest.BodyPublishers.ofString(json))
                            .header("Content-Type", "application/json")
                            .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 409) {
                return ReturnStatus.DUPLICATED;
            }
            if (response.statusCode() != 201) {
                return ReturnStatus.ERROR;
            }

            if (image != null) {
                uploadImage(UrlPath.UPLOAD_COMIC_IMAGE.getUrl(), image, comicDto.getId());
            }
            return ReturnStatus.SUCCESS;
        } catch (Exception logged) {
            log.error(ExceptionUtils.getStackTrace(logged));
            CustomAlert.showConnectingAlert(null);
        }
        return ReturnStatus.ERROR;
    }

    public static void deletePicture(int id) {
        String url = UrlPath.COMIC_IMAGE.getUrl() + "/" + id;

        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpRequest request = RequestUtil.createRequest(url).DELETE().build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            response.statusCode();
        } catch (Exception logged) {
            log.error(ExceptionUtils.getStackTrace(logged));
            CustomAlert.showConnectingAlert(null);
        }
    }

    public static boolean deleteComic(int id) {
        String url = UrlPath.COMIC.getUrl() + "/" + id;

        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpRequest request = RequestUtil.createRequest(url).DELETE().build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                return false;
            }
            deletePicture(id);
            return true;
        } catch (Exception logged) {
            log.error(ExceptionUtils.getStackTrace(logged));
            CustomAlert.showConnectingAlert(null);
        }
        return false;
    }
}
