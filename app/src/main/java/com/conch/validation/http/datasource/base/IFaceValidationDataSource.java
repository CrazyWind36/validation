package com.conch.validation.http.datasource.base;





import com.conch.validation.http.basis.callback.RequestCallback;

import okhttp3.RequestBody;

/**
 * 人脸检测,后端比对
 * @author Administrator
 */
public interface IFaceValidationDataSource {

    /**
     * 检测人脸
     * @param request
     * @param callback
     */
    void faceValidation(RequestBody request, RequestCallback<String> callback);

}
