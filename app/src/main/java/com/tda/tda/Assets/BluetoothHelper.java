package com.tda.tda.Assets;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.sirvar.bluetoothkit.BluetoothKit;
import com.tda.tda.DB.Models.Device;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BluetoothHelper {

    private BluetoothKit BTKit;
    private Activity context;
    private Boolean hasBTPermission=false;
    private DiscoveryFinishedListener discoveryFinishedListener;
    private ConnectionListener connectionListener;
    BluetoothAdapter mBluetoothAdapter;
    private int triesToConnect=0;//Tries 3 times to connect.. Connect Tries Counter
    private Boolean isConnected=false;//if we Connect To a Device : true
    private Device conDevice;//Device To Connect
    final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public BluetoothHelper(Activity context){
        BTKit=new BluetoothKit();
        this.context=context;
        checkPermission();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public Boolean isBluetoothOn(){
        if(hasBTPermission){
            return BTKit.isEnabled();
        }else{
            return false;
        }
    }

    public Boolean hasPermission(){
        return hasBTPermission;
    }

    public void bluetoothOn(){
        try{
            if(!BTKit.isEnabled()) {
                BTKit.setEnabled(true);
            }
        }catch (Exception e){
        }
    }

    public void bluetoothOff(){
        try{BTKit.setEnabled(false);}catch (Exception e){}
    }

    public void checkPermission(){
        Dexter.withContext(this.context)
                .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if(multiplePermissionsReport.areAllPermissionsGranted()){
                            hasBTPermission=true;
                        }else{
                            hasBTPermission=false;
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                    }
                }).check();
    }

    public void startScan(DiscoveryFinishedListener DiscoveryFinishedListener){
        if(!hasBTPermission)return;
        if(!BTKit.isEnabled())return;
        this.discoveryFinishedListener=DiscoveryFinishedListener;
        try {
            if (!mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.startDiscovery();
            }
        }catch (Exception e){}
        new Handler().postDelayed(discoveryFinished,1);
    }

    Runnable discoveryFinished=new Runnable() {
        @Override
        public void run() {
            if(mBluetoothAdapter.isDiscovering()){
                if(BTKit.isEnabled()){
                    new Handler().postDelayed(discoveryFinished,10);
                }
            }else{
                returnDeviceLists();
            }
        }
    };

    public void returnDeviceLists(){
        List<com.tda.tda.model.dataclass.BluetoothDevice> Devices = new ArrayList<>();
        try{
            //Paired Devices
            Set<BluetoothDevice> setPair = mBluetoothAdapter.getBondedDevices();
            if(setPair.size()>0) {
                for (BluetoothDevice device : setPair) {
                    boolean isInArray = false;
                    for (int i = 0; i < Devices.size(); i++) {
                        if (Devices.get(i).getIp() == device.getAddress()) {
                            isInArray = true;
                        }
                    }
                    if (!isInArray) {
                        com.tda.tda.model.dataclass.BluetoothDevice myDevice=new com.tda.tda.model.dataclass.BluetoothDevice();
                        myDevice.setIp(device.getAddress());
                        myDevice.setName(device.getName());
                        Devices.add(myDevice);
                    }
                }
            }
        }catch (Exception e){
        }
        if(discoveryFinishedListener!=null)discoveryFinishedListener.onDiscover(Devices);
    }

    public void connectToDevice(Device device,ConnectionListener connectionListener){
        this.conDevice=device;
        this.connectionListener=connectionListener;
        context.runOnUiThread(connectionRunnabel);
//        new Handler().postDelayed(connectionRunnabel,1);
    }

    private Runnable connectionRunnabel=new Runnable() {
        @Override
        public void run() {
            try{
                if(triesToConnect>2) {

                    if (isConnected) {
                        connectionListener.onConnect(true);
                    } else {
                        connectionListener.onConnect(false);
                    }
                }else {
                    triesToConnect++;
                    BluetoothDevice mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(conDevice.device_ip);
                    Log.i("BTDevice",mBluetoothDevice.toString());
                    if (mBluetoothDevice == null) {
                        Log.i("BTDevice","Device is Null");
                        connectionListener.onConnect(false);
                        return;
                    }
                    BluetoothSocket socket=mBluetoothDevice.createInsecureRfcommSocketToServiceRecord(MY_UUID);
                    socket.connect();
                    Log.i("BTConnecting",socket.toString());
                    isConnected=socket.isConnected();
//                    isConnected=BTKit.connect(mBluetoothDevice,MY_UUID);
//                    isConnected=BTKit.getBluetoothSocket().getSocket().isConnected();
                    Log.i("BTConnect",String.valueOf(isConnected));
                    if(!isConnected) {
                        connectionListener.onConnect(true);
                    }else{
                        new Handler().postDelayed(connectionRunnabel,10);
                    }
                }
            }catch (Exception e){
                Log.e("BTK-FATAL",e.toString());
            }
        }
    };

    public interface DiscoveryFinishedListener{
        public void onDiscover(List<com.tda.tda.model.dataclass.BluetoothDevice> deviceList);
    }
    public interface ConnectionListener{
        public void onConnect(Boolean Connected);
    }
}
