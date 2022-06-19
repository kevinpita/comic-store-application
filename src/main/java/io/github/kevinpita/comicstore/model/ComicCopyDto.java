/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.model;

import io.github.kevinpita.comicstore.model.table.ComicCopyTable;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComicCopyDto implements Serializable {
    private Long id;
    private LocalDate purchaseDate;
    private String state;
    private Double price;
    private String cover;

    public static ComicCopyDto fromTable(ComicCopyTable comicCopyTable) {
        return ComicCopyDto.builder()
                .id((long) comicCopyTable.getId())
                .purchaseDate(comicCopyTable.getPurchase())
                .state(comicCopyTable.getState())
                .cover(comicCopyTable.getCover())
                .price(Double.parseDouble(comicCopyTable.getPrice().replace("€", "")))
                .build();
    }
}
