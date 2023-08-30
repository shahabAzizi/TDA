package com.tda.tda.data.repositories.impl;

import android.util.Log;

import com.tda.tda.di.NetworkModule;
import com.tda.tda.helpers.DB.Models.Device;
import com.tda.tda.model.Constants;
import com.tda.tda.model.dataclass.ApiResult;
import com.tda.tda.model.dataclass.User;
import com.tda.tda.model.listeners.ServerListener;

import javax.inject.Inject;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServerRepository {

    private final APIInterface apiInterface;
    private ServerListener serverListener;
    private DevicesRepository devicesRepository;

    @Inject
    public ServerRepository(APIInterface apiInterface,DevicesRepository devicesRepository) {
        this.apiInterface=apiInterface;
        this.devicesRepository=devicesRepository;
    }

    public void login(String mobile,ServerListener listener) {
        try {
            this.serverListener=listener;
            Call<ApiResult> call= apiInterface.login(mobile);
            call.enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void activation(String mobile,String code,ServerListener listener) {
        try {
            this.serverListener=listener;
            Call<ApiResult> call= apiInterface.activation(mobile,code);
//            call.request().headers("Authorization").add("Bearer"+"");
            call.enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void backup(String token,String data,ServerListener listener) {
        try {
            this.serverListener=listener;
            Call<ApiResult> call= apiInterface.backup(data);
            Constants.token=token;
//            call.request().headers("Authorization").add("Bearer"+"");
            call.enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void restore(String token,ServerListener listener) {
        try {
            this.serverListener=listener;
            Call<ApiResult> call= apiInterface.restore();
            Constants.token=token;
//            call.request().headers("Authorization").add("Bearer"+"");
            call.enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private final Callback<ApiResult> callback=new Callback<ApiResult>() {
        @Override
        public void onResponse(Call<ApiResult> call, Response<ApiResult> response) {
            if(response.code()==200){
                if(call.request().url().toString().contains("activate")){
                    new Thread(() -> {
                        try {
                            devicesRepository.newUser(
                                    response.body().getData().getAsJsonObject().getAsJsonObject("user").get("mobile").toString(),
                                    response.body().getData().getAsJsonObject().getAsJsonObject("user").get("name").toString(),
                                    response.body().getData().getAsJsonObject().getAsJsonObject("user").get("activation").toString(),
                                    response.body().getData().getAsJsonObject().getAsJsonObject("user").get("update_at").toString(),
                                    response.headers().get("access_token").toString()
                            );
                        }catch (Exception e){e.printStackTrace();}
                    }).start();
                }
            }
            ServerRepository.this.onResponse(response);
        }

        @Override
        public void onFailure(Call<ApiResult> call, Throwable t) {
            t.printStackTrace();
            if(serverListener!=null)serverListener.onFailure("لطفا شبکه را بررسی کنید");
        }
    };

    public void onResponse(Response<ApiResult> response){
        if(response.code()==200){
            if(serverListener!=null)serverListener.onResponse(response.body());
        }else if(response.code()==403){
            if(serverListener!=null)serverListener.onFailure("دسترسی ندارید. لطفا کمی صبر کنید");
        }else if(response.code()==500){
            if(serverListener!=null)serverListener.onFailure("خطا در ارتباط با سرور");
        }else{
            if(serverListener!=null)serverListener.onFailure("خطای سمت سرور. با پشتیبانی تماس بگیرید");
        }

    }

}
