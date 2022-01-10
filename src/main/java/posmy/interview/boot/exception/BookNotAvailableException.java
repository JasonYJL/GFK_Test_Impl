package posmy.interview.boot.exception;

public class BookNotAvailableException extends Exception{
    public BookNotAvailableException(String message) {
        super(message);
    }
}
