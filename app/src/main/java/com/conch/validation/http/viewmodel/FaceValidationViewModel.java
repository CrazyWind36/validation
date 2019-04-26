package com.conch.validation.http.viewmodel;

import android.arch.lifecycle.MutableLiveData;


import com.conch.validation.http.basis.callback.RequestMultiplyCallback;
import com.conch.validation.http.basis.exception.base.BaseException;
import com.conch.validation.http.datasource.FaceValidationDataSource;
import com.conch.validation.http.repo.FaceValidationRepo;
import com.conch.validation.http.viewmodel.base.BaseViewModel;
import com.conch.validation.view.camera.PreviewSize;

import okhttp3.RequestBody;


/**
 * 后台检测人脸
 *
 * @author Administrator
 */
public class FaceValidationViewModel extends BaseViewModel {

    private MutableLiveData<String> validationOkMutableLiveData;
    private MutableLiveData<BaseException> validationErrorMutableLiveData;

    private FaceValidationRepo faceValidationRepo;

    public FaceValidationViewModel() {
        validationOkMutableLiveData = new MutableLiveData<>();
        validationErrorMutableLiveData = new MutableLiveData<>();
        faceValidationRepo = new FaceValidationRepo(new FaceValidationDataSource(this));
    }

    public void faceValidation(String param  ) {
        faceValidationRepo.faceValidation(param , new RequestMultiplyCallback<String>() {
            @Override
            public void onSuccess(String s) {
                validationOkMutableLiveData.setValue(s);
            }

            @Override
            public void onFail(BaseException e) {
                validationErrorMutableLiveData.setValue(e);
            }
        });
    }

    public MutableLiveData<String> getValidationOkMutableLiveData() {
        return validationOkMutableLiveData;
    }

    public MutableLiveData<BaseException> getValidationErrorMutableLiveData() {
        return validationErrorMutableLiveData;
    }
}
