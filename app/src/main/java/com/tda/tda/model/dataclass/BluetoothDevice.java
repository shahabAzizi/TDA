package com.tda.tda.model.dataclass;

public class BluetoothDevice {

    private String name;
    private String ip;

    public BluetoothDevice(String name, String ip) {
        this.name = name;
        this.ip = ip;
    }
    public BluetoothDevice() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
