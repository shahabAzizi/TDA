package com.tda.tda.model.dialogs.changeadmin;

import androidx.lifecycle.MutableLiveData;

import com.tda.tda.MyBaseViewModel;
import com.tda.tda.data.repositories.impl.DevicesRepository;
import com.tda.tda.model.dataclass.Result;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ChangeAdminDialogViewModel extends MyBaseViewModel {

    private DevicesRepository devicesRepository;
    private MutableLiveData<Result> result;

    @Inject
    public ChangeAdminDialogViewModel(DevicesRepository devicesRepository){
        result=new MutableLiveData<>();
        this.devicesRepository=devicesRepository;
    }


    public MutableLiveData<Result> getResult(){return result;}


    public void resetDevice(int user_id){
        long answer = devicesRepository.resetDevice(user_id);
        if(answer>0) {
            result.postValue(new Result(Result.CODE_BLUETOOTH_RESET_DEVICE,true));
        }else{
            result.postValue(new Result(Result.CODE_BLUETOOTH_RESET_DEVICE,false));
        }
    }

}