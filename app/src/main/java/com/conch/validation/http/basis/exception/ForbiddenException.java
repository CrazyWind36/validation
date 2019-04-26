package com.conch.validation.http.basis.exception;



import com.conch.validation.http.basis.config.HttpCode;
import com.conch.validation.http.basis.exception.base.BaseException;

public class ForbiddenException extends BaseException {

    public ForbiddenException() {
        super(HttpCode.CODE_PARAMETER_INVALID, "404错误");
    }

}