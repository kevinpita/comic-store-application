/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComicAuthorDto implements Serializable {
    private int id;
    private AuthorDto creator;
    private String role;
}