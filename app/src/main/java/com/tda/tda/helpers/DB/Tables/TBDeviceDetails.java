package com.tda.tda.helpers.DB.Tables;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.tda.tda.helpers.DB.Models.DeviceDetails;

import java.util.List;

@Dao
public interface TBDeviceDetails {

    @Query("SELECT * FROM devicedetails where device_id=:id")
    List<DeviceDetails> getAllByDevice(int id);

    @Insert
    long insert(DeviceDetails users);
//
//    @Delete
//    void delete(Task task);
//
//    @Update
//    void update(Task task);


}
