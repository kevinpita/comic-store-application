/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.model.table;

import javafx.beans.property.SimpleStringProperty;

public class ComicAuthorTable {
    private final SimpleStringProperty role;
    private final SimpleStringProperty name;

    public ComicAuthorTable(String role, String fullName) {
        this.role = new SimpleStringProperty(role);
        this.name = new SimpleStringProperty(fullName);
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
}
