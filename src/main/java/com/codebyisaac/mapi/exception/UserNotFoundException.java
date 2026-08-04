package com.codebyisaac.mapi.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String id) {
        super ("User not found" + id);
    }
}