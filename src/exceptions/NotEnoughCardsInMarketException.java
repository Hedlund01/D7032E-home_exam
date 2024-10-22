package exceptions;

public class NotEnoughCardsInMarketException extends Exception {
    public NotEnoughCardsInMarketException(String message) {
        super(message);
    }
}
