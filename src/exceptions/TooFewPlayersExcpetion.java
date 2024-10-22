package exceptions;

public class TooFewPlayersExcpetion extends RuntimeException {
    private static final String message = "Too few players to start the game";
    public TooFewPlayersExcpetion(String message) {
        super(message);
    }

    public TooFewPlayersExcpetion() {
        super(message);
    }
}
