package exceptions;

public class TooManyPlayerException extends RuntimeException {
    private static final String message = "Too many players to start the game";
    public TooManyPlayerException(String message) {
        super(message);
    }

    public TooManyPlayerException() {
        super(message);
    }
}
