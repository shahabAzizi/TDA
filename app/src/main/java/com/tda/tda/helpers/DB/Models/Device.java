package com.tda.tda.helpers.DB.Models;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Device implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "device_name")
    public String device_name;

    @ColumnInfo(name = "device_bluetooth")
    public String device_bluetooth;

    @ColumnInfo(name = "device_ip")
    public String device_ip;

    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "fp_password")
    public String fp_password;

}
