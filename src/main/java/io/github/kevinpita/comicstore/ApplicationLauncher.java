/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore;

import io.github.kevinpita.comicstore.configuration.Configuration;
import io.github.kevinpita.comicstore.view.MainWindow;
import java.util.Locale;

public class ApplicationLauncher {
    public static void main(String[] args) {
        Configuration.readConfiguration();
        Locale.setDefault(Configuration.getLanguage());
        MainWindow.main(args);
    }
}
