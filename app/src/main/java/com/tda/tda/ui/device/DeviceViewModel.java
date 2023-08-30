package com.tda.tda.ui.device;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tda.tda.HomeActivity;
import com.tda.tda.MyBaseViewModel;
import com.tda.tda.data.repositories.impl.BluetoothRepository;
import com.tda.tda.data.repositories.impl.DevicesRepository;
import com.tda.tda.helpers.BluetoothHelper;
import com.tda.tda.helpers.DB.Models.DeviceDetails;
import com.tda.tda.model.dataclass.Result;
import com.tda.tda.model.listeners.BluetoothCommandListener;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.disposables.Disposable;

@HiltViewModel
public class DeviceViewModel extends MyBaseViewModel {

    private int triesToConnect = 0;
    private final DevicesRepository devicesRepository;
    private final BluetoothRepository bluetoothRepo;
    private final MutableLiveData<Result> result;
    private final MutableLiveData<Boolean> loading;
    private final MutableLiveData<Boolean> isBluetoothOn;
    private final MutableLiveData<List<DeviceDetails>> usersList;

    private Disposable disposable;
    private final int REQUEST_ENABLE_BT = 1;
    private int triesToSendCommand = 0;
    private boolean hasCommendSended = false;
    private int hasCommendEnded = 0;
    private BluetoothSocket socket;
    private Timer timerSend;
    private Timer timerReading;
    private String recieveData = "";
    private String command = "";

    @Inject
    public DeviceViewModel(DevicesRepository devicesRepository, BluetoothRepository bluetoothRepository) {
        result = new MutableLiveData<>();
        loading = new MutableLiveData<>();
        isBluetoothOn = new MutableLiveData<>();
        usersList = new MutableLiveData<>();
        bluetoothRepo = bluetoothRepository;
        bluetoothRepo.setBluetoothCommandListener(this.bluetoothCommandListener);
        this.devicesRepository = devicesRepository;
    }

