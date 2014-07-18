package suggestions;

/**
 * Created by oleksii on 18/07/14.
 */
@SuppressWarnings("UnusedDeclaration")
public class PositionSuggestionException extends Exception {
    public PositionSuggestionException() {
    }

    public PositionSuggestionException(String message) {
        super(message);
    }

    public PositionSuggestionException(String message, Throwable cause) {
        super(message, cause);
    }

    public PositionSuggestionException(Throwable cause) {
        super(cause);
    }

    public PositionSuggestionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
