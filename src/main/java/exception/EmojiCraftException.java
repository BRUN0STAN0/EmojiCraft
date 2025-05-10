
package exception;

public class EmojiCraftException extends RuntimeException {
    public EmojiCraftException(String message) {
        super(message);
    }

    public EmojiCraftException(String message, Throwable cause) {
        super(message, cause);
    }
}
