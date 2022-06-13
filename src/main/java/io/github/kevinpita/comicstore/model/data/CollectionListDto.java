package io.github.kevinpita.comicstore.model.data;

import io.github.kevinpita.comicstore.model.CollectionDto;
import io.github.kevinpita.comicstore.model.ComicDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollectionListDto extends DataDto {
    private List<CollectionDto> data;
}
