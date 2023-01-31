package com.tda.tda.helpers;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import com.sirvar.bluetoothkit.BluetoothKit;
import com.tda.tda.model.listeners.BluetoothConnectionListener;
import com.tda.tda.model.listeners.BluetoothStatusListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BluetoothHelper {

    private static BluetoothHelper instance;
    private BluetoothKit BTKit;

    /**
     * Listeners
     */
    private DiscoveryFinishedListener discoveryFinishedListener;
    private BluetoothConnectionListener connectionListener;
    private BluetoothStatusListener bluetoothStatusListener;

    private BluetoothAdapter mBluetoothAdapter;
    private Boolean isConnected = false;//if we Connect To a Device : true
    private String conDeviceIp;//Device IP To Connect
    private BluetoothSocket connectedSocket;
    final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public static synchronized BluetoothHelper getInstance() {
        if (instance == null) {
            instance = new BluetoothHelper();
            instance.BTKit = new BluetoothKit();
            instance.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        return instance;
    }

    /**
     * Return Current Bluetooth Status
     */
    public Boolean isBluetoothOn() {
        return BTKit.isEnabled();
    }

    /**
     * Set Bluetooth On or Off With a Callback.
     * Callback used to change UI status after some seconds
     */
    public void setbluetoothOnOrOff(boolean isOn, BluetoothStatusListener callbacks) {
        try {
            if (isOn) {
                if (!BTKit.isEnabled()) BTKit.setEnabled(true);
            } else {
                BTKit.setEnabled(false);
            }
        } catch (Exception e) {
        }

        //** Call This after 1/5 second Because Bluetooth device has some delay to get on */
        if (callbacks != null) bluetoothStatusListener = callbacks;
        new Handler().postDelayed(getOnOrOffStatus, 1500);
    }

    /**
     * Return Bluetooth's name
     */
    public String getBluetoothName() {
        return mBluetoothAdapter.getName();
    }

    /**
     * Start Bluetooth Scanning Devices. Only Paried Devices returns
     */
    public void startScan() {
        if (!BTKit.isEnabled()) return;
        try {
            if (!mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.startDiscovery();
                new Handler().postDelayed(discoveryFinished, 10);
            }
        } catch (Exception e) {
        }
    }


    private void returnDeviceLists() {
        List<com.tda.tda.model.dataclass.BluetoothDevice> Devices = new ArrayList<>();
        try {
            //Paired Devices
            Set<BluetoothDevice> setPair = mBluetoothAdapter.getBondedDevices();
            if (setPair.size() > 0) {
                for (BluetoothDevice device : setPair) {
                    boolean isInArray = false;
                    for (int i = 0; i < Devices.size(); i++) {
                        if (Devices.get(i).getIp() == device.getAddress()) {
                            isInArray = true;
                        }
                    }
                    if (!isInArray) {
                        com.tda.tda.model.dataclass.BluetoothDevice myDevice = new com.tda.tda.model.dataclass.BluetoothDevice();
                        myDevice.setIp(device.getAddress());
                        myDevice.setName(device.getName());
                        Devices.add(myDevice);
                    }
                }
            }
        } catch (Exception e) {
        }
        if (discoveryFinishedListener != null) discoveryFinishedListener.onDiscover(Devices);
    }

    public boolean disconnect() throws Exception {
        boolean result = false;
        if (connectedSocket != null) {
            connectedSocket.close();
            connectedSocket = null;
            isConnected = false;
            result = true;
        }
        return result;
    }

    public void connectToDevice(String ip, BluetoothConnectionListener connectionListener) throws Exception {
        this.conDeviceIp = ip;
        if (isConnected) {
            disconnect();
        }
        BluetoothDevice mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(conDeviceIp);
        if (mBluetoothDevice == null) {
            connectionListener.onConnectionChanged(false, 1);
            return;
        }
        connectedSocket = mBluetoothDevice.createInsecureRfcommSocketToServiceRecord(MY_UUID);
        connectedSocket.connect();
        isConnected = connectedSocket.isConnected();
        if (isConnected) {
            connectionListener.onConnectionChanged(true, 1);
        } else {
            connectionListener.onConnectionChanged(false, 1);
        }
    }

    /**
     * This Runnable Runs for return Bluetooth Enable call back after some seconds
     */
    Runnable getOnOrOffStatus = () -> {
        if (bluetoothStatusListener != null)
            bluetoothStatusListener.onEnabledChanged(BTKit.isEnabled());
    };

    /**
     * This Runnable used to check Bluetooth scanner finished or not
     */
    Runnable discoveryFinished = new Runnable() {
        @Override
        public void run() {
            if (mBluetoothAdapter.isDiscovering()) {
                if (BTKit.isEnabled()) {

                    //** if scanning devices not finished we checked it after 10 milisecond later */
                    new Handler().postDelayed(discoveryFinished, 10);
                }
            } else {
                returnDeviceLists();
            }
        }
    };

    public boolean isConnected() {
        return isConnected;
    }

    public void setDiscoveryFinishedListener(DiscoveryFinishedListener discoveryFinishedListener) {
        this.discoveryFinishedListener = discoveryFinishedListener;
    }

    public interface DiscoveryFinishedListener {
        public void onDiscover(List<com.tda.tda.model.dataclass.BluetoothDevice> deviceList);
    }

    public interface ConnectionListener {
        public void onConnect(Boolean Connected);
    }
}
