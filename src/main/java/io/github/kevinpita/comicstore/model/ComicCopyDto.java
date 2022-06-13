/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class ComicCopyDto implements Serializable {
    private Long id;
    private LocalDate purchaseDate;
    private String state;
    private BigDecimal price;
    private ComicDto comic;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ComicDto implements Serializable {
        private Long id;
        private String title;
        private String description;
        private int issueNumber;
    }
}
