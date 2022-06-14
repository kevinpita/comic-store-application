/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import io.github.kevinpita.comicstore.configuration.Configuration;
import io.github.kevinpita.comicstore.configuration.UrlPath;
import io.github.kevinpita.comicstore.model.AuthorDto;
import io.github.kevinpita.comicstore.model.AuthorTable;
import io.github.kevinpita.comicstore.model.data.AuthorListDto;
import io.github.kevinpita.comicstore.util.CustomAlert;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

// comic singleton
@Slf4j
public class AuthorService {
    private static AuthorService instance;
    private ObservableList<AuthorDto> authors;
    private ObservableList<AuthorTable> authorsTable;

    private AuthorService() {}

    public static AuthorService getInstance() {
        if (instance == null) {
            instance = new AuthorService();
        }
        return instance;
    }

    public ObservableList<AuthorTable> getAuthorsTable() {
        if (authorsTable == null) {
            authors = FXCollections.observableArrayList();
            authorsTable = FXCollections.observableArrayList();
        }
        fillAuthors();
        return authorsTable;
    }

    public ObservableList<AuthorDto> getAuthors() {
        if (authors == null) {
            authors = FXCollections.observableArrayList();
            authorsTable = FXCollections.observableArrayList();
        }
        fillAuthors();

        return authors;
    }

    private void fillAuthors() {
        String url = UrlPath.AUTHOR.getUrl();
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
            authors.clear();
            authors.setAll(gson.fromJson(response.body(), AuthorListDto.class).getData());
            authorsTable.clear();
            authorsTable.setAll(dtoToTable(authors));
        } catch (Exception ignored) {
            log.error(ExceptionUtils.getStackTrace(ignored));
            CustomAlert.showConnectingAlert();
            System.exit(1);
        }
    }

    public List<AuthorTable> dtoToTable(List<AuthorDto> authorDtos) {
        List<AuthorTable> authorsTable = new ArrayList<>();

        authors.forEach(
                author -> {
                    SimpleStringProperty name = new SimpleStringProperty(author.getName());
                    SimpleStringProperty lastName = new SimpleStringProperty(author.getLastName());
                    SimpleIntegerProperty createdComicNumber =
                            new SimpleIntegerProperty(author.getCreatedComics());
                    SimpleIntegerProperty id = new SimpleIntegerProperty(author.getId());
                    AuthorTable authorTable =
                            AuthorTable.builder()
                                    .name(name)
                                    .lastName(lastName)
                                    .createdComics(createdComicNumber)
                                    .id(id)
                                    .build();
                    authorsTable.add(authorTable);
                });
        return authorsTable;
    }

    public static boolean createAuthor(String name, String lastName) {
        String url = UrlPath.AUTHOR.getUrl();
        String password = Configuration.getAuthToken();

        HttpClient client = HttpClient.newHttpClient();
        String json = new Gson().toJson(AuthorDto.builder().name(name).lastName(lastName).build());
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

            if (response.statusCode() != 201) {
                return false;
            }
            AuthorService.getInstance().getAuthors();
            return true;
        } catch (Exception ignored) {
            log.error(ExceptionUtils.getStackTrace(ignored));
            CustomAlert.showConnectingAlert();
        }
        return false;
    }
}
