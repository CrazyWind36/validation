package com.conch.validation.http.basis.exception;


import com.conch.validation.http.basis.config.HttpCode;
import com.conch.validation.http.basis.exception.base.BaseException;

public class ParamterInvalidException extends BaseException {

    public ParamterInvalidException() {
        super(HttpCode.CODE_PARAMETER_INVALID, "参数有误");
    }

}
