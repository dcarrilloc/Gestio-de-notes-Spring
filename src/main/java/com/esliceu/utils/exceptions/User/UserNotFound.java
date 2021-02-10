package com.esliceu.utils.exceptions.User;

public class UserNotFound extends UserException {
    public UserNotFound() {
        super("User not found. Try again later.");
    }
}
