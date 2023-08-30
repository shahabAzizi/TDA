package com.tda.tda.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tda.tda.MyBaseViewModel;
import com.tda.tda.data.repositories.impl.DevicesRepository;
import com.tda.tda.data.repositories.impl.ServerRepository;
import com.tda.tda.helpers.DB.Models.Device;
import com.tda.tda.helpers.DB.Models.DeviceDetails;
import com.tda.tda.helpers.DB.Models.User;
import com.tda.tda.model.dataclass.ApiResult;
import com.tda.tda.model.dataclass.Devices;
import com.tda.tda.model.dataclass.Result;
import com.tda.tda.model.listeners.ServerListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class HomeViewModel extends MyBaseViewModel {
    // TODO: Implement the ViewModel

    private DevicesRepository devicesRepository;
    private ServerRepository serverRepository;
    private MutableLiveData<List<Devices>> deviceList;
    private MutableLiveData<Result> result;
    private MutableLiveData<Boolean> loading;

    @Inject
    public HomeViewModel(DevicesRepository devicesRepository, ServerRepository serverRepository){
        this.devicesRepository = devicesRepository;
        this.serverRepository=serverRepository;
        result=new MutableLiveData<>();
        deviceList=new MutableLiveData<>();
        loading=new MutableLiveData<>();
    }

    public void getDevices(){
        deviceList.postValue(devicesRepository.getDevices());
    }

    public LiveData<List<Devices>> getDeviceLiveData() {
        return (LiveData<List<Devices>>) deviceList;
    }
    public void checkUser(){
        checkUserInfo(devicesRepository);
    }

    public void startBackup(){
        User user = devicesRepository.getUser();
        if(user.need_to_update.equals("true")){
            List<Devices> devices=devicesRepository.getDevices();
            JSONArray devicesJson=new JSONArray();
            for (int i=0;i<devices.size();i++) {
                try {
                    String objStr="{"+"name:'"+devices.get(i).getName()+"',ip:'"+devices.get(i).getIp()+"'}";
                    JSONObject jsonObject = new JSONObject(objStr);
                    List<DeviceDetails> details=devicesRepository.getDeviceUsers(devices.get(i).getId());
                    objStr="[";
                    for(int j=0;j<details.size();j++){
                        if(j>0){objStr=objStr+",";}
                        objStr=objStr+"{"+"name:'"+details.get(j).name+"',fp_user_id:'"+details.get(j).fp_user_id+"',type:'"+details.get(j).type+"'}";
                    }
                    objStr=objStr+"]";
                    jsonObject.put("details",new JSONArray(objStr));
                    devicesJson.put(jsonObject);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            if(devicesJson.length()>0){
                loading.postValue(true);
                serverRepository.backup(user.token,devicesJson.toString(),listener);
            }else{
                devicesRepository.setUserBackup(new Date().toString(),false);
                result.postValue(new Result(Result.CODE_CREATE_BACKUP,false,"هیچ موردی برای بکاپ گیری پیدا نشد"));
            }
        }else{
            result.postValue(new Result(Result.CODE_CREATE_BACKUP,false,"تغییراتی برای بکاپ گیری یافت نشد"));
        }
    }

    public void startRestore(){
        User user = devicesRepository.getUser();
        loading.postValue(true);
        serverRepository.restore(user.token, new ServerListener() {
            @Override
            public void onResponse(ApiResult result1) {
                loading.postValue(false);
                new Thread(()->{
                    try{
                        JsonArray devices=result1.getData().getAsJsonArray();
                        if(devices.size()>0){
                            devicesRepository.resetDB();
                        }
                        for(int i=0;i<devices.size();i++){
                            JsonObject device=devices.get(i).getAsJsonObject();
                            devicesRepository.saveNewDevice(
                                    device.get("name").getAsString(),
                                    device.get("password").getAsString(),
                                    device.get("ip").getAsString(),
                                    device.get("bl_name").getAsString()
                            );
                            Device storeDevice=devicesRepository.getDeviceByNameOrIp(device.get("name").toString(),device.get("ip").toString());

                            JsonArray details=device.getAsJsonArray("details");
                            for(int j=0;j<details.size();j++){
                                JsonObject detail=details.get(j).getAsJsonObject();
                                devicesRepository.newUsers(
                                        storeDevice.id,
                                        detail.get("name").getAsString(),
                                        detail.get("fp_user_id").getAsString(),
                                        detail.get("type").getAsString()
                                );
                            }
                        }
                        if(devices.size()>0){
                            result.postValue(new Result(Result.CODE_CREATE_RESTORE, true, "ریستور با موفقیت انجام شد"));
                        }else{
                            result.postValue(new Result(Result.CODE_CREATE_RESTORE, false, "هیچ دیتایی بکاپ نشده است"));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }).start();

            }

            @Override
            public void onFailure(String message) {
                loading.postValue(false);
                result.postValue(new Result(Result.CODE_CREATE_RESTORE, false, message));
            }
        });
    }

    private ServerListener listener=new ServerListener() {
        @Override
        public void onResponse(ApiResult result1) {
            loading.postValue(false);
            result.postValue(new Result(Result.CODE_CREATE_BACKUP,true,"فرآیند بکاپ گیری با موفقیت انجام شد"));
            new Thread(()->{devicesRepository.setUserBackup(new Date().toString(),false);}).start();
        }

        @Override
        public void onFailure(String message) {
            if(message!=null) {
                loading.postValue(false);
                result.postValue(new Result(Result.CODE_CREATE_BACKUP, false, message));
            }
        }
    };

    public MutableLiveData<Result> getResult() { return result; }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }
}