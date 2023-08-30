package com.tda.tda.helpers.DB.Models;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class User implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "mobile")
    public String mobile;

    @ColumnInfo(name = "activation")
    public String activation;

    @ColumnInfo(name = "token")
    public String token;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "last_update")
    public String last_update;

    @ColumnInfo(name = "need_to_update")
    public String need_to_update;

}
