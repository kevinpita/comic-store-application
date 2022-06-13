/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataDto {
    private List<ComicDto> data;
    private boolean error;
    private String message;
}
