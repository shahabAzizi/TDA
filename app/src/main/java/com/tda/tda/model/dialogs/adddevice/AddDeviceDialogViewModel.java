package com.tda.tda.model.dialogs.adddevice;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tda.tda.MyBaseViewModel;
import com.tda.tda.data.repositories.impl.BluetoothRepository;
import com.tda.tda.data.repositories.impl.DevicesRepository;
import com.tda.tda.model.dataclass.BluetoothDevice;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AddDeviceDialogViewModel extends MyBaseViewModel {

    private DevicesRepository devicesRepository;
    private MutableLiveData<Boolean> result;

    @Inject
    public AddDeviceDialogViewModel(DevicesRepository devicesRepository){
        result=new MutableLiveData<>();
        this.devicesRepository=devicesRepository;
    }


    public MutableLiveData<Boolean> getResult(){return result;}


    public void saveNewDevice(String name,String password,String bluetooth_name,String ip){
        result.postValue(devicesRepository.saveNewDevice(name,password,ip,bluetooth_name));
    }

}