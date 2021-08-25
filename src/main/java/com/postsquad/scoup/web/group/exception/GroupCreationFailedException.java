package com.postsquad.scoup.web.group.exception;

public class GroupCreationFailedException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Failed to create group";
    private String description;

    public GroupCreationFailedException(String message) {
        super(DEFAULT_MESSAGE);
        description = message;
    }

    public GroupCreationFailedException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }

    public GroupCreationFailedException(String message, Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
        description = message;
    }

    public String getDescription() {
        return description;
    }
}
