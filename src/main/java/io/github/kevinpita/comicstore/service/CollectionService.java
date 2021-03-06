/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.service;

import com.github.mizosoft.methanol.Methanol;
import com.github.mizosoft.methanol.MultipartBodyPublisher;
import com.github.mizosoft.methanol.MutableRequest;
import com.google.gson.Gson;
import io.github.kevinpita.comicstore.configuration.ReturnStatus;
import io.github.kevinpita.comicstore.configuration.UrlPath;
import io.github.kevinpita.comicstore.model.CollectionDto;
import io.github.kevinpita.comicstore.model.data.CollectionListDto;
import io.github.kevinpita.comicstore.model.data.NewCollectionDto;
import io.github.kevinpita.comicstore.util.CustomAlert;
import io.github.kevinpita.comicstore.util.RequestUtil;
import io.github.kevinpita.comicstore.view.CollectionController;
import io.github.kevinpita.comicstore.view.MainController;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

// collection singleton
@Slf4j
public class CollectionService {
    private static CollectionService instance;
    private ObservableList<CollectionDto> collections;
    private ObservableList<Node> nodes;
    @Getter private Map<Node, CollectionController> nodeControllerMap = new HashMap<>();

    private CollectionService() {}

    public static CollectionService getInstance() {
        if (instance == null) {
            instance = new CollectionService();
        }
        return instance;
    }

    public static void uploadImage(String urlPath, Path imagePath, int id) {
        try {
            final Methanol client = Methanol.create();
            MultipartBodyPublisher multipartBody = RequestUtil.getMultipartBodyPublisher(imagePath);
            MutableRequest request = RequestUtil.getMutableRequest(urlPath, id, multipartBody);

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
            response.statusCode();

        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }

    public static ReturnStatus createCollection(
            String name, String publisher, String description, Path image) {
        String url = UrlPath.COLLECTION.getUrl();

        HttpClient client = HttpClient.newHttpClient();
        CollectionDto dto = getCollectionDto(name, publisher, description);
        String json = RequestUtil.getGson().toJson(dto);
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
                            .fromJson(response.body(), NewCollectionDto.class)
                            .getData()
                            .getId();
            if (image != null) {
                uploadImage(UrlPath.UPLOAD_COLLECTION_IMAGE.getUrl(), image, id);
            }
            return ReturnStatus.SUCCESS;
        } catch (Exception logged) {
            log.error(ExceptionUtils.getStackTrace(logged));
            CustomAlert.showConnectingAlert(null);
        }
        return ReturnStatus.SUCCESS;
    }

    public static ReturnStatus updateCollection(
            int id, String name, String publisher, String description, Path image) {
        String url = UrlPath.COLLECTION.getUrl() + "/" + id;

        HttpClient client = HttpClient.newHttpClient();
        CollectionDto dto = getCollectionDto(name, publisher, description);
        String json = RequestUtil.getGson().toJson(dto);
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
                uploadImage(UrlPath.UPLOAD_COLLECTION_IMAGE.getUrl(), image, id);
            }
            return ReturnStatus.SUCCESS;
        } catch (Exception logged) {
            log.error(ExceptionUtils.getStackTrace(logged));
            CustomAlert.showConnectingAlert(null);
        }
        return ReturnStatus.ERROR;
    }

    private static CollectionDto getCollectionDto(
            String name, String publisher, String description) {
        return CollectionDto.builder()
                .name(name)
                .publisher(publisher)
                .description(description)
                .build();
    }

    public static void deletePicture(int id) {
        String url = UrlPath.COLLECTION_IMAGE.getUrl() + "/" + id;

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

    public static boolean deleteCollection(int id) {
        String url = UrlPath.COLLECTION.getUrl() + "/" + id;

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

    public ObservableList<CollectionDto> getCollections() {
        if (collections == null) {
            collections = FXCollections.observableArrayList();
        }
        fillCollections();
        return collections;
    }

    public ObservableList<Node> getCollectionsAsNodes() {
        if (nodes == null) {
            nodes = FXCollections.observableArrayList();
        }

        nodes.clear();
        nodeControllerMap.clear();
        for (CollectionDto collection : getCollections()) {
            FXMLLoader loader = MainController.getFxmlLoader("collection");

            Node node;
            try {
                node = loader.load();
            } catch (IOException e) {
                log.error(ExceptionUtils.getStackTrace(e));
                continue;
            }

            CollectionController collectionController = loader.getController();

            collectionController.setCollection(collection);
            collectionController.setImage(collection.getImageUrl());
            collectionController.setTitle(collection.getName());

            collectionController.lateInit();

            nodes.add(node);
            nodeControllerMap.put(node, collectionController);
        }

        return nodes;
    }

    public void fillCollections() {
        String url = UrlPath.COLLECTION.getUrl();

        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpRequest request = RequestUtil.createRequest(url).build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                return;
            }

            collections.clear();

            Gson gson = RequestUtil.getGson();
            collections.setAll(gson.fromJson(response.body(), CollectionListDto.class).getData());
        } catch (Exception logged) {
            log.error(ExceptionUtils.getStackTrace(logged));
            CustomAlert.showConnectingAlert(null);
        }
    }
}
