package com.tda.tda.model.dialogs.newuser;

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
import com.tda.tda.databinding.LayoutDialogAddDeviceBinding;
import com.tda.tda.databinding.NewUserDialogBinding;
import com.tda.tda.helpers.DB.Models.DeviceDetails;
import com.tda.tda.model.dataclass.Result;
import com.tda.tda.model.dialogs.ShowMessage;
import com.tda.tda.ui.device.DeviceFragment;

import org.json.JSONException;
import org.json.JSONObject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NewUserDialog extends DialogFragment {

    private NewUserDialogViewModel viewModel;
    private NewUserDialogBinding binding;
    private DeviceFragment deviceFragment;
    private int device_id = 0;
    private int fp_id = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= NewUserDialogBinding.inflate(inflater,container,false);
        viewModel= new ViewModelProvider(this).get(NewUserDialogViewModel.class);

        initResult();
        initButtonRefresh();
        initButtonAdd();
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
    private void init(){
        binding.newUserRefresh.setVisibility(View.GONE);
        binding.newUserSpinKitStepZero.setVisibility(View.VISIBLE);
        binding.newUserStepZero.setTextColor(getResources().getColor(R.color.gray_light));
        binding.newUserSpinKitStepOne.setVisibility(View.GONE);
        binding.newUserStepOne.setTextColor(getResources().getColor(R.color.gray_light));
        binding.newUserSpinKitStepTwo.setVisibility(View.GONE);
        binding.newUserStepTwo.setTextColor(getResources().getColor(R.color.gray_light));
        binding.newUserAdd.setEnabled(false);
    }

    private void initButtonRefresh(){
        binding.newUserRefresh.setOnClickListener(view -> {
            start();
        });
    }

    private void initResult(){
        viewModel.getResult().observe(getViewLifecycleOwner(),result -> {
            if(result.code== Result.CODE_BLUETOOTH_ADD_NEW_USER){
                if(result.result){
                    ((HomeActivity)requireActivity()).showMessage.showMessageAlert("کاربر ثبت گردید",ShowMessage.ALERT_SUCCESS);
                    NewUserDialog.this.dismiss();
                    deviceFragment.initUserList();
                }else{
                    ((HomeActivity)requireActivity()).showMessage.showMessageAlert("کاربر ثبت نشد. دوباره تلاش کنید",ShowMessage.ALERT_ERROR);
                }
            }
        });
    }

    private void initButtonAdd(){
        binding.newUserAdd.setOnClickListener(view -> {
            binding.newUserName.setError(null);
            if(binding.newUserName.getText().toString().length()>0){
                new Thread(()->{
                    viewModel.saveNewUser(device_id,binding.newUserName.getText().toString(),String.valueOf(fp_id), DeviceDetails.TYPE_USER_NORMAL);
                }).start();
            }else{
                binding.newUserName.setError("پر کردن این فیلد الزامی است");
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    private void start(){
        init();
        deviceFragment.newUser();
    }

    public void onRecieveData(boolean res,String data){
        try {
            if(res) {
                JSONObject state = new JSONObject(data);
                Log.e("Frg", state.toString());
                if (state.has("state")) {
                    switch (state.getString("state")) {
                        case "00":
                            if (state.getString("res").equals("00")) {
                                binding.newUserSpinKitStepZero.setVisibility(View.GONE);
                                binding.newUserStepZero.setTextColor(getResources().getColor(R.color.colorPrimary));
                                binding.newUserSpinKitStepOne.setVisibility(View.VISIBLE);
                                binding.newUserStepOne.setTextColor(getResources().getColor(R.color.gray_light));
                            }
                            if (state.getString("res").equals("01")) {
                                binding.newUserSpinKitStepOne.setVisibility(View.GONE);
                                binding.newUserStepOne.setTextColor(getResources().getColor(R.color.colorPrimary));
                                binding.newUserSpinKitStepTwo.setVisibility(View.VISIBLE);
                                binding.newUserStepTwo.setTextColor(getResources().getColor(R.color.gray_light));
                            }
                            if (state.getString("res").equals("-1")) {
                                binding.newUserSpinKitStepOne.setVisibility(View.GONE);
                                binding.newUserStepOne.setTextColor(getResources().getColor(R.color.red));
                                binding.newUserRefresh.setVisibility(View.VISIBLE);
                            }
                            if (state.getString("res").equals("-2")) {
                                binding.newUserSpinKitStepTwo.setVisibility(View.GONE);
                                binding.newUserStepTwo.setTextColor(getResources().getColor(R.color.red));
                                binding.newUserRefresh.setVisibility(View.VISIBLE);
                            }
                            if (state.getString("res").equals("02")) {
                                fp_id = Integer.parseInt(state.getString("data"));
                                Log.e("NUSER","FP ID : "+fp_id);
                                binding.newUserSpinKitStepTwo.setVisibility(View.GONE);
                                binding.newUserStepTwo.setTextColor(getResources().getColor(R.color.colorPrimary));
                                binding.newUserAdd.setEnabled(true);
                            }
                            break;
                        case "01":
                            break;
                    }
                }
            }else{
                init();
                binding.newUserSpinKitStepZero.setVisibility(View.GONE);
                binding.newUserRefresh.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            init();
            binding.newUserRefresh.setVisibility(View.VISIBLE);
            ((HomeActivity)requireActivity()).showMessage.showMessageAlert("لطفا دوباره تلاش کنید",ShowMessage.ALERT_ERROR);
            e.printStackTrace();
        }
    }

    public void setFragment(DeviceFragment deviceFragment){
        this.deviceFragment = deviceFragment;
    }

    @Override
    public int getTheme() {
        return R.style.Theme_Dialog;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }
}
