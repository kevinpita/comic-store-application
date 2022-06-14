/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import io.github.kevinpita.comicstore.configuration.Configuration;
import io.github.kevinpita.comicstore.configuration.UrlPath;
import io.github.kevinpita.comicstore.model.CollectionDto;
import io.github.kevinpita.comicstore.model.data.CollectionListDto;
import io.github.kevinpita.comicstore.util.CustomAlert;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

// comic singleton
@Slf4j
public class CollectionService {
    private static CollectionService instance;
    @Getter private List<CollectionDto> collections = new ArrayList<>();

    private CollectionService() {}

    public static CollectionService getInstance() {
        if (instance == null) {
            instance = new CollectionService();
        }
        return instance;
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

            Gson gson =
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
            if (response.statusCode() != 200) {
                return;
            }
            collections = gson.fromJson(response.body(), CollectionListDto.class).getData();
        } catch (Exception ignored) {
            log.error(ExceptionUtils.getStackTrace(ignored));
            CustomAlert.showConnectingAlert();
            System.exit(1);
        }
    }
}
