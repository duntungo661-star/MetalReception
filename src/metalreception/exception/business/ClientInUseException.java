package metalreception.exception.business;

public class ClientInUseException extends EntityInUseException {
    public ClientInUseException(String message) {
        super(message);
    }
}
