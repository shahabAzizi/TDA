package com.tda.tda.helpers.DB.Tables;

import androidx.room.Dao;
import androidx.room.Delete;
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
    @Query("DELETE FROM devicedetails where device_id=:id")
    int delete(int id);

    @Query("DELETE FROM devicedetails where id=:id")
    int deleteUser(int id);

    @Query("DELETE FROM devicedetails where 1")
    int deleteAll();

    @Query("DELETE FROM devicedetails where device_id=:id")
    int resetDevice(int id);

    @Query("SELECT * FROM devicedetails where device_id=:device_id and fp_user_id=:fp")
    DeviceDetails getUserByFP(int device_id,String fp);
//
//    @Update
//    void update(Task task);


}
