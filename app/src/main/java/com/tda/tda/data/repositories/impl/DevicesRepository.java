package com.tda.tda.data.repositories.impl;

import com.tda.tda.helpers.DB.DB;
import com.tda.tda.helpers.DB.Models.Device;
import com.tda.tda.helpers.DB.Models.DeviceDetails;
import com.tda.tda.model.dataclass.Devices;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class DevicesRepository {

    private DB db;

    @Inject
    public DevicesRepository(DB db) {
        this.db = db;
    }

    public List<Devices> getDevices() {
        List<Devices> newList=new ArrayList<>();
        List<Device> myList=db.tbDevice().getAll();
        for(int i=0;i<myList.size();i++){
            Devices item=new Devices();
            item.setId(myList.get(i).id);
            item.setName(myList.get(i).device_name);
            item.setIp(myList.get(i).device_ip);
            newList.add(item);
        }
        return  newList;
    }

    public Device getDeviceByNameOrIp(String name,String ip) {
        Device myDevice=db.tbDevice().getByNameOrAddress(name,ip);
        return  myDevice;
    }

    public Boolean saveNewDevice(String name,String password,String ip,String bluetoothName){
        boolean result=false;
        try {
            Device hasDevice=getDeviceByNameOrIp(name,ip);
            if(hasDevice==null){
                Device newDevice=new Device();
                newDevice.device_name=name;
                newDevice.device_bluetooth=bluetoothName;
                newDevice.device_ip=ip;
                newDevice.password=password;
                db.tbDevice().insert(newDevice);
                result=true;
            }
        }catch (Exception e){}
        return result;
    }

    public List<DeviceDetails> getDeviceUsers(int device_id){
        List<DeviceDetails> result=new ArrayList<>();
        try {
            result=db.tbDeviceDetails().getAllByDevice(device_id);

        }catch (Exception e){}
        return result;
    }

}
