package com.tda.tda.model.dialogs.changeadmin;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.tda.tda.HomeActivity;
import com.tda.tda.R;
import com.tda.tda.databinding.ResetDialogBinding;
import com.tda.tda.helpers.DB.Models.DeviceDetails;
import com.tda.tda.model.dataclass.Result;
import com.tda.tda.model.dialogs.ShowMessage;
import com.tda.tda.model.dialogs.resetdevice.ResetDialogViewModel;
import com.tda.tda.ui.device.DeviceFragment;

import org.json.JSONObject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ChangeAdminDialog extends DialogFragment {

    private ResetDialogViewModel viewModel;
    private ResetDialogBinding binding;
    private DeviceFragment deviceFragment;
    private int device_id = 0;
    private DeviceDetails user;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ResetDialogBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(ResetDialogViewModel.class);

        initResult();
        initButtonRefresh();
        start();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
    }

    private void init() {
        binding.resetSpinKitStepZero.setVisibility(View.VISIBLE);
        binding.resetStepZero.setTextColor(getResources().getColor(R.color.gray_light));
        binding.resetRefresh.setVisibility(View.GONE);
        binding.resetSpinKitStepOne.setVisibility(View.GONE);
        binding.resetStepOne.setTextColor(getResources().getColor(R.color.gray_light));
    }

    private void initButtonRefresh() {
        binding.resetRefresh.setOnClickListener(view -> {
            start();
        });
    }

    private void initResult() {
        viewModel.getResult().observe(getViewLifecycleOwner(), result -> {
            if (result.code == Result.CODE_BLUETOOTH_CHANGE_ADMIN) {
                if (result.result) {
                    ((HomeActivity) requireActivity()).showMessage.showMessageAlert("تغییر مدیر انجام گردید", ShowMessage.ALERT_SUCCESS);
                    ChangeAdminDialog.this.dismiss();
                } else {
                    ((HomeActivity) requireActivity()).showMessage.showMessageAlert("تغییر انجام نشد. دوباره تلاش کنید", ShowMessage.ALERT_ERROR);
                }
            }
        });
    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    private void start() {
        init();
        deviceFragment.changeAdmin(user.fp_user_id);
    }

    public void onRecieveData(boolean res, String data) {
        try {
            Log.i("BTK","Result : "+res);
            if (res) {
                JSONObject state = new JSONObject(data);
                Log.e("Frg", state.toString());
                if (state.has("state")) {
                    switch (state.getString("state")) {
                        case "04":
                            if (state.getString("res").equals("00")) { //Get Admin Finger Print
                                binding.resetSpinKitStepZero.setVisibility(View.GONE);
                                binding.resetStepZero.setTextColor(getResources().getColor(R.color.colorPrimary));
                                binding.resetSpinKitStepOne.setVisibility(View.VISIBLE);
                                binding.resetStepOne.setTextColor(getResources().getColor(R.color.gray_light));
                            }
                            if (state.getString("res").equals("-1")) { // Admin Finger Print Failed
                                binding.resetSpinKitStepOne.setVisibility(View.GONE);
                                binding.resetStepOne.setTextColor(getResources().getColor(R.color.red));
                                binding.resetRefresh.setVisibility(View.VISIBLE);
                            }
                            if (state.getString("res").equals("01")) { // Reset Success !!
                                binding.resetSpinKitStepOne.setVisibility(View.GONE);
                                binding.resetStepOne.setTextColor(getResources().getColor(R.color.colorPrimary));
                            }
                            if (state.getString("res").equals("02")) { // Reset Success !!
                                ((HomeActivity)requireActivity()).showMessage.showMessageAlert("تغییر مدیر انجام شد",ShowMessage.ALERT_SUCCESS);
                                ChangeAdminDialog.this.dismiss();
                            }
                            if (state.getString("res").equals("-2")) { // Reset Success !!
                                ((HomeActivity)requireActivity()).showMessage.showMessageAlert("تغییر مدیر انجام نشد",ShowMessage.ALERT_ERROR);
                                ChangeAdminDialog.this.dismiss();
                            }
                            break;
                        case "000":
                            break;
                    }
                }
            }else{
                init();
                binding.resetSpinKitStepZero.setVisibility(View.GONE);
                binding.resetRefresh.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            init();
            binding.resetRefresh.setVisibility(View.VISIBLE);
            ((HomeActivity) requireActivity()).showMessage.showMessageAlert("لطفا دوباره تلاش کنید", ShowMessage.ALERT_ERROR);
            e.printStackTrace();
        }
    }


    public void setFragment(DeviceFragment deviceFragment) {
        this.deviceFragment = deviceFragment;
    }

    @Override
    public int getTheme() {
        return R.style.Theme_Dialog;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public void setUser(DeviceDetails user) {
        this.user = user;
    }
}
