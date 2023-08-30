package com.tda.tda.model.dialogs.getuserbyfp;

import androidx.lifecycle.MutableLiveData;

import com.tda.tda.MyBaseViewModel;
import com.tda.tda.data.repositories.impl.DevicesRepository;
import com.tda.tda.helpers.DB.Models.DeviceDetails;
import com.tda.tda.model.dataclass.Result;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class GetUserByFPDialogViewModel extends MyBaseViewModel {

    private DevicesRepository devicesRepository;
    private MutableLiveData<Result> result;

    @Inject
    public GetUserByFPDialogViewModel(DevicesRepository devicesRepository){
        result=new MutableLiveData<>();
        this.devicesRepository=devicesRepository;
    }


    public MutableLiveData<Result> getResult(){return result;}


    public void newUserByFP(int device_id,String fp){
        DeviceDetails answer = devicesRepository.newUserByFP(device_id,fp);
        if(answer.name==null){
            result.postValue(new Result(Result.CODE_BLUETOOTH_GET_USER_BY_FP,false,"-1"));
        }else if(answer.name.equals("بدون نام")) {
            result.postValue(new Result(Result.CODE_BLUETOOTH_GET_USER_BY_FP,true,""));
        }else{
            result.postValue(new Result(Result.CODE_BLUETOOTH_GET_USER_BY_FP,false,answer.name));
        }
    }

}