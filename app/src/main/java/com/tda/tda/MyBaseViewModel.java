package com.tda.tda;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tda.tda.data.repositories.impl.DevicesRepository;
import com.tda.tda.helpers.DB.Models.Device;
import com.tda.tda.helpers.DB.Models.User;

public class MyBaseViewModel extends ViewModel {

    private MutableLiveData<Boolean> hasBluetoothPermission;
    private DevicesRepository devicesRepository;
    public MutableLiveData<String> Token;
    public MutableLiveData<String> UserName;
    public MutableLiveData<Boolean> isUserLoggedin;

    public MyBaseViewModel(){
        hasBluetoothPermission=new MutableLiveData<>();
        Token=new MutableLiveData<>("");
        UserName=new MutableLiveData<>("بدون نام");
        isUserLoggedin=new MutableLiveData<>(false);
    }


    public MutableLiveData<Boolean> getHasBluetoothPermission() {
        return hasBluetoothPermission;
    }

    public void checkUserInfo(DevicesRepository devicesRepository){
        this.devicesRepository=devicesRepository;
        User user1=devicesRepository.getUser();
        if(user1!=null){
            isUserLoggedin.postValue(true);
            UserName.postValue(user1.name);
            Token.postValue(user1.token);
        }
    }
}
