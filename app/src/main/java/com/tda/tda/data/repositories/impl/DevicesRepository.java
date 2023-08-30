package com.tda.tda.data.repositories.impl;

import android.util.Log;

import com.tda.tda.helpers.DB.DB;
import com.tda.tda.helpers.DB.Models.Device;
import com.tda.tda.helpers.DB.Models.DeviceDetails;
import com.tda.tda.helpers.DB.Models.User;
import com.tda.tda.model.dataclass.Devices;
import com.tda.tda.model.dataclass.Result;

import java.util.ArrayList;
import java.util.Date;
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
                db.tbUser().updateNeedToUpdate("true");
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

    public long newUsers(int device_id,String name,String fp,String type){
        try {
            DeviceDetails details = new DeviceDetails();
            details.name = name;
            details.device_id = device_id;
            details.fp_user_id = fp;
            details.type = type;

            db.tbUser().updateNeedToUpdate("true");

            return db.tbDeviceDetails().insert(details);

        }catch (Exception e){}
        return -1;
    }

    public DeviceDetails newUserByFP(int device_id,String fp){
        try {
            DeviceDetails search=db.tbDeviceDetails().getUserByFP(device_id,fp);
            if(search == null) {
                DeviceDetails details = new DeviceDetails();
                details.name = "بدون نام";
                details.device_id = device_id;
                details.fp_user_id = fp;
                details.type = DeviceDetails.TYPE_USER_NORMAL;
                long result = db.tbDeviceDetails().insert(details);
                if(result>=0){
                    db.tbUser().updateNeedToUpdate("true");
                    return details;
                }
            }else{
                return search;
            }

        }catch (Exception e){}
        return new DeviceDetails();
    }

    public Boolean deleteDevice(int device_id){
        boolean result=false;
        try {
            db.tbDeviceDetails().delete(device_id);
            db.tbDevice().delete(device_id);
            db.tbUser().updateNeedToUpdate("true");
            result=true;
        }catch (Exception e){}
        return result;
    }

    public long deleteUser(int user_id){
        long result=-1;
        try {
            db.tbDeviceDetails().deleteUser(user_id);
            db.tbUser().updateNeedToUpdate("true");
            result=1;
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public long resetDevice(int device_id){
        long result=-1;
        try {
            db.tbDeviceDetails().resetDevice(device_id);
            db.tbUser().updateNeedToUpdate("true");
            result=1;
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public long newUser(String mobile,String name,String activation,String last_update,String token){
        try {
            User user = new User();
            user.name = name;
            user.mobile = mobile;
            user.activation = activation;
            user.last_update = last_update;
            user.token=token;
            user.need_to_update="true";

            return db.tbUser().insert(user);

        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    public User getUser(){
        try {
            return db.tbUser().get();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public long resetDB(){
        try {
            db.tbDevice().deleteAll();
            db.tbDeviceDetails().deleteAll();
            return 1;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    public long setUserBackup(String dateStr,boolean isBackup){
        try {
            db.tbUser().setlastUpdate(dateStr);
            db.tbUser().updateNeedToUpdate(String.valueOf(isBackup));
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

}
