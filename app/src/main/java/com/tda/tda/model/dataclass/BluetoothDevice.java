package com.tda.tda.model.dataclass;

public class BluetoothDevice {

    private String name;
    private String ip;
    private boolean isSelected;

    public BluetoothDevice(String name, String ip) {
        this.name = name;
        this.ip = ip;
        this.isSelected = false;
    }
    public BluetoothDevice() {
        this.isSelected = false;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
