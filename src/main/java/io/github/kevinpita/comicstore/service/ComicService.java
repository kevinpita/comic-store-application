/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import io.github.kevinpita.comicstore.configuration.Configuration;
import io.github.kevinpita.comicstore.configuration.UrlPath;
import io.github.kevinpita.comicstore.model.ComicDto;
import io.github.kevinpita.comicstore.model.DataDto;
import lombok.Getter;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;

// comic singleton
public class ComicService {
    private static ComicService instance;
    @Getter
    private List<ComicDto> comics;

    private ComicService() {
    }

    public static ComicService getInstance() {
        if (instance == null) {
            instance = new ComicService();
        }
        return instance;
    }

    public void fillComics() {
        String url = UrlPath.COMIC.getUrl();
        String password = Configuration.getAuthToken();

        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(URI.create(url))
                            .header("Authorization", password)
                            .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println(response.body());

            Gson gson =
                    new GsonBuilder()
                            .registerTypeAdapter(
                                    LocalDate.class,
                                    (JsonDeserializer<LocalDate>) (json, type, jsonDeserializationContext) -> {
                                        String date = json.getAsString();
                                        return LocalDate.of(
                                                Integer.parseInt(date.split("-")[0]),
                                                Integer.parseInt(date.split("-")[1]),
                                                Integer.parseInt(date.split("-")[2]));
                                    })
                            .create();
            comics = gson.fromJson(response.body(), DataDto.class).getData();
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }
}
