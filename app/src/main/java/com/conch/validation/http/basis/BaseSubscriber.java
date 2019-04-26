package com.conch.validation.http.basis;

import android.widget.Toast;


import com.conch.validation.http.basis.callback.RequestCallback;
import com.conch.validation.http.basis.callback.RequestMultiplyCallback;
import com.conch.validation.http.basis.config.HttpCode;
import com.conch.validation.http.basis.exception.base.BaseException;
import com.conch.validation.http.holder.ContextHolder;
import com.conch.validation.http.viewmodel.base.BaseViewModel;

import io.reactivex.observers.DisposableObserver;


public class BaseSubscriber<T> extends DisposableObserver<T> {

    private BaseViewModel baseViewModel;

    private RequestCallback<T> requestCallback;

    public BaseSubscriber(BaseViewModel baseViewModel) {
        this.baseViewModel = baseViewModel;
    }

    BaseSubscriber(BaseViewModel baseViewModel, RequestCallback<T> requestCallback) {
        this.baseViewModel = baseViewModel;
        this.requestCallback = requestCallback;
    }

    @Override
    public void onNext(T t) {
        if (requestCallback != null) {
            requestCallback.onSuccess(t);
        }
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (requestCallback instanceof RequestMultiplyCallback) {
            RequestMultiplyCallback callback = (RequestMultiplyCallback) requestCallback;
            if (e instanceof BaseException) {
                callback.onFail((BaseException) e);
            } else {
                callback.onFail(new BaseException(HttpCode.CODE_UNKNOWN, e.getMessage()));
            }
        } else {
            if (baseViewModel == null) {
                Toast.makeText(ContextHolder.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                baseViewModel.showToast(e.getMessage());
            }
        }
    }

    @Override
    public void onComplete() {

    }

}