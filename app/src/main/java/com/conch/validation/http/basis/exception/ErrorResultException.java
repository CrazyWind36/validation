package com.conch.validation.http.basis.exception;


import com.conch.validation.http.basis.exception.base.BaseException;

/**
 * 服务器提示请求结果为失败
 * 返回 失败提示
 * @author Administrator
 */
public class ErrorResultException extends BaseException{


    public ErrorResultException(int code,String body) {
        super(code,body);
    }


}
