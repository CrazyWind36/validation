package com.conch.validation.http.basis.exception;


import com.conch.validation.http.basis.config.HttpCode;
import com.conch.validation.http.basis.exception.base.BaseException;

public class AccountInvalidException extends BaseException {

    public AccountInvalidException() {
        super(HttpCode.CODE_ACCOUNT_INVALID, "账号或者密码错误");
    }

}
