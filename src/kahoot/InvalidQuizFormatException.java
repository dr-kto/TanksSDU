package kahoot;

public class InvalidQuizFormatException extends Exception {
    public InvalidQuizFormatException(String errorMessage) {
        super(errorMessage);
    }
}