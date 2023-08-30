package com.tda.tda.model.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.KeyEvent;

import com.andreseko.SweetAlert.SweetAlertDialog;
import com.tda.tda.model.listeners.ConfirmListener;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class ShowMessage {

    private Context context;
    public static final int ALERT_ERROR=0;
    public static final int ALERT_SUCCESS=1;

    public ShowMessage(Context context){
        this.context=context;
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

    public void showConfirm(String text, ConfirmListener callback){
        new SweetAlertDialog(context)
                .setTitleText(text)
                .setCancelButton("خیر", Dialog::dismiss)
                .setConfirmButton("بله",sweetAlertDialog -> {
                callback.onConfirmed(sweetAlertDialog);
        }).show();
    }


}
