package com.cnsbd.pms.exceptionhandler;

public class UsernameNotAvailableException extends RuntimeException {
    public UsernameNotAvailableException(String msg) {
        super(msg);
    }
}
