/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.model.data;

import io.github.kevinpita.comicstore.model.CollectionDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewCollectionDto extends DataDto {
    private CollectionDto data;
}
