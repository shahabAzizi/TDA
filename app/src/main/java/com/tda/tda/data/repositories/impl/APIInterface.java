package com.tda.tda.data.repositories.impl;

import com.tda.tda.model.dataclass.ApiResult;
import com.tda.tda.model.dataclass.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIInterface {

    @POST("/api/login")
    @FormUrlEncoded
    Call<ApiResult> login(@Field("mobile") String mobile);


    @POST("/api/activate")
    @FormUrlEncoded
    Call<ApiResult> activation(@Field("mobile") String mobile, @Field("code") String code);

    @POST("/api/update")
    @FormUrlEncoded
    Call<ApiResult> backup(@Field("data") String data);

    @GET("/api/get")
    Call<ApiResult> restore();
}
