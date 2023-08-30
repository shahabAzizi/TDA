package com.tda.tda.helpers;

import android.content.Context;

import com.github.ivbaranov.rxbluetooth.RxBluetooth;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class BluetoothUtils{

    private static BluetoothUtils instance;
    private RxBluetooth rxBluetooth;

    public static synchronized BluetoothUtils getInstance(Context context){
        if(instance == null){
            instance = new BluetoothUtils(context);
        }
        return instance;
    }

    private BluetoothUtils(Context context){
        rxBluetooth=new RxBluetooth(context);
    }

    public RxBluetooth getRxBluetooth(){return rxBluetooth;}

}
