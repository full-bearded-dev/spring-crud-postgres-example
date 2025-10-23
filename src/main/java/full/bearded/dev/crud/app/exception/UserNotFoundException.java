package full.bearded.dev.crud.app.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(final String message) {

        super(message);
    }
}
