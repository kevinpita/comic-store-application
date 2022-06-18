/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.configuration;

import io.github.kevinpita.comicstore.util.CustomAlert;
import io.github.kevinpita.comicstore.util.i18n;
import java.io.*;
import java.util.Locale;
import java.util.Properties;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

@Slf4j
public class Configuration {
    @Setter private static String apiUrl = "http://localhost:8383/";

    @Getter @Setter private static String authToken = "auth_token_env_compose";

    private static String language = "es";

    // return server url without trailing slash
    public static String getApiUrl() {
        String url = apiUrl;
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }

    // get current Locale language
    public static Locale getLanguage() {
        if (language.equals("gl")) {
            return new Locale("gl", "ES");
        } else {
            return new Locale("es", "ES");
        }
    }

    // set language variable from Locale
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

            apiUrl = prop.getProperty("apiUrl", apiUrl);
            authToken = prop.getProperty("authToken", authToken);
            language = prop.getProperty("language", language);
        } catch (FileNotFoundException ignored) {
            // if file not found, use default values
            writeConfiguration();
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            CustomAlert.showAlert(i18n.getString("configurationFileError"));
        }
    }

    public static void writeConfiguration() {
        Properties prop = new Properties();

        prop.setProperty("apiUrl", apiUrl);
        prop.setProperty("authToken", authToken);
        prop.setProperty("language", language);

        try (OutputStream output = new FileOutputStream("./configuration.properties")) {
            prop.store(output, null);
        } catch (Exception logged) {
            log.error(ExceptionUtils.getStackTrace(logged));
        }
    }
}
