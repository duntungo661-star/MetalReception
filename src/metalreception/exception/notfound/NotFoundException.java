package metalreception.exception.notfound;

import metalreception.exception.MetalReceptionException;

public class NotFoundException extends MetalReceptionException {
    public NotFoundException(String message) {
        super(message);
    }
}
