/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.service;

import com.google.gson.Gson;
import io.github.kevinpita.comicstore.configuration.UrlPath;
import io.github.kevinpita.comicstore.model.AuthorDto;
import io.github.kevinpita.comicstore.model.data.AuthorListDto;
import io.github.kevinpita.comicstore.model.table.AuthorTable;
import io.github.kevinpita.comicstore.util.CustomAlert;
import io.github.kevinpita.comicstore.util.RequestUtil;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

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

        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpRequest request = RequestUtil.createRequest(url).build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            Gson gson = RequestUtil.getGson();

            if (response.statusCode() != 200) {
                return;
            }

            authors.clear();
            authors.setAll(gson.fromJson(response.body(), AuthorListDto.class).getData());

            authorsTable.clear();
            authorsTable.setAll(dtoToTable());
        } catch (Exception logged) {
            log.error(ExceptionUtils.getStackTrace(logged));
            CustomAlert.showConnectingAlert(null);
        }
    }

    public List<AuthorTable> dtoToTable() {
        List<AuthorTable> authorsTable = new ArrayList<>();

        authors.forEach(
                author -> {
                    var name = new SimpleStringProperty(author.getName());
                    var lastName = new SimpleStringProperty(author.getLastName());
                    var createdComicNumber = new SimpleIntegerProperty(author.getCreatedComics());
                    var id = new SimpleIntegerProperty(author.getId());

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

        HttpClient client = HttpClient.newHttpClient();
        String json = RequestUtil.getGson().toJson(getAuthorDto(name, lastName));
        try {
            HttpRequest request =
                    RequestUtil.createRequest(url)
                            .POST(HttpRequest.BodyPublishers.ofString(json))
                            .header("Content-Type", "application/json")
                            .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 201) {
                return false;
            }
            AuthorService.getInstance().getAuthors();
            return true;
        } catch (Exception logged) {
            log.error(ExceptionUtils.getStackTrace(logged));
            // TODO CONNECTING ALERT
            CustomAlert.showConnectingAlert(null);
        }
        return false;
    }

    public static boolean updateAuthor(int id, String name, String lastName) {
        String url = UrlPath.AUTHOR.getUrl() + "/" + id;

        HttpClient client = HttpClient.newHttpClient();

        String json = RequestUtil.getGson().toJson(getAuthorDto(name, lastName));
        try {
            HttpRequest request =
                    RequestUtil.createRequest(url)
                            .PUT(HttpRequest.BodyPublishers.ofString(json))
                            .header("Content-Type", "application/json")
                            .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 201) {
                return false;
            }
            AuthorService.getInstance().getAuthors();
            return true;
        } catch (Exception logged) {
            log.error(ExceptionUtils.getStackTrace(logged));
            CustomAlert.showConnectingAlert(null);
        }
        return false;
    }

    private static AuthorDto getAuthorDto(String name, String lastName) {
        return AuthorDto.builder().name(name).lastName(lastName).build();
    }

    public static boolean deleteAuthor(int id) {
        String url = UrlPath.AUTHOR.getUrl() + "/" + id;

        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpRequest request = RequestUtil.createRequest(url).DELETE().build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                return false;
            }
            AuthorService.getInstance().getAuthors();
            return true;
        } catch (Exception logged) {
            log.error(ExceptionUtils.getStackTrace(logged));
            CustomAlert.showConnectingAlert(null);
        }
        return false;
    }
}
