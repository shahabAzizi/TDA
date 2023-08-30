package com.tda.tda.model.dialogs.deleteuser;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.tda.tda.MyBaseViewModel;
import com.tda.tda.data.repositories.impl.DevicesRepository;
import com.tda.tda.model.dataclass.Result;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DeleteUserDialogViewModel extends MyBaseViewModel {

    private DevicesRepository devicesRepository;
    private MutableLiveData<Result> result;

    @Inject
    public DeleteUserDialogViewModel(DevicesRepository devicesRepository){
        result=new MutableLiveData<>();
        this.devicesRepository=devicesRepository;
    }


    public MutableLiveData<Result> getResult(){return result;}


    public void deleteUser(int user_id){
        new Thread(()->{
            long answer = devicesRepository.deleteUser(user_id);
            if(answer>0) {
                result.postValue(new Result(Result.CODE_BLUETOOTH_DELETE_USER,true));
            }else{
                result.postValue(new Result(Result.CODE_BLUETOOTH_DELETE_USER,false));
            }
        }).start();

    }

}