package io.github.kevinpita.comicstore.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
public class AuthorTable {
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
}
