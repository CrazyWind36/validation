package com.conch.validation.http.basis.exception;


import com.conch.validation.http.basis.config.HttpCode;
import com.conch.validation.http.basis.exception.base.BaseException;

public class ResultInvalidException extends BaseException {

    public ResultInvalidException() {
        super(HttpCode.CODE_RESULT_INVALID, "无效请求");
    }

}
