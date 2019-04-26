package com.conch.validation.http.repo;


import android.graphics.Bitmap;

import com.conch.validation.http.basis.BaseRepo;
import com.conch.validation.http.basis.callback.RequestMultiplyCallback;
import com.conch.validation.http.datasource.base.IFaceValidationDataSource;
import com.conch.validation.util.LogUtils;
import com.conch.validation.util.base64.Base64Util;
import com.conch.validation.view.camera.PreviewSize;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;


public class FaceValidationRepo extends BaseRepo<IFaceValidationDataSource> {

    public FaceValidationRepo(IFaceValidationDataSource remoteDataSource) {
        super(remoteDataSource);
    }


    public void faceValidation(String param, RequestMultiplyCallback<String> multiplyCallback) {
        LogUtils.e("video",System.currentTimeMillis() + "repo时间");
        remoteDataSource.faceValidation(httpRequest(param),multiplyCallback);
    }


    private RequestBody httpRequest(String param) {
        JSONObject jsonObject = new JSONObject();
        try {
            LogUtils.e("video",System.currentTimeMillis() + "byte开始时间");
            jsonObject.put("Img", param);
            LogUtils.e("video",System.currentTimeMillis() + "byte结束时间");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
    }
}
