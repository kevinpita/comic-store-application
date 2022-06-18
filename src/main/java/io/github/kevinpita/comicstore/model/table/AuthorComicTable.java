/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.model.table;

import io.github.kevinpita.comicstore.model.AuthorComicDto;
import io.github.kevinpita.comicstore.model.AuthorDto;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;

public class AuthorComicTable {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty role;
    private final SimpleStringProperty name;
    @Getter private AuthorDto creator;

    public void setCreator(AuthorDto author) {
        this.creator = author;
        this.name.set(author.toString());
    }

    public AuthorComicTable(long id, String role, AuthorDto creator) {
        this.id = new SimpleIntegerProperty((int) id);
        this.role = new SimpleStringProperty(role);
        this.name = new SimpleStringProperty(creator.toString());
        this.creator = creator;
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

    public String getRole() {
        return role.get();
    }

    public SimpleStringProperty roleProperty() {
        return role;
    }

    public void setRole(String role) {
        this.role.set(role);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public AuthorComicDto toDto(AuthorComicTable authorComicTable) {
        return AuthorComicDto.builder()
                .id(authorComicTable.getId())
                .role(authorComicTable.getRole())
                .creator(creator)
                .build();
    }
}
