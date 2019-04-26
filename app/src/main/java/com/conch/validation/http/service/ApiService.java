package com.conch.validation.http.service;



import com.conch.validation.http.basis.model.BaseResponseBody;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 *  接口
 */
public interface ApiService {

    String baseUrl = "http://192.168.1.211:15000";

    /**
     * 发送人脸图片获取最相似的人员信息
     * @param request
     * @return
     */
    @POST("/FaceCompare/GetMostSimilarPerson")
    Observable<BaseResponseBody<String>> getMostSimilarPerson(@Body RequestBody request);





}
