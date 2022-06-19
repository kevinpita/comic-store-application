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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuthorDto authorDto = (AuthorDto) o;

        if (id != authorDto.id) return false;
        if (!name.equals(authorDto.name)) return false;
        return lastName.equals(authorDto.lastName);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + lastName.hashCode();
        return result;
    }
}
