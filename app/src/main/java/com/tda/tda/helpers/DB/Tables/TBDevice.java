package com.tda.tda.helpers.DB.Tables;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import com.tda.tda.helpers.DB.Models.Device;

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

    @Query("DELETE FROM device where id=:id")
    int delete(int id);


    @Query("DELETE FROM device where 1")
    int deleteAll();

//
//    @Delete
//    void delete(Task task);
//
//    @Update
//    void update(Task task);


}
