package com.tda.tda.helpers.DB.Models;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(
        foreignKeys = {
                @ForeignKey(entity = Device.class,
                        parentColumns = "id",
                        childColumns = "device_id")
        }
)
public class DeviceDetails implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "device_id")
    public int device_id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "type")
    public String type;

    @ColumnInfo(name = "fp_user_id")
    public String fp_user_id;

}
