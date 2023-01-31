package com.tda.tda.ui.adddevice;

import androidx.lifecycle.MutableLiveData;

import com.tda.tda.MyBaseViewModel;
import com.tda.tda.data.repositories.impl.BluetoothRepository;
import com.tda.tda.data.repositories.impl.DevicesRepository;
import com.tda.tda.model.dataclass.BluetoothDevice;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AddDeviceViewModel extends MyBaseViewModel {

    private DevicesRepository devicesRepository;
    private BluetoothRepository bluetoothRepo;
    private MutableLiveData<Boolean> isBluetoothOn;
    private MutableLiveData<String> bluetoothName;
    private MutableLiveData<List<BluetoothDevice>> bluetoothList;

    @Inject
    public AddDeviceViewModel(DevicesRepository devicesRepository, BluetoothRepository bluetoothRepository){

        bluetoothList=new MutableLiveData<>();
        bluetoothName=new MutableLiveData<>();
        isBluetoothOn=new MutableLiveData<>();
        bluetoothRepo= bluetoothRepository;
        this.devicesRepository=devicesRepository;
    }

    public void setBluetoothOnOrOff(Boolean isOn){
        bluetoothRepo.setBluetoothOnOrOff(isOn,aBoolean -> {
            checkBluetoothStatus();
        });
    }

    public MutableLiveData<List<BluetoothDevice>> getBluetoothList(){return bluetoothList;}

    public MutableLiveData<Boolean> getBluetoothOn() {
        return isBluetoothOn;
    }

    public MutableLiveData<String> getBluetoothName() {
        return bluetoothName;
    }


    public void scanBluetooth(){
        bluetoothRepo.startScan(deviceList -> {
            bluetoothList.postValue(deviceList);
        });
    }


    public void checkBluetoothStatus(){
        if(bluetoothRepo.isBluetoothOn()){
            bluetoothName.postValue(bluetoothRepo.getBluetoothName());
            isBluetoothOn.postValue(true);
            scanBluetooth();
        }else{
            isBluetoothOn.postValue(false);
        }
    }
}