/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.model;

import io.github.kevinpita.comicstore.configuration.UrlPath;
import java.io.Serializable;
import java.util.List;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CollectionDto implements Serializable {
    private Long id;
    private String name;
    private String description;
    private String publisher;
    private List<ComicDto> comics;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private String imageUrl;

    public String getImageUrl() {
        return UrlPath.COLLECTION_IMAGE.getUrl() + "/" + id;
    }
}
