package com.postsquad.scoup.web.group.exception;

public class GroupNotFoundException extends RuntimeException {

    public GroupNotFoundException(Long groupId) {
        super("Group with id '" + groupId + "' does not exist");
    }
}
