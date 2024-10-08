package Exceptions;

import java.util.Optional;

public class PileOutOfCardsException extends Exception{
    static final String message = "Pile is out of cards";
    public PileOutOfCardsException(String message) {
        super(message );
    }

    public PileOutOfCardsException() {
        super(message);
    }
}
