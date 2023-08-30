package com.tda.tda.ui.login;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tda.tda.MyBaseViewModel;
import com.tda.tda.data.repositories.impl.ServerRepository;
import com.tda.tda.model.dataclass.ApiResult;
import com.tda.tda.model.dataclass.Result;
import com.tda.tda.model.listeners.ServerListener;

import java.io.IOException;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class LoginViewModel extends MyBaseViewModel {

    private ServerRepository serverRepository;
    private MutableLiveData<Boolean> loading;
    private MutableLiveData<Result> result;

    @Inject
    public LoginViewModel(ServerRepository serverRepository){
        this.serverRepository=serverRepository;
        this.loading=new MutableLiveData<>();
        this.result=new MutableLiveData<>();
    }

    public void login(String mobile){
        loading.postValue(true);
        serverRepository.login(mobile,listener);
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
        }

        @Override
        public void onFailure(String message) {
            loading.postValue(false);
            result.postValue(new Result(400,false,message));
        }
    };
}