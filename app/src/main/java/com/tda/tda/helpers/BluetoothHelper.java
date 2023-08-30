package com.tda.tda.helpers;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.annotation.WorkerThread;

import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothService;
import com.sirvar.bluetoothkit.BluetoothKit;
import com.tda.tda.model.listeners.BluetoothCommandListener;
import com.tda.tda.model.listeners.BluetoothConnectionListener;
import com.tda.tda.model.listeners.BluetoothStatusListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

public class BluetoothHelper {

    public final static UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public final static String COMMAND_ACCEPTED = "*";
    public final static String COM_ADD_NEW_USER = "*AddNew#";
    public final static String COM_GET_USER_BY_FP = "*GetUser#";
    public final static String COM_RESET_DEVICE = "*Reset#";
    public final static String COM_DELETE_USER = "*Delete{id}#";
    public final static String COM_CHANGE_ADMIN = "*NewAdmin{id}#";


    private static BluetoothHelper instance;
    private BluetoothKit BTKit;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothSocket connectedSocket;

    /**
     * Listeners
     */
    private DiscoveryFinishedListener discoveryFinishedListener;
    private BluetoothStatusListener bluetoothStatusListener;
    private BluetoothCommandListener bluetoothCommandListener;

    private int triesToSendCommand = 0;//Count of Retry to send a command
    private Boolean isConnected = false;//if we Connect To a Device : true
    private Boolean hasCommandExecuted = false;//if Command executed successfully true:false
    private String conDeviceIp;//Device IP To Connect
    private String command = "";
    private String result = "";
    private long readingStartedTimeout = 0;

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
//        if (!BTKit.isEnabled()) return;
        try {
            if (!mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.startDiscovery();
//                new Handler().postDelayed(discoveryFinished, 10);
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

    public BluetoothDevice getDeviceByIp(String ip){
        return mBluetoothAdapter.getRemoteDevice(ip);
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
        connectedSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(MY_UUID);
        connectedSocket.connect();
        Thread.sleep(1000);
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

    private void readWrite(String command, int tries, boolean hasCommandExecuted, String result) {
        this.triesToSendCommand = tries;
        this.command = command;
        this.hasCommandExecuted = hasCommandExecuted;
        this.result = result;
        executeCommand();
        read();
        if (!this.hasCommandExecuted && tries < 20)
            readWrite(command,tries+1,hasCommandExecuted,result);
    }

    @WorkerThread
    public void readWrite(String command) {
        readWrite(command, 0, false, "");
    }

    private void executeCommand() {
        this.readingStartedTimeout = System.currentTimeMillis();
        if (triesToSendCommand >= 20 && !hasCommandExecuted) {
            if (bluetoothCommandListener != null) bluetoothCommandListener.onCommandFailed(command);
            return;
        }
        try {
            Log.e("BTK", "Write Command with Try:" + triesToSendCommand);
            if (!hasCommandExecuted) {
                triesToSendCommand++;
                this.result = "";
                connectedSocket.getOutputStream().write(command.getBytes());
                connectedSocket.getOutputStream().flush();
                Thread.sleep(50);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void read() {
        Log.e("BTK", "Method Read");
        if (connectedSocket == null) return;
        if (!connectedSocket.isConnected()) return;



        try {

            if (connectedSocket.getInputStream().available() <= 0 && !hasCommandExecuted) {
                Log.e("BTK", "Cannot Read Acknowledge");
                return;
            } else {
                if (hasCommandExecuted) {
                    readOthers();
                } else {
                    byte[] buffer = new byte[2];
                    connectedSocket.getInputStream().read(buffer);
                    String res = new String(buffer);
                    Log.e("BTK", "Acc:" + res);
                    if (res.charAt(0) == '*') {
                        Log.e("BTK", "Reading Started 1");
                        hasCommandExecuted = true;
                        result = result.concat(res.substring(1));
                        readOthers();
//                    }else if(res.indexOf("*")==0 && res.length()==1) {
//                        Log.e("BTK", "Reading Started 2");
//                        hasCommandExecuted = true;
//                        result = result.concat(res.substring(1));
//                        readOthers();
                    } else {
                        Log.e("BTK", "Accknowlage Statement failed 2");
                        return;
                    }
                }
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    private void readOthers() {
        try {
            Log.e("BTK", "ReadOthers");
            //Read until we have Data
            while (connectedSocket.getInputStream().available() > 0 || (System.currentTimeMillis() - readingStartedTimeout) < 10000) {

                if (connectedSocket.getInputStream().available() > 0) {
                    byte[] buffer = new byte[connectedSocket.getInputStream().available()];
                    connectedSocket.getInputStream().read(buffer);
                    String res = new String(buffer);
                    result = result.concat(res);

                    int indexofStartResult = result.indexOf("{");
                    int indexofEndResult = result.indexOf("}");
                    if (indexofStartResult >= 0 && indexofEndResult > 0 && indexofStartResult != indexofEndResult) {
                        hasCommandExecuted = true;
                        String oneResult = result.substring(indexofStartResult, indexofEndResult + 1);
                        Log.e("BTK", "Final String Result: " + oneResult + ". of Total String : " + result);
                        result = result.substring(indexofEndResult + 1);
                        if (bluetoothCommandListener != null) {
                            bluetoothCommandListener.onCommandExecuted(command, true, oneResult);
                        }
                        JSONObject jsonObject=new JSONObject(oneResult);
                        if(jsonObject.getString("code").equals("01")){
                            Log.e("BTK","Reading Out-- End of Command");
                            return;
                        }
                        Thread.sleep(20);
                    }

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBluetoothCommandListener(BluetoothCommandListener bluetoothCommandListener) {
        this.bluetoothCommandListener = bluetoothCommandListener;
    }

    public interface DiscoveryFinishedListener {
        public void onDiscover(List<com.tda.tda.model.dataclass.BluetoothDevice> deviceList);
    }


}
