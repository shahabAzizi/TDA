package com.tda.tda.model.dialogs.resetdevice;

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
import com.tda.tda.databinding.DeleteUserDialogBinding;
import com.tda.tda.databinding.ResetDialogBinding;
import com.tda.tda.helpers.DB.Models.DeviceDetails;
import com.tda.tda.model.dataclass.Result;
import com.tda.tda.model.dialogs.ShowMessage;
import com.tda.tda.model.dialogs.deleteuser.DeleteUserDialogViewModel;
import com.tda.tda.ui.device.DeviceFragment;

import org.json.JSONObject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ResetDialog extends DialogFragment {

    private ResetDialogViewModel viewModel;
    private ResetDialogBinding binding;
    private DeviceFragment deviceFragment;
    private int device_id = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= ResetDialogBinding.inflate(inflater,container,false);
        viewModel= new ViewModelProvider(this).get(ResetDialogViewModel.class);

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
    private void init(){
        binding.resetSpinKitStepZero.setVisibility(View.VISIBLE);
        binding.resetStepZero.setTextColor(getResources().getColor(R.color.gray_light));
        binding.resetRefresh.setVisibility(View.GONE);
        binding.resetSpinKitStepOne.setVisibility(View.GONE);
        binding.resetStepOne.setTextColor(getResources().getColor(R.color.gray_light));
    }

    private void initButtonRefresh(){
        binding.resetRefresh.setOnClickListener(view -> {
            start();
        });
    }

    private void initResult(){
        viewModel.getResult().observe(getViewLifecycleOwner(),result -> {
            if(result.code== Result.CODE_BLUETOOTH_RESET_DEVICE){
                if(result.result){
                    ((HomeActivity)requireActivity()).showMessage.showMessageAlert("دستگاه ریست گردید",ShowMessage.ALERT_SUCCESS);
                    ResetDialog.this.dismiss();
                    deviceFragment.initUserList();
                }else{
                    ((HomeActivity)requireActivity()).showMessage.showMessageAlert("دستگاه ریست نشد. دوباره تلاش کنید",ShowMessage.ALERT_ERROR);
                }
            }
        });
    }



    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    private void start(){
        init();
        deviceFragment.resetDevice();
    }

    public void onRecieveData(boolean res,String data){
        try {
            if(res) {
                JSONObject state = new JSONObject(data);
                Log.e("Frg", state.toString());
                if (state.has("state")) {
                    switch (state.getString("state")) {
                        case "03":
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
                                viewModel.resetDevice(device_id);
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
