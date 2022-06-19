/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.util;

import com.github.mizosoft.methanol.MediaType;
import com.github.mizosoft.methanol.MultipartBodyPublisher;
import com.github.mizosoft.methanol.MutableRequest;
import com.google.gson.*;
import io.github.kevinpita.comicstore.configuration.Configuration;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RequestUtil {
    private static final Gson gson =
            new GsonBuilder()
                    .registerTypeAdapter(
                            LocalDate.class,
                            (JsonDeserializer<LocalDate>)
                                    (json, ignored, ignoredContext) -> {
                                        String date = json.getAsString();
                                        return LocalDate.of(
                                                Integer.parseInt(date.split("-")[0]),
                                                Integer.parseInt(date.split("-")[1]),
                                                Integer.parseInt(date.split("-")[2]));
                                    })
                    .registerTypeAdapter(
                            LocalDate.class,
                            (JsonSerializer<LocalDate>)
                                    (date, ignored, ignoredContext) ->
                                            new JsonPrimitive(
                                                    date.format(
                                                            DateTimeFormatter.ofPattern(
                                                                    "yyyy-MM-dd"))))
                    .create();

    public static HttpRequest.Builder createRequest(String url) {
        String password = Configuration.getAuthToken();
        return HttpRequest.newBuilder()
                .timeout(Duration.ofSeconds(3))
                .uri(URI.create(url))
                .header("Authorization", password);
    }

    public static Gson getGson() {
        return gson;
    }

    public static MutableRequest getMutableRequest(
            String httpPath, int id, MultipartBodyPublisher multipartBody) {
        return MutableRequest.POST(httpPath + "/" + id, multipartBody)
                .header("Authorization", Configuration.getAuthToken());
    }

    public static MultipartBodyPublisher getMultipartBodyPublisher(Path imagePath)
            throws FileNotFoundException {
        return MultipartBodyPublisher.newBuilder()
                .filePart("image", imagePath, MediaType.IMAGE_PNG)
                .build();
    }
}
