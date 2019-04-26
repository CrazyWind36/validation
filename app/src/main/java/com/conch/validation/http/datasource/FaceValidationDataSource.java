package com.conch.validation.http.datasource;



import com.conch.validation.http.basis.BaseRemoteDataSource;
import com.conch.validation.http.basis.callback.RequestCallback;
import com.conch.validation.http.datasource.base.IFaceValidationDataSource;
import com.conch.validation.http.service.ApiService;
import com.conch.validation.http.viewmodel.base.BaseViewModel;
import com.conch.validation.util.LogUtils;

import okhttp3.RequestBody;

/**
 * 人脸检测,后端比对
 */
public class FaceValidationDataSource extends BaseRemoteDataSource implements IFaceValidationDataSource {

    private final ApiService service;

    public FaceValidationDataSource(BaseViewModel baseViewModel) {
        super(baseViewModel);
        service = getService(ApiService.class, ApiService.baseUrl);
    }


    @Override
    public void faceValidation(RequestBody request, RequestCallback<String> callback) {
        LogUtils.e("video",System.currentTimeMillis() + "Fc_source时间");
         executeWithoutProgress(service.getMostSimilarPerson(request), callback);
    }
}
