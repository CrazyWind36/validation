package com.conch.validation.http.viewmodel.base;


import android.arch.lifecycle.MutableLiveData;

import com.conch.validation.http.event.BaseActionEvent;

public interface IViewModelAction {

    void startLoading();

    void startLoading(String message);

    void dismissLoading();

    void showToast(String message);

    void finish();

    void finishWithResultOk();

    MutableLiveData<BaseActionEvent> getActionLiveData();

}