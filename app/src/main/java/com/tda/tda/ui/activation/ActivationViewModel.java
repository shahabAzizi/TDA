package com.tda.tda.ui.activation;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tda.tda.MyBaseViewModel;
import com.tda.tda.data.repositories.impl.DevicesRepository;
import com.tda.tda.data.repositories.impl.ServerRepository;
import com.tda.tda.model.dataclass.ApiResult;
import com.tda.tda.model.dataclass.Result;
import com.tda.tda.model.listeners.ServerListener;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ActivationViewModel extends MyBaseViewModel {

    private ServerRepository serverRepository;
    private MutableLiveData<Boolean> loading;
    private MutableLiveData<Result> result;
    private DevicesRepository devicesRepository;

    @Inject
    public ActivationViewModel(ServerRepository serverRepository, DevicesRepository devicesRepository){
        this.serverRepository=serverRepository;
        this.loading=new MutableLiveData<>();
        this.result=new MutableLiveData<>();
        this.devicesRepository=devicesRepository;
    }

    public void activation(String mobile,String code){
        loading.postValue(true);
        serverRepository.activation(mobile,code,listener);
    }


    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<Result> getResult() {
        return result;
    }


    private ServerListener listener=new ServerListener() {
        @Override
        public void onResponse(ApiResult result1) {
            loading.postValue(false);
            result.postValue(new Result(200,true,""));
            new Thread(()->{checkUserInfo(devicesRepository);}).start();
        }

        @Override
        public void onFailure(String message) {
            loading.postValue(false);
            result.postValue(new Result(400,false,message));
        }
    };
}