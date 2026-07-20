package main.java.metalreception.exception.business;

import main.java.metalreception.exception.MetalReceptionException;

public class EntityInUseException extends MetalReceptionException {
    public EntityInUseException(String message) {
        super(message);
    }
}
