/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.service;

import com.github.mizosoft.methanol.MediaType;
import com.github.mizosoft.methanol.Methanol;
import com.github.mizosoft.methanol.MultipartBodyPublisher;
import com.github.mizosoft.methanol.MutableRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import io.github.kevinpita.comicstore.configuration.Configuration;
import io.github.kevinpita.comicstore.configuration.UrlPath;
import io.github.kevinpita.comicstore.model.CollectionDto;
import io.github.kevinpita.comicstore.model.data.CollectionListDto;
import io.github.kevinpita.comicstore.model.data.NewCollectionDto;
import io.github.kevinpita.comicstore.util.CustomAlert;
import io.github.kevinpita.comicstore.view.CollectionController;
import io.github.kevinpita.comicstore.view.MainController;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

// comic singleton
@Slf4j
public class CollectionService {
    private static BorderPane borderPane;

    public static void setBorderPanel(BorderPane borderPane) {
        CollectionService.borderPane = borderPane;
    }

    private Gson gson =
            new GsonBuilder()
                    .registerTypeAdapter(
                            LocalDate.class,
                            (JsonDeserializer<LocalDate>)
                                    (json, type, jsonDeserializationContext) -> {
                                        String date = json.getAsString();
                                        return LocalDate.of(
                                                Integer.parseInt(date.split("-")[0]),
                                                Integer.parseInt(date.split("-")[1]),
                                                Integer.parseInt(date.split("-")[2]));
                                    })
                    .create();
    private static CollectionService instance;
    private ObservableList<CollectionDto> collections;
    private ObservableList<Node> nodes;
    private Map<Node, CollectionController> nodeControllerMap = new HashMap<>();

    private CollectionService() {}

    public static CollectionService getInstance() {
        if (instance == null) {
            instance = new CollectionService();
        }
        return instance;
    }

    public static boolean uploadImage(Path imagePath, int id) {
        try {
            final Methanol client = Methanol.create();
            var multipartBody =
                    MultipartBodyPublisher.newBuilder()
                            .filePart("image", imagePath, MediaType.IMAGE_GIF)
                            .build();
            var request =
                    MutableRequest.POST(
                                    UrlPath.UPLOAD_COLLECTION_IMAGE.getUrl() + "/" + id,
                                    multipartBody)
                            .header("Authorization", Configuration.getAuthToken());

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return true;
            }

        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return false;
    }
    // SUBIR IMAGEN
    public static int createCollection(
            String name, String publisher, String description, Path image) {
        String url = UrlPath.COLLECTION.getUrl();
        String password = Configuration.getAuthToken();

        HttpClient client = HttpClient.newHttpClient();
        CollectionDto dto =
                CollectionDto.builder()
                        .name(name)
                        .publisher(publisher)
                        .description(description)
                        .build();
        String json = new Gson().toJson(dto);
        try {
            HttpRequest request =
                    HttpRequest.newBuilder()
                            .POST(HttpRequest.BodyPublishers.ofString(json))
                            .timeout(Duration.ofSeconds(3))
                            .header("Authorization", password)
                            .header("Content-Type", "application/json")
                            .uri(URI.create(url))
                            .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 409) {
                return 0;
            }
            if (response.statusCode() != 201) {
                return 1;
            }

            String b = response.body();
            int id = getInstance().getGson().fromJson(b, NewCollectionDto.class).getData().getId();
            uploadImage(image, id);
            return 2;
        } catch (Exception ignored) {
            log.error(ExceptionUtils.getStackTrace(ignored));
            CustomAlert.showConnectingAlert();
        }
        return 1;
    }

    public static int updateCollection(
            int id, String name, String publisher, String description, Path image) {
        String url = UrlPath.COLLECTION.getUrl() + "/" + id;
        String password = Configuration.getAuthToken();

        HttpClient client = HttpClient.newHttpClient();
        CollectionDto dto =
                CollectionDto.builder()
                        .name(name)
                        .publisher(publisher)
                        .description(description)
                        .build();
        String json = new Gson().toJson(dto);
        try {
            HttpRequest request =
                    HttpRequest.newBuilder()
                            .PUT(HttpRequest.BodyPublishers.ofString(json))
                            .timeout(Duration.ofSeconds(3))
                            .header("Authorization", password)
                            .header("Content-Type", "application/json")
                            .uri(URI.create(url))
                            .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 409) {
                return 0;
            }
            if (response.statusCode() != 201) {
                return 1;
            }
            AuthorService.getInstance().getAuthors();
            if (image != null) {
                uploadImage(image, id);
            }
            return 2;
        } catch (Exception ignored) {
            log.error(ExceptionUtils.getStackTrace(ignored));
            CustomAlert.showConnectingAlert();
        }
        return 1;
    }

    public static boolean deletePicture(int id) {
        String url = UrlPath.COLLECTION_IMAGE.getUrl() + "/" + id;
        String password = Configuration.getAuthToken();

        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpRequest request =
                    HttpRequest.newBuilder()
                            .DELETE()
                            .timeout(Duration.ofSeconds(3))
                            .header("Authorization", password)
                            .uri(URI.create(url))
                            .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.statusCode() == 200;
        } catch (Exception ignored) {
            log.error(ExceptionUtils.getStackTrace(ignored));
            CustomAlert.showConnectingAlert();
        }
        return false;
    }

    public static boolean deleteCollection(int id) {
        String url = UrlPath.COLLECTION.getUrl() + "/" + id;
        String password = Configuration.getAuthToken();

        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpRequest request =
                    HttpRequest.newBuilder()
                            .DELETE()
                            .timeout(Duration.ofSeconds(3))
                            .header("Authorization", password)
                            .uri(URI.create(url))
                            .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                return false;
            }
            deletePicture(id);
            return true;
        } catch (Exception ignored) {
            log.error(ExceptionUtils.getStackTrace(ignored));
            CustomAlert.showConnectingAlert();
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
        getNodeMap().clear();
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
        String password = Configuration.getAuthToken();

        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpRequest request =
                    HttpRequest.newBuilder()
                            .timeout(Duration.ofSeconds(3))
                            .uri(URI.create(url))
                            .header("Authorization", password)
                            .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                return;
            }
            collections.clear();
            collections.setAll(gson.fromJson(response.body(), CollectionListDto.class).getData());
        } catch (Exception ignored) {
            log.error(ExceptionUtils.getStackTrace(ignored));
            CustomAlert.showConnectingAlert();
        }
    }

    public Gson getGson() {
        return gson;
    }

    public Map<Node, CollectionController> getNodeMap() {
        return nodeControllerMap;
    }
}
