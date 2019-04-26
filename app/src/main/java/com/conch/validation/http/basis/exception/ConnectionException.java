package com.conch.validation.http.basis.exception;

import com.conch.validation.http.basis.config.HttpCode;
import com.conch.validation.http.basis.exception.base.BaseException;

public class ConnectionException extends BaseException {

    public ConnectionException() {
        super(HttpCode.CODE_CONNECTION_FAILED, "网络请求失败");
    }

}
