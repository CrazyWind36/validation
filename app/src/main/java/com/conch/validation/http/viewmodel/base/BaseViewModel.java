package com.conch.validation.http.viewmodel.base;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.conch.validation.http.event.BaseActionEvent;


/**
 *
 * @author Administrator
 */
public class BaseViewModel extends ViewModel implements IViewModelAction {

    private MutableLiveData<BaseActionEvent> actionLiveData;

    protected LifecycleOwner lifecycleOwner;

    public BaseViewModel() {
        actionLiveData = new MutableLiveData<>();
    }

    @Override
    public void startLoading() {
        startLoading(null);
    }

    @Override
    public void startLoading(String message) {
        BaseActionEvent baseActionEvent = new BaseActionEvent(BaseActionEvent.SHOW_LOADING_DIALOG);
        baseActionEvent.setMessage(message);
        actionLiveData.setValue(baseActionEvent);
    }

    @Override
    public void dismissLoading() {
        actionLiveData.setValue(new BaseActionEvent(BaseActionEvent.DISMISS_LOADING_DIALOG));
    }

    @Override
    public void showToast(String message) {
        BaseActionEvent baseActionEvent = new BaseActionEvent(BaseActionEvent.SHOW_TOAST);
        baseActionEvent.setMessage(message);
        actionLiveData.setValue(baseActionEvent);
    }

    @Override
    public void finish() {
        actionLiveData.setValue(new BaseActionEvent(BaseActionEvent.FINISH));
    }

    @Override
    public void finishWithResultOk() {
        actionLiveData.setValue(new BaseActionEvent(BaseActionEvent.FINISH_WITH_RESULT_OK));
    }

    /**
     * 获取 请求状态的 对象,根据对象进行页面状态的修改
     * @return
     */
    @Override
    public MutableLiveData<BaseActionEvent> getActionLiveData() {
        return actionLiveData;
    }

    void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
    }

}