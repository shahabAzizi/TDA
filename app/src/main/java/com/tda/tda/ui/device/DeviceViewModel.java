package com.tda.tda.ui.device;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tda.tda.MyBaseViewModel;
import com.tda.tda.data.repositories.impl.BluetoothRepository;
import com.tda.tda.data.repositories.impl.DevicesRepository;
import com.tda.tda.helpers.DB.Models.DeviceDetails;
import com.tda.tda.model.dataclass.BluetoothDevice;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DeviceViewModel extends MyBaseViewModel {

    private int triesToConnect = 0;
    private DevicesRepository devicesRepository;
    private BluetoothRepository bluetoothRepo;
    private MutableLiveData<Boolean> isBluetoothOn;
    private MutableLiveData<Boolean> isConnected;
    private MutableLiveData<String> bluetoothName;
    private MutableLiveData<Boolean> showConnectionError;
    private MutableLiveData<List<DeviceDetails>> bluetoothList;

    @Inject
    public DeviceViewModel(DevicesRepository devicesRepository, BluetoothRepository bluetoothRepository) {

        bluetoothList = new MutableLiveData<>();
        bluetoothName = new MutableLiveData<>();
        isBluetoothOn = new MutableLiveData<>();
        isConnected = new MutableLiveData<>();
        showConnectionError=new MutableLiveData<>();
        bluetoothRepo = bluetoothRepository;
        this.devicesRepository = devicesRepository;
    }

    public void setBluetoothOnOrOff(Boolean isOn) {
        bluetoothRepo.setBluetoothOnOrOff(isOn, aBoolean -> {
            checkBluetoothStatus();
        });
    }

    public LiveData<List<DeviceDetails>> getBluetoothList() {
        return bluetoothList;
    }

    public LiveData<Boolean> getBluetoothOn() {
        return isBluetoothOn;
    }

    public LiveData<String> getBluetoothName() {
        return bluetoothName;
    }

    public LiveData<Boolean> getIsConnected() {
        return isConnected;
    }

    public LiveData<Boolean> getShowConnectionError() {
        return showConnectionError;
    }

    public Boolean isBluetoothTurnOn() {
        return bluetoothRepo.isBluetoothOn();
    }

    public void getDeviceUsers(int device_id) {
        bluetoothList.postValue(devicesRepository.getDeviceUsers(device_id));
    }

    public void connectToDevice(String ip) {
        try {
            if (!bluetoothRepo.isBluetoothOn()) {
                checkBluetoothStatus();
            }

            if (bluetoothRepo.isConnected()) {
                isConnected.postValue(!bluetoothRepo.disconnect());
            } else {
                triesToConnect++;
                bluetoothRepo.connectToDevice(ip, (result, type) -> {
                    if (!result && triesToConnect > 2) {
                        //Not Connected and Tries is more than 2 times
                        triesToConnect = 0;
                        isConnected.postValue(false);
                        showConnectionError.postValue(true);
                    } else {
                        if (result) {
                            //Connect to Device
                            triesToConnect = 0;
                            isConnected.postValue(true);
                        } else {
                            //Tries one more time
                            connectToDevice(ip);
                        }
                    }
                });
            }
        } catch (Exception e) {
            isConnected.postValue(false);
        }
    }

    public void checkBluetoothStatus() {
        if (bluetoothRepo.isBluetoothOn()) {
            bluetoothName.postValue(bluetoothRepo.getBluetoothName());
            isBluetoothOn.postValue(true);
        } else {
            if (bluetoothRepo.isConnected()) {
                try {
                    isConnected.postValue(false);
                    bluetoothRepo.disconnect();
                } catch (Exception ignored) {
                }
            }
            isBluetoothOn.postValue(false);
        }
    }

}