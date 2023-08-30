package com.tda.tda.helpers.DB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.tda.tda.helpers.DB.Models.Device;
import com.tda.tda.helpers.DB.Models.DeviceDetails;
import com.tda.tda.helpers.DB.Models.User;
import com.tda.tda.helpers.DB.Tables.TBDevice;
import com.tda.tda.helpers.DB.Tables.TBDeviceDetails;
import com.tda.tda.helpers.DB.Tables.TBUser;

@Database(entities = {Device.class, DeviceDetails.class, User.class}, version = 1)
public abstract class DB extends RoomDatabase {

    private static DB instance;
    public static synchronized DB getInstance(Context context){
        if (instance == null) {
            // if the instance is null we
            // are creating a new instance
            instance =
                    // for creating a instance for our database
                    // we are creating a database builder and passing
                    // our database class with our database name.
                    Room.databaseBuilder(context.getApplicationContext(),
                            DB.class, "TDADB")
                            // below line is use to add fall back to
                            // destructive migration to our database.
                            .fallbackToDestructiveMigration()
                            // below line is to add callback
                            // to our database.
//                            .addCallback(roomCallback)
                            // below line is to
                            // build our database.
                            .build();
        }
        // after creating an instance
        // we are returning our instance
        return instance;
    }

    public abstract TBDevice tbDevice();
    public abstract TBDeviceDetails tbDeviceDetails();
    public abstract TBUser tbUser();
}
