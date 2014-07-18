package util;

/**
 * Mime type enum
 *
* Created by oleksii on 18/07/14.
*/
public enum Mime {
    JSON("application/json");

    private String mimeType;

    Mime(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return mimeType;
    }
}
