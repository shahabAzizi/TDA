package com.tda.tda.data.repositories.impl;

import com.tda.tda.helpers.BluetoothHelper;
import com.tda.tda.model.listeners.BluetoothConnectionListener;
import com.tda.tda.model.listeners.BluetoothStatusListener;


import javax.inject.Inject;

public class BluetoothRepository {

    private BluetoothHelper bluetoothHelper;

    @Inject
    public BluetoothRepository(BluetoothHelper bluetoothHelper) {
        this.bluetoothHelper = bluetoothHelper;
    }

    public void setBluetoothOnOrOff(Boolean isOn, BluetoothStatusListener callbacks){
        bluetoothHelper.setbluetoothOnOrOff(isOn,callbacks);
    }

    public Boolean isBluetoothOn(){
        return bluetoothHelper.isBluetoothOn();
    }

    public String getBluetoothName(){
        return bluetoothHelper.getBluetoothName();
    }

    public void startScan(BluetoothHelper.DiscoveryFinishedListener listener){
        bluetoothHelper.setDiscoveryFinishedListener(listener);
        bluetoothHelper.startScan();
    }

    public void connectToDevice(String Ip,BluetoothConnectionListener bluetoothConnectionListener) throws Exception{
        bluetoothHelper.connectToDevice(Ip, bluetoothConnectionListener);
    }

    public Boolean disconnect()throws Exception{
        return bluetoothHelper.disconnect();
    }

    public boolean isConnected(){
        return bluetoothHelper.isConnected();
    }

}
