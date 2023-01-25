package com.tda.tda.UiAssets;

import android.app.Activity;
import android.content.Context;

import com.andreseko.SweetAlert.SweetAlertDialog;

import java.util.logging.Handler;

import javax.inject.Inject;

public class ShowMessage {

    private Activity context;
    private String text="";
    private int alertType=0;
    public static final int ALERT_ERROR=0;
    public static final int ALERT_SUCCESS=1;

    @Inject
    public ShowMessage(){

    }

    public void showMessageAlert(String text,int alertType){
        if(alertType==ALERT_ERROR){
            new SweetAlertDialog(context,SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(text)
                    .show();
        }else{
            new SweetAlertDialog(context,SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(text)
                    .show();
        }
    }


}
