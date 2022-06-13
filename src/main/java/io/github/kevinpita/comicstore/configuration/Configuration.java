/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.configuration;

import java.io.*;
import java.util.Locale;
import java.util.Properties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class Configuration {
    @Getter(AccessLevel.NONE)
    @Setter
    private static String apiUrl = "http://localhost:8383/";

    @Getter @Setter private static String authToken = "auth_token_env_compose";

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private static String language = "es";

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private static Configuration instance;

    private Configuration() {}

    public static String getApiUrl() {
        String url = apiUrl;
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }

    public static Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }

    public static Locale getLanguage() {
        if (language.equals("gl")) {
            return new Locale("gl", "ES");
        } else {
            return new Locale("es", "ES");
        }
    }

    public static void setLanguage(Locale language) {
        if (language.getLanguage().equals("gl")) {
            Configuration.language = "gl";
        } else {
            Configuration.language = "es";
        }
    }
    // read configuration.properties
    public static void readConfiguration() {
        try (InputStream input = new FileInputStream("./configuration.properties")) {
            Properties prop = new Properties();
            prop.load(input);

            apiUrl = prop.getProperty("apiUrl");
            authToken = prop.getProperty("authToken");
            language = prop.getProperty("language");
        } catch (FileNotFoundException ignored) {
            writeConfiguration();
        } catch (Exception e) {
        }
    }

    public static void writeConfiguration() {
        Properties prop = new Properties();
        prop.setProperty("apiUrl", apiUrl);
        prop.setProperty("authToken", authToken);
        prop.setProperty("language", language);
        try (OutputStream output = new FileOutputStream("./configuration.properties")) {
            prop.store(output, null);
        } catch (Exception ignored) {
        }
    }
}
