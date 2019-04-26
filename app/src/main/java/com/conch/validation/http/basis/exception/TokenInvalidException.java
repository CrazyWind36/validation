package com.conch.validation.http.basis.exception;


import com.conch.validation.http.basis.config.HttpCode;
import com.conch.validation.http.basis.exception.base.BaseException;

public class TokenInvalidException extends BaseException {

    public TokenInvalidException() {
        super(HttpCode.CODE_TOKEN_INVALID, "Token失效");
    }

}
