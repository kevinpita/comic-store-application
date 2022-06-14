/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDto implements Serializable {
    private int id;
    private String name;
    private String lastName;
    private int createdComics;

    @Override
    public String toString() {
        String stringResult = name + " " + lastName;
        if (stringResult.length() > 50) {
            stringResult = stringResult.substring(0, 50).strip() + "...";
        }

        return stringResult;
    }
}
