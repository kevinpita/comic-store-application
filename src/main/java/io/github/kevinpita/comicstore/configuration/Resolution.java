/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Resolution {
    CONFIGURATION(495, 254),
    AUTHOR(421, 370),
    COLLECTION(840, 840),
    COMIC(1250, 900);

    private final int WIDTH;
    private final int HEIGHT;
}
