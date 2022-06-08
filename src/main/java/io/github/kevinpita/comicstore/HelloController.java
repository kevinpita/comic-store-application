/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore;

import java.net.URL;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.JButton;

public class HelloController {
    private final JButton a = new JButton();

    public HelloController() {
        try {
            URL helpURL;
            helpURL = this.getClass().getResource("/SpanishHelp/help.hs");
            HelpSet helpset = new HelpSet(null, helpURL);
            HelpBroker browser = helpset.createHelpBroker();
            browser.enableHelpOnButton(a, "principal", helpset);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        a.doClick();

        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
