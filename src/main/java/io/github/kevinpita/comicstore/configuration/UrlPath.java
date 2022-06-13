/* Kevin Pita 2022 */
package io.github.kevinpita.comicstore.configuration;

public enum UrlPath {
    UPLOAD_COMIC_IMAGE("/images/upload/comic"),
    COMIC_IMAGE("/images/comic"),
    UPLOAD_COLLECTION_IMAGE("/images/upload/collection"),
    COLLECTION_IMAGE("/images/collection"),
    VALIDATION("/api/v1/validation"),
    STATS("/api/v1/stats"),
    COMIC("/api/v1/comics"),
    COLLECTION("/api/v1/collections");

    private final String path;

    UrlPath(String url) {
        this.path = url;
    }

    public String getUrl() {
        return Configuration.getApiUrl() + path;
    }

    public String getPath() {
        return path;
    }
}
