package com.tda.tda.model.dialogs.newuser;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.tda.tda.MyBaseViewModel;
import com.tda.tda.data.repositories.impl.DevicesRepository;
import com.tda.tda.model.dataclass.Result;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class NewUserDialogViewModel extends MyBaseViewModel {

    private DevicesRepository devicesRepository;
    private MutableLiveData<Result> result;

    @Inject
    public NewUserDialogViewModel(DevicesRepository devicesRepository){
        result=new MutableLiveData<>();
        this.devicesRepository=devicesRepository;
    }


    public MutableLiveData<Result> getResult(){return result;}


    public void saveNewUser(int device_id,String name,String fp,String type){
        long answer = devicesRepository.newUsers(device_id,name,fp,type);
        Log.e("NUSER","answer : "+answer);
        if(answer>0) {
            result.postValue(new Result(Result.CODE_BLUETOOTH_ADD_NEW_USER,true));
        }else{
            result.postValue(new Result(Result.CODE_BLUETOOTH_ADD_NEW_USER,false));
        }
    }

}