package com.tda.tda.model.listeners;

import com.tda.tda.model.dataclass.ApiResult;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public interface ServerListener {
    public void onResponse(ApiResult result);
    public void onFailure(String message);
}
