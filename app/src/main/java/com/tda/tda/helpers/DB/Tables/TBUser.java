package com.tda.tda.helpers.DB.Tables;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.tda.tda.helpers.DB.Models.Device;
import com.tda.tda.helpers.DB.Models.User;

import java.util.List;

@Dao
public interface TBUser {

    @Query("SELECT * FROM user LIMIT 1")
    User get();

    @Insert
    long insert(User user);

    @Query("DELETE FROM user")
    int delete();

    @Query("update user set token=:token")
    int updateToken(String token);

    @Query("update user set need_to_update=:needToUpdate")
    int updateNeedToUpdate(String needToUpdate);

    @Query("update user set last_update=:lastUpdate")
    int setlastUpdate(String lastUpdate);

//
//    @Delete
//    void delete(Task task);
//
//    @Update
//    void update(Task task);


}
