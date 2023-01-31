package com.tda.tda.model.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tda.tda.R;

public class Loading extends Dialog {
    public Loading(@NonNull Context context) {
        super(context,R.style.Theme_Dialog);
//        this.getWindow().setGravity(Gravity.BOTTOM);
//        this.getWindow().setDimAmount(0);
//        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading);
    }

    public Loading(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected Loading(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


}