    public LiveData<Boolean> getBluetoothOn() {
        return isBluetoothOn;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<Result> getResult() {
        return result;
    }

    public LiveData<List<DeviceDetails>> getUsersList() {
        return usersList;
    }

    public void setBluetoothOnOrOff(Fragment fragment, Boolean isOn) {
        if (isOn) {
            bluetoothRepo.getRxBluetooth().enableBluetooth(fragment, REQUEST_ENABLE_BT);
        } else {
            bluetoothRepo.getRxBluetooth().disable();
            disconnect();
        }
    }

    public void prepareBluetooth() {
        onBluetoothStatusListen();
        onBluetoothStatusChanged(bluetoothRepo.isBluetoothOnOrOff() ? BluetoothAdapter.STATE_ON : BluetoothAdapter.STATE_OFF);
    }

    private void onBluetoothStatusListen() {
        disposable = bluetoothRepo.getBluetoothStatus()
                .subscribe(this::onBluetoothStatusChanged);
    }

    private void onBluetoothStatusChanged(int integer) {
        if (integer == BluetoothAdapter.STATE_ON) {
            isBluetoothOn.postValue(true);
        }
        if (integer == BluetoothAdapter.STATE_OFF) {
            isBluetoothOn.postValue(false);
        }
    }

    public void disconnect() {
        cancelTimers();
        try {
            Log.i("BTK","Disconnect");
            if (socket != null) {
//                bluetoothRepo.disconnect();
                socket.getInputStream().close();
                socket.getOutputStream().close();
                socket.close();
                socket = null;
                result.postValue(new Result(Result.CODE_BLUETOOTH_CONNECT, false));
            }
        } catch (Exception e) {
        }
    }

    public void startConnecting(String ip) {
        if (socket == null) {
            BluetoothDevice device=bluetoothRepo.getDevice(ip);
            if(device.getBondState() != BluetoothDevice.BOND_BONDED){
                loading.postValue(false);
                result.postValue(new Result(Result.CODE_BLUETOOTH_CONNECT, false));
//                result.postValue(new Result(Result.CODE_BLUETOOTH_BOUND, true));
                device.createBond();

            }else {
                disposable = bluetoothRepo.connectToDevice2(ip)
                        .subscribe(bluetoothSocket -> {
                            socket = bluetoothSocket;
                            result.postValue(new Result(Result.CODE_BLUETOOTH_CONNECT, true));
                        }, throwable -> {
                            result.postValue(new Result(Result.CODE_BLUETOOTH_CONNECT, false));
                        });
            }
        } else {
            disconnect();
        }
    }

    private void clearDataOnNewCommand(){
        hasCommendSended = false;
        hasCommendEnded = 0;
        triesToSendCommand = 0;
        recieveData = "";
    }

    private void startExecuteCommand() {
        if (socket != null) {
            if (bluetoothRepo.prepareToSendCommand(socket)) {
                clearDataOnNewCommand();
                reading();
                timerSend = new Timer();
                timerSend.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        //If Connection Failed show Error to User
                        if (socket == null) {
                            result.postValue(new Result(Result.CODE_BLUETOOTH_CONNECT, false));
                            cancelTimers();
                        }

                        if (hasCommendSended && hasCommendEnded == 1) {
                            //If Command Sent Successfully
                            cancelTimers();
                        } else {
                            if (!hasCommendSended) {
                                //If Command Not Sent
                                triesToSendCommand++;
                                if (triesToSendCommand <= 10 && hasCommendEnded != 1) {
                                    //Retries one more time
                                    startSending(command);
                                } else {
                                    //If our Tries is over 40 times and we have no answer
                                    bluetoothCommandListener.onCommandFailed(command);
                                    cancelTimers();
                                }
                            }
                            if (hasCommendSended && hasCommendEnded == -1) {
                                //If Command Sent but the command String received on hc-05 with error, Retries one more
                                triesToSendCommand++;
                                startSending(command);
                            }
                        }
                    }
                }, 1000, 1000);
            } else {
                result.postValue(new Result(Result.CODE_BLUETOOTH_CONNECT, false));
            }
        } else {
            result.postValue(new Result(Result.CODE_BLUETOOTH_CONNECT, false));
        }
    }

    private void startSending(String Command) {

        Log.i("BTK","Sending  : "+Command);
        Boolean ans=bluetoothRepo.send(Command);
        Log.i("BTK","Sending Result  : "+ans);
    }

    private void onRecieveString() {
        int indexOffinishcommand = -1;
        while(recieveData.indexOf("}")>0) {
            indexOffinishcommand = recieveData.indexOf("}");
            if (recieveData.charAt(0) == '{' && indexOffinishcommand > 0) {
                String res = recieveData.substring(0, indexOffinishcommand + 1);
                if((indexOffinishcommand+1)<recieveData.length()) {
                    recieveData = recieveData.substring(indexOffinishcommand + 1);
                }else{
                    recieveData="";
                }
                bluetoothCommandListener.onCommandExecuted(command, true, res);
            }
        }
    }

    public void newUser() {
        command = BluetoothHelper.COM_ADD_NEW_USER;
        startExecuteCommand();
    }

    public void deleteUser(String fp_id) {
        command = BluetoothHelper.COM_DELETE_USER.replaceAll("id", fp_id);
        startExecuteCommand();
    }

    public void changeAdmin(String fp_id) {
        command = BluetoothHelper.COM_CHANGE_ADMIN.replaceAll("id", fp_id);
        startExecuteCommand();
    }

    public void getUserByFP() {
        command = BluetoothHelper.COM_GET_USER_BY_FP;
        startExecuteCommand();
    }

    public void getDeviceUsersList(int device_id) {
        usersList.postValue(devicesRepository.getDeviceUsers(device_id));
    }

    public void deleteDevice(int device_id) {
        result.postValue(new Result(Result.CODE_DELETE_DEVICE,devicesRepository.deleteDevice(device_id)));
    }

    public void resetDevice() {
        command = BluetoothHelper.COM_RESET_DEVICE;
        startExecuteCommand();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disconnect();
        cancelTimers();
        try {
            if (!disposable.isDisposed()) disposable.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final BluetoothCommandListener bluetoothCommandListener;

    {
        bluetoothCommandListener = new BluetoothCommandListener() {
            @Override
            public void onCommandExecuted(String command, boolean res, String recievedData) {
                    if (command.equals(BluetoothHelper.COM_ADD_NEW_USER)) {
                        result.postValue(new Result(Result.CODE_BLUETOOTH_ADD_NEW_USER, res, recievedData));
                    }
                    if (command.contains("Delete")) {
                        result.postValue(new Result(Result.CODE_BLUETOOTH_DELETE_USER, res, recievedData));
                    }
                    if (command.equals(BluetoothHelper.COM_GET_USER_BY_FP)) {
                        result.postValue(new Result(Result.CODE_BLUETOOTH_GET_USER_BY_FP, res, recievedData));
                    }
                    if (command.equals(BluetoothHelper.COM_RESET_DEVICE)) {
                        result.postValue(new Result(Result.CODE_BLUETOOTH_RESET_DEVICE, res, recievedData));
                    }
                    if (command.contains("NewAdmin")) {
                        result.postValue(new Result(Result.CODE_BLUETOOTH_CHANGE_ADMIN, res, recievedData));
                    }
            }

            @Override
            public void onCommandFailed(String command) {
                result.postValue(new Result(Result.CODE_COMMAND_FAILED, false, command));
            }
        };
    }

    private void reading() {
        new Thread(() -> {
            timerReading = new Timer();
            timerReading.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    try {
                        if(socket==null){
                            cancelTimers();
                            return;
                        }
                        if (socket.getInputStream().available() > 0) {
                            byte[] arrByte = new byte[socket.getInputStream().available()];
                            socket.getInputStream().read(arrByte);
                            String result = new String(arrByte);
                            Log.i("BTK","Reading : " +result);
                            switch (result) {
                                case "*":
                                    hasCommendSended = true;
                                    break;
                                case "+":
                                    hasCommendEnded = 1;
                                    hasCommendSended=true;
                                    timerReading.cancel();
                                    break;
                                case "!":
                                    hasCommendEnded = -1;
                                    break;
                                default:
                                    hasCommendSended = true;
                                    recieveData = recieveData.concat(result.trim());
                                    onRecieveString();
                                    break;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 0, 1);
        }).start();
    }

    private void cancelTimers(){
        try{
           if(timerSend!=null)timerSend.cancel();
           if(timerReading!=null)timerReading.cancel();
        }catch (Exception e){}
    }

}