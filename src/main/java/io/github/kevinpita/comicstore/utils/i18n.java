/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.utils;

import java.util.ResourceBundle;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class i18n {

    private final String resourceBundlePath = "io/github/kevinpita/comicstore/languages/bundle";
    private static i18n instance;
    private final ObjectProperty<ResourceBundle> resources = new SimpleObjectProperty<>();

    static {
        instance = getInstance();
        instance.resources.set(ResourceBundle.getBundle(instance.resourceBundlePath));
    }

    private i18n() {}

    public static i18n getInstance() {
        if (instance == null) {
            instance = new i18n();
        }
        return instance;
    }

    public static ResourceBundle getResourceBundle() {
        return getInstance().resources.get();
    }

    public static StringBinding getStringBinding(String key) {
        return new StringBinding() {
            {
                bind(getInstance().resources);
            }

            @Override
            public String computeValue() {
                return getInstance().resources.get().getString(key);
            }
        };
    }

    public static String getString(String key) {
        return getInstance().resources.get().getString(key);
    }

    public static void update() {
        getInstance()
                .resources
                .set(ResourceBundle.getBundle("io/github/kevinpita/comicstore/languages/bundle"));
    }
}
