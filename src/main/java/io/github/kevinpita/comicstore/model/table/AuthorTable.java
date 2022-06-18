/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.model.table;

import io.github.kevinpita.comicstore.model.AuthorDto;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Builder;

@Builder
public class AuthorTable {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty name;
    private final SimpleStringProperty lastName;
    private final SimpleIntegerProperty createdComics;

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getLastName() {
        return lastName.get();
    }

    public SimpleStringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public int getCreatedComics() {
        return createdComics.get();
    }

    public SimpleIntegerProperty createdComicsProperty() {
        return createdComics;
    }

    public void setCreatedComics(int createdComics) {
        this.createdComics.set(createdComics);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public AuthorDto getDto() {
        return AuthorDto.builder()
                .id(id.get())
                .name(name.get())
                .lastName(lastName.get())
                .createdComics(createdComics.get())
                .build();
    }
}
