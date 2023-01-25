package com.tda.tda.ui.AddDevice;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tda.tda.DB.Models.Device;
import com.tda.tda.MyBaseViewModel;
import com.tda.tda.data.repositories.impl.DevicesRepository;
import com.tda.tda.model.dataclass.BluetoothDevice;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AddDeviceViewModel extends MyBaseViewModel {

    private DevicesRepository devicesRepository;
    private MutableLiveData<String> name;
    private MutableLiveData<String> password;
    private MutableLiveData<List<BluetoothDevice>> bluetoothList;

    @Inject
    public AddDeviceViewModel(DevicesRepository devicesRepository){
        name=new MutableLiveData<>();
        password=new MutableLiveData<>();
        bluetoothList=new MutableLiveData<>();

        this.devicesRepository=devicesRepository;
    }

    public MutableLiveData<String> getName() {
        return name;
    }
    public MutableLiveData<String> getPassword() {
        return password;
    }
}