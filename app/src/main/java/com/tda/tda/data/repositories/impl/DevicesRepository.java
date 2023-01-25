package com.tda.tda.data.repositories.impl;

import android.content.Context;

import com.tda.tda.DB.DB;
import com.tda.tda.DB.Models.Device;
import com.tda.tda.model.dataclass.Devices;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;
import kotlinx.coroutines.MainCoroutineDispatcher;

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

}
