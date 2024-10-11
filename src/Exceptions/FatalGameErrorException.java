package Exceptions;

public class FatalGameErrorException extends RuntimeException {
    private static final String message = "A fatal error has occurred in the game.";
    public FatalGameErrorException(String message) {
        super(message);
    }
    public FatalGameErrorException() {
        super(message);
    }
}
