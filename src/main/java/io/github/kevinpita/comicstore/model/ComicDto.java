/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import io.github.kevinpita.comicstore.configuration.UrlPath;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComicDto implements Serializable {
    private int id;
    private String title;
    private String description;
    private int issueNumber;
    @Getter(AccessLevel.NONE)
    private String imageUrl;
    private CollectionDto collection;
    private List<ComicCreatorDto> comicCreators;
    private List<ComicCopyDto> copies;

    public String getFullTitle() {
        return title + " #" + issueNumber;
    }
    public String getImageUrl() {
        return UrlPath.COMIC_IMAGE.getUrl() + "/" + id;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CollectionDto implements Serializable {
        private int id;
        private String name;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ComicCopyDto implements Serializable {
        private Long id;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonSerialize(using = LocalDateSerializer.class)
        private LocalDate purchaseDate;

        private String state;
        private Double price;
    }
}
