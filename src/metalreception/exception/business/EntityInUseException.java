package metalreception.exception.business;

import metalreception.exception.MetalReceptionException;

public class EntityInUseException extends MetalReceptionException {
    public EntityInUseException(String message) {
        super(message);
    }
}
