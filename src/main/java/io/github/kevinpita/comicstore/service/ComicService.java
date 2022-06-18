/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.service;

import com.google.gson.Gson;
import io.github.kevinpita.comicstore.configuration.UrlPath;
import io.github.kevinpita.comicstore.model.ComicDto;
import io.github.kevinpita.comicstore.model.ComicTable;
import io.github.kevinpita.comicstore.model.data.ComicListDto;
import io.github.kevinpita.comicstore.util.CustomAlert;
import io.github.kevinpita.comicstore.util.RequestUtil;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

// comic singleton
@Slf4j
public class ComicService {
    private static ComicService instance;
    private ObservableList<ComicDto> comics;

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
            CustomAlert.showConnectingAlert();
        }
    }
}
