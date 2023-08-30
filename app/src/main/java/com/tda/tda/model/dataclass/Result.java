package com.tda.tda.model.dataclass;

import android.util.Log;

import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

public class Result<T> {

    public final static int CODE_COMMAND_FAILED = -1;
    public final static int CODE_DELETE_DEVICE = 1;
    public final static int CODE_BLUETOOTH_ON_OFF = 2;
    public final static int CODE_BLUETOOTH_LIST_DEVICE_USERS = 3;
    public final static int CODE_BLUETOOTH_CONNECT = 4;
    public final static int CODE_BLUETOOTH_NAME = 5;
    public final static int CODE_BLUETOOTH_ADD_NEW_USER = 6;
    public final static int CODE_BLUETOOTH_DELETE_USER = 7;
    public final static int CODE_BLUETOOTH_GET_USER_BY_FP = 8;
    public final static int CODE_BLUETOOTH_RESET_DEVICE = 9;
    public final static int CODE_BLUETOOTH_CHANGE_ADMIN = 10;
    public final static int CODE_CREATE_BACKUP = 11;
    public final static int CODE_CREATE_RESTORE = 12;
    public final static int CODE_BLUETOOTH_BOUND = 13;

    public int code;
    public boolean result;
    public List<T> data;
    public String stringData;

    public Result() {
    }

    public Result(int code, boolean result) {
        this.code = code;
        this.result = result;
    }

    public Result(int code, boolean result, String stringData) {
        this.code = code;
        this.result = result;
        this.stringData = stringData;
    }

    public Result(int code, boolean result, List<T> data) {
        this.code = code;
        this.result = result;
        this.data = data;
    }

}
