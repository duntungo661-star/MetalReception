package main.java.metalreception.exception.notfound;

import main.java.metalreception.exception.MetalReceptionException;

public class NotFoundException extends MetalReceptionException {
    public NotFoundException(String message) {
        super(message);
    }
}
