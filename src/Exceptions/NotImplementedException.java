package Exceptions;

public class NotImplementedException extends RuntimeException {
  private static final String message = "Functionality not implemented";
    public NotImplementedException(String message) {
        super(message);
    }

    public NotImplementedException() {
        super(message);
    }
}
