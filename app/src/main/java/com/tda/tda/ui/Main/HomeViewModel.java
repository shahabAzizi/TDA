package com.tda.tda.ui.Main;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tda.tda.DB.Models.Device;
import com.tda.tda.MyBaseViewModel;
import com.tda.tda.data.repositories.impl.DevicesRepository;
import com.tda.tda.model.dataclass.Devices;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.Dispatchers;

@HiltViewModel
public class HomeViewModel extends MyBaseViewModel {
    // TODO: Implement the ViewModel

    private DevicesRepository devicesRepository;

    private MutableLiveData<List<Devices>> deviceList;

    @Inject
    public HomeViewModel(DevicesRepository devicesRepository){
        this.devicesRepository = devicesRepository;
        deviceList=new MutableLiveData<>();
    }

    public void getDevices(){
        deviceList.postValue(devicesRepository.getDevices());
    }

    public LiveData<List<Devices>> getDeviceLiveData() {
        return (LiveData<List<Devices>>) deviceList;
    }
}