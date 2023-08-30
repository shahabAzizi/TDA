package com.tda.tda.data.repositories.impl;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.github.ivbaranov.rxbluetooth.BluetoothConnection;
import com.github.ivbaranov.rxbluetooth.RxBluetooth;
import com.github.ivbaranov.rxbluetooth.exceptions.ConnectionClosedException;
import com.github.ivbaranov.rxbluetooth.predicates.BtPredicate;
import com.tda.tda.helpers.BluetoothHelper;
import com.tda.tda.model.listeners.BluetoothCommandListener;
import com.tda.tda.model.listeners.BluetoothConnectionListener;


import java.io.IOException;
import java.net.Socket;

import javax.inject.Inject;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.android.MainThreadDisposable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BluetoothRepository {

    private final BluetoothHelper bluetoothHelper;
    private final RxBluetooth rxBluetooth;
    private BluetoothConnection bluetoothConnection;
    BluetoothAdapter bluetoothAdapter;

    @Inject
    public BluetoothRepository(BluetoothHelper bluetoothHelper,RxBluetooth rxBluetooth) {
        this.bluetoothHelper = bluetoothHelper;
        this.rxBluetooth=rxBluetooth;
    }

    public void disconnect(){if(bluetoothConnection!=null)bluetoothConnection.closeConnection();}

    public RxBluetooth getRxBluetooth(){return rxBluetooth;}

    public Boolean isBluetoothOnOrOff(){
        return rxBluetooth.isBluetoothEnabled();
    }

    public Observable<Integer> getBluetoothStatus(){
        return rxBluetooth.observeBluetoothState()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<String> prepareScan(){
        if(rxBluetooth.isDiscovering()){
            Log.i("BTK","HERE");
            rxBluetooth.cancelDiscovery();
        }
        return rxBluetooth.observeDiscovery()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation());
    }

    public void startScan(){

        boolean sasa=rxBluetooth.enable();
        boolean starte=rxBluetooth.startDiscovery();

    }

    public Observable<BluetoothDevice> onScannedDevice(){
        return rxBluetooth.observeDevices()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Single<BluetoothSocket> connectToDevice2(String ip){
        BluetoothDevice device=bluetoothHelper.getDeviceByIp(ip);
        return rxBluetooth.connectAsClient(device, BluetoothHelper.MY_UUID,false)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public BluetoothDevice getDevice(String ip){
        BluetoothDevice device=bluetoothHelper.getDeviceByIp(ip);
        return device;
    }

    public void boundDevice(String ip){
        try {
            BluetoothDevice device = bluetoothHelper.getDeviceByIp(ip);
            device.createBond();
        }catch (Exception e){}
    }


    public boolean prepareToSendCommand(BluetoothSocket socket){
        try{
            if(bluetoothConnection==null) {
                bluetoothConnection = new BluetoothConnection(socket);
            }

            return true;
        }catch (Exception e){
            return false;
        }
    }


    public Boolean send(String command){
        return bluetoothConnection.send(command);
    }

    public void setBluetoothCommandListener(BluetoothCommandListener bluetoothCommandListener){
        bluetoothHelper.setBluetoothCommandListener(bluetoothCommandListener);
    }


}
