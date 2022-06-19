/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReturnStatus {
    SUCCESS(0),
    DUPLICATED(1),
    NOT_FOUND(2),
    ERROR(3);

    private final int CODE;
}
