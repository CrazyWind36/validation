package com.conch.validation.http.basis.exception;



import com.conch.validation.http.basis.exception.base.BaseException;

public class ServerResultException extends BaseException {

    public ServerResultException(int code, String message) {
        super(code, message);
    }

}
