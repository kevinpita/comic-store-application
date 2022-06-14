/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.model.data;

import io.github.kevinpita.comicstore.model.CreatorDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorListDto extends DataDto {
    private List<CreatorDto> data;
}
