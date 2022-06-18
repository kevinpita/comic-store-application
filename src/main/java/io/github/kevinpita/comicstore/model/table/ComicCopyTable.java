/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.model.table;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleStringProperty;

public class ComicCopyTable {
    private final SimpleStringProperty cover;
    private final SimpleStringProperty state;
    private final SimpleStringProperty price;
    private final SimpleStringProperty purchase;

    public ComicCopyTable(String cover, String state, Double price, LocalDate purchase) {
        this.cover = new SimpleStringProperty(cover);
        this.state = new SimpleStringProperty(state);
        this.price = new SimpleStringProperty(price + "€");
        this.purchase =
                new SimpleStringProperty(
                        purchase.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    public String getCover() {
        return cover.get();
    }

    public SimpleStringProperty coverProperty() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover.set(cover);
    }

    public String getState() {
        return state.get();
    }

    public SimpleStringProperty stateProperty() {
        return state;
    }

    public void setState(String state) {
        this.state.set(state);
    }

    public String getPrice() {
        return price.get();
    }

    public SimpleStringProperty priceProperty() {
        return price;
    }

    public void setPrice(String price) {
        this.price.set(price);
    }

    public String getPurchase() {
        return purchase.get();
    }

    public SimpleStringProperty purchaseProperty() {
        return purchase;
    }

    public void setPurchase(String purchase) {
        this.purchase.set(purchase);
    }
}
