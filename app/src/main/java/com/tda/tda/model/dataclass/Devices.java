package com.tda.tda.model.dataclass;

public class Devices {

    private String name;
    private String ip;
    private int id;

    public Devices(String name, String ip) {
        this.name = name;
        this.ip = ip;
    }
    public Devices() {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
