package com.tda.tda.ui.adddevice;


import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.ivbaranov.rxbluetooth.RxBluetooth;
import com.tda.tda.MyBaseViewModel;
import com.tda.tda.data.repositories.impl.BluetoothRepository;
import com.tda.tda.data.repositories.impl.DevicesRepository;
import com.tda.tda.model.dataclass.BluetoothDevice;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.disposables.Disposable;

@HiltViewModel
public class AddDeviceViewModel extends MyBaseViewModel {

    private BluetoothRepository bluetoothRepo;
    private MutableLiveData<Boolean> isBluetoothOn;
    private MutableLiveData<String> bluetoothName;
    private MutableLiveData<List<BluetoothDevice>> bluetoothDevice;
    private MutableLiveData<Boolean> loading;
    private Disposable disposable;

    private List<BluetoothDevice> discoveryList;
    private int REQUEST_ENABLE_BT = 1;
    private Context context;

    @Inject
    public AddDeviceViewModel(BluetoothRepository bluetoothRepository){

        bluetoothDevice=new MutableLiveData<>();
        bluetoothName=new MutableLiveData<>();
        isBluetoothOn=new MutableLiveData<>();
        loading=new MutableLiveData<>();
        bluetoothRepo= bluetoothRepository;
        discoveryList=new ArrayList<>();
    }


    public LiveData<List<BluetoothDevice>> getBluetoothList(){return bluetoothDevice;}

    public LiveData<Boolean> getBluetoothOn() {
        return isBluetoothOn;
    }

    public LiveData<String> getBluetoothName() {
        return bluetoothName;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public void setBluetoothOnOrOff(Fragment fragment, Boolean isOn){
        if(isOn) {
            bluetoothRepo.getRxBluetooth().enableBluetooth(fragment,REQUEST_ENABLE_BT);
        }else{
            bluetoothRepo.getRxBluetooth().disable();
        }
    }

    public void prepareBluetooth(){
        onBluetoothStatusListen();
        onBluetoothStatusChanged(bluetoothRepo.isBluetoothOnOrOff()?BluetoothAdapter.STATE_ON:BluetoothAdapter.STATE_OFF);
    }

    private void onBluetoothStatusListen(){
        disposable=bluetoothRepo.getBluetoothStatus()
                .subscribe(this::onBluetoothStatusChanged);
    }

    private void onBluetoothStatusChanged(int integer){
        if(integer == BluetoothAdapter.STATE_ON){
            isBluetoothOn.postValue(true);
            scanBluetooth();
        }
        if (integer == BluetoothAdapter.STATE_OFF){
            isBluetoothOn.postValue(false);
        }
    }

    public void scanBluetooth(){
        disposable=bluetoothRepo.prepareScan()
                .subscribe(s->{
                    if(s.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)){
                        loading.postValue(true);
                        discoveryList.clear();
                    }
                    if(s.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)){
                        loading.postValue(false);
                        bluetoothDevice.postValue(discoveryList);
                    }
                },throwable -> {
                    Log.i("BTK","Scan : ");
                    throwable.printStackTrace();
                });

        disposable=bluetoothRepo.onScannedDevice()
                .subscribe(device -> {
                    try {
                        discoveryList.add(new BluetoothDevice(device.getName(), device.getAddress()));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                },throwable -> {
                    Log.i("BTK","ERERE");
                    throwable.printStackTrace();}, () -> {
                });

        bluetoothRepo.startScan();
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public void setContext(Context context) {
        this.context = context;
    }
}