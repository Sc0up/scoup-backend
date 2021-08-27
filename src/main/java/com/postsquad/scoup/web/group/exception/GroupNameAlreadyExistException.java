package com.postsquad.scoup.web.group.exception;

public class GroupNameAlreadyExistException extends GroupCreationFailedException {

    public GroupNameAlreadyExistException(String name) {
        super("Group name '" + name + "' already exists");
    }
}
