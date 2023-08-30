package com.tda.tda.model.listeners;

public interface BluetoothCommandListener {
    public void onCommandExecuted(String command,boolean result,String recievedData);
    public void onCommandFailed(String command);
}
