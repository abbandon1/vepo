package exception;

public class TourServiceValidationException extends RuntimeException {
    public TourServiceValidationException(String message) {
        super(message);
    }
}