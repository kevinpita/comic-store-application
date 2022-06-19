/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.view.create;

import io.github.kevinpita.comicstore.model.table.ComicCopyTable;
import io.github.kevinpita.comicstore.util.CustomAlert;
import io.github.kevinpita.comicstore.util.i18n;
import java.time.LocalDate;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.util.StringConverter;
import lombok.Setter;

public class ComicCopyData {
    @FXML private TextField inputCreateState;
    @FXML private TextField inputCreateCover;
    @FXML private AnchorPane parentAnchorPane;
    @FXML private DatePicker datePicker;
    @FXML private Button saveButton;
    @FXML private TextField inputCreatePrice;

    @Setter private ComicCopyTable comicCopyTable;
    @Setter private ObservableList<ComicCopyTable> comicCopyTableObservableList;

    @FXML
    private void initialize() {
        setDoubleTextField();
    }

    public void lateInit() {
        if (comicCopyTable == null) {
            datePicker.setValue(LocalDate.now());
            inputCreatePrice.setText("10.00");

            return;
        }
        datePicker.setValue(comicCopyTable.getPurchase());
        inputCreateState.setText(comicCopyTable.getState());
        inputCreateCover.setText(comicCopyTable.getCover());
        inputCreatePrice.setText(comicCopyTable.getPrice().replace("€", ""));

        Platform.runLater(() -> parentAnchorPane.requestFocus());
    }

    @FXML
    public void save(ActionEvent actionEvent) {
        LocalDate selectedDate = datePicker.getValue();
        double price = Double.parseDouble(inputCreatePrice.getText());
        String state = inputCreateState.getText().strip();
        String cover = inputCreateCover.getText().strip();

        boolean error = checkFields(state, cover, price, selectedDate);
        if (error) {
            CustomAlert.showAlert(
                    i18n.getString("formError"), inputCreateState.getScene().getWindow());
            return;
        }

        if (comicCopyTable != null) {
            comicCopyTable.setPurchase(selectedDate);
            comicCopyTable.setPrice(price + "");
            comicCopyTable.setState(state);
            comicCopyTable.setCover(cover);
        } else {
            comicCopyTableObservableList.add(
                    new ComicCopyTable(0, cover, state, price, selectedDate));
        }
        CustomAlert.showInfo(
                i18n.getString("newComicCopyAlert"), parentAnchorPane.getScene().getWindow());
        inputCreatePrice.getScene().getWindow().hide();
    }

    private boolean checkFields(String state, String cover, double price, LocalDate selectedDate) {
        boolean error = false;
        if (state.isEmpty() || state.length() > 50) {
            error = true;
            inputCreateState.getStyleClass().add("errorField");
            Tooltip tooltip = new Tooltip(i18n.getString("stateError"));
            tooltip.setFont(new Font(16));
            inputCreateState.setTooltip(tooltip);
        } else {
            inputCreateState.getStyleClass().remove("errorField");
            inputCreateState.setTooltip(null);
        }

        if (cover.isEmpty() || cover.length() > 50) {
            error = true;
            inputCreateCover.getStyleClass().add("errorField");

            Tooltip tooltip = new Tooltip(i18n.getString("coverError"));
            tooltip.setFont(new Font(16));
            inputCreateCover.setTooltip(tooltip);
        } else {
            inputCreateCover.getStyleClass().remove("errorField");
            inputCreateCover.setTooltip(null);
        }

        if (price <= 0) {
            error = true;
            inputCreatePrice.getStyleClass().add("errorField");
            Tooltip tooltip = new Tooltip(i18n.getString("priceError"));
            tooltip.setFont(new Font(16));
            inputCreatePrice.setTooltip(tooltip);
        } else {
            inputCreatePrice.getStyleClass().remove("errorField");
            inputCreatePrice.setTooltip(null);
        }

        if (selectedDate == null || selectedDate.isAfter(LocalDate.now())) {
            error = true;
            datePicker.getStyleClass().add("errorField");
            Tooltip tooltip = new Tooltip(i18n.getString("dateError"));
            tooltip.setFont(new Font(16));
            datePicker.setTooltip(tooltip);
        } else {
            datePicker.getStyleClass().remove("errorField");
            datePicker.setTooltip(null);
        }
        return error;
    }

    private void setDoubleTextField() {
        Pattern validEditingState = Pattern.compile("^(([1-9]\\d*)|0)?(\\.\\d*)?$");

        UnaryOperator<TextFormatter.Change> filter =
                c -> {
                    String text = c.getControlNewText();
                    if (validEditingState.matcher(text).matches()) {
                        return c;
                    } else {
                        return null;
                    }
                };

        StringConverter<Double> converter =
                new StringConverter<>() {

                    @Override
                    public Double fromString(String s) {
                        if (s.isEmpty() || "-".equals(s) || ".".equals(s) || "-.".equals(s)) {
                            return 0.0;
                        } else {
                            return Double.valueOf(s);
                        }
                    }

                    @Override
                    public String toString(Double d) {
                        return d.toString();
                    }
                };

        TextFormatter<Double> textFormatter = new TextFormatter<>(converter, 0.0, filter);
        inputCreatePrice.setTextFormatter(textFormatter);
    }
}
