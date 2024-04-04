package com.projects.bookstore.common.exceptions;

public class ObjectNotFoundException extends RuntimeException {

    public ObjectNotFoundException(String objectId) {
        super("Object not found with ID: " + objectId);
    }

    public ObjectNotFoundException(String objectId, Throwable cause) {
        super("Object not found with ID: " + objectId, cause);
    }
}
