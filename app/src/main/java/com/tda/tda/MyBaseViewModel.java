package com.tda.tda;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyBaseViewModel extends ViewModel {

    private MutableLiveData<Boolean> hasBluetoothPermission;

    public MyBaseViewModel(){
        hasBluetoothPermission=new MutableLiveData<>();
    }


    public MutableLiveData<Boolean> getHasBluetoothPermission() {
        return hasBluetoothPermission;
    }
}
