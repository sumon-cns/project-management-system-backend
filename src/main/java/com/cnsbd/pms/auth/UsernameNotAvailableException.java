package com.cnsbd.pms.auth;

public class UsernameNotAvailableException extends RuntimeException {
    public UsernameNotAvailableException(String msg) {
        super(msg);
    }
}
