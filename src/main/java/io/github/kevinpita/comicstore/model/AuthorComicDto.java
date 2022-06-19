/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.model;

import io.github.kevinpita.comicstore.model.table.AuthorComicTable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorComicDto implements Serializable {
    private int id;
    private AuthorDto creator;
    private String role;

    public static AuthorComicDto fromTable(AuthorComicTable authorComicTable) {
        return AuthorComicDto.builder()
                .id(authorComicTable.getId())
                .creator(authorComicTable.getCreator())
                .role(authorComicTable.getRole())
                .build();
    }
}
