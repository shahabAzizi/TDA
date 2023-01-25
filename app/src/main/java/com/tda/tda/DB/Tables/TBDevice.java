package com.tda.tda.DB.Tables;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import com.tda.tda.DB.Models.Device;

@Dao
public interface TBDevice {

    @Query("SELECT * FROM device")
    List<Device> getAll();

    @Query("SELECT * FROM device where id=:id")
    Device get(int id);

    @Query("SELECT * FROM device where device_name=:name or device_ip=:address")
    Device getByNameOrAddress(String name,String address);

    @Insert
    long insert(Device device);
//
//    @Delete
//    void delete(Task task);
//
//    @Update
//    void update(Task task);


}
