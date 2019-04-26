package com.conch.validation.http.basis.callback;


import com.conch.validation.http.basis.exception.base.BaseException;

public interface RequestMultiplyCallback<T> extends RequestCallback<T> {

    void onFail(BaseException e);

}