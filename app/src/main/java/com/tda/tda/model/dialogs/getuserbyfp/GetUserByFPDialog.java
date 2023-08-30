package com.tda.tda.model.dialogs.getuserbyfp;

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
import com.tda.tda.databinding.GetUserByFpDialogBinding;
import com.tda.tda.helpers.DB.Models.DeviceDetails;
import com.tda.tda.model.dataclass.Result;
import com.tda.tda.model.dialogs.ShowMessage;
import com.tda.tda.ui.device.DeviceFragment;

import org.json.JSONObject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class GetUserByFPDialog extends DialogFragment {

    private GetUserByFPDialogViewModel viewModel;
    private GetUserByFpDialogBinding binding;
    private DeviceFragment deviceFragment;
    private int device_id = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= GetUserByFpDialogBinding.inflate(inflater,container,false);
        viewModel= new ViewModelProvider(this).get(GetUserByFPDialogViewModel.class);

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
        binding.getUserByFpRefresh.setVisibility(View.GONE);
        binding.getUserByFpSpinKitStepZero.setVisibility(View.VISIBLE);
        binding.getUserByFpStepZero.setTextColor(getResources().getColor(R.color.gray_light));
        binding.getUserByFpSpinKitStepOne.setVisibility(View.GONE);
        binding.getUserByFpStepOne.setTextColor(getResources().getColor(R.color.gray_light));
        binding.getUserByFpSpinKitStepTwo.setVisibility(View.GONE);
        binding.getUserByFpStepTwo.setTextColor(getResources().getColor(R.color.gray_light));
    }

    private void initButtonRefresh(){
        binding.getUserByFpRefresh.setOnClickListener(view -> {
            start();
        });
    }

    private void initResult(){
        viewModel.getResult().observe(getViewLifecycleOwner(),result -> {
            if(result.code== Result.CODE_BLUETOOTH_GET_USER_BY_FP){
                if(result.result){
                    ((HomeActivity)requireActivity()).showMessage.showMessageAlert("کاربر ثبت گردید",ShowMessage.ALERT_SUCCESS);
                    GetUserByFPDialog.this.dismiss();
                    deviceFragment.initUserList();
                }else if(!result.stringData.equals("-1")){
                    ((HomeActivity)requireActivity()).showMessage.showMessageAlert("این اثرانگشت قبلا در گوشی با نام " + result.stringData+" ثبت شده است ",ShowMessage.ALERT_SUCCESS);
                    GetUserByFPDialog.this.dismiss();
                }else{
                    ((HomeActivity)requireActivity()).showMessage.showMessageAlert("کاربر ثبت نشد. دوباره تلاش کنید",ShowMessage.ALERT_ERROR);
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
        deviceFragment.getUserByFP();
    }

    public void onRecieveData(boolean res,String data){
        try {
            if(res) {
                JSONObject state = new JSONObject(data);
                Log.e("Frg", state.toString());
                if (state.has("state")) {
                    switch (state.getString("state")) {
                        case "02":
                            if (state.getString("res").equals("00")) { //Get Admin Finger Print
                                binding.getUserByFpSpinKitStepZero.setVisibility(View.GONE);
                                binding.getUserByFpStepZero.setTextColor(getResources().getColor(R.color.colorPrimary));
                                binding.getUserByFpSpinKitStepOne.setVisibility(View.VISIBLE);
                                binding.getUserByFpStepOne.setTextColor(getResources().getColor(R.color.gray_light));
                            }
                            if (state.getString("res").equals("01")) { //Admin Finger Print Accept
                                binding.getUserByFpSpinKitStepOne.setVisibility(View.GONE);
                                binding.getUserByFpStepOne.setTextColor(getResources().getColor(R.color.colorPrimary));
                                binding.getUserByFpSpinKitStepTwo.setVisibility(View.VISIBLE);
                                binding.getUserByFpStepTwo.setTextColor(getResources().getColor(R.color.gray_light));
                            }
                            if (state.getString("res").equals("-1")) { // Admin Finger Print Failed
                                binding.getUserByFpSpinKitStepOne.setVisibility(View.GONE);
                                binding.getUserByFpStepOne.setTextColor(getResources().getColor(R.color.red));
                                binding.getUserByFpRefresh.setVisibility(View.VISIBLE);
                            }
                            if (state.getString("res").equals("-2")) { // Delete ID from FP device not Success
                                binding.getUserByFpSpinKitStepTwo.setVisibility(View.GONE);
                                binding.getUserByFpStepTwo.setTextColor(getResources().getColor(R.color.red));
                                GetUserByFPDialog.this.dismiss();
                                ((HomeActivity)requireActivity()).showMessage.showMessageAlert("اثر انگشت در دستگاه یافت نشد",ShowMessage.ALERT_ERROR);
                            }
                            if (state.getString("res").equals("02")) { // Deleteing Success !!
                                binding.getUserByFpSpinKitStepTwo.setVisibility(View.GONE);
                                binding.getUserByFpStepTwo.setTextColor(getResources().getColor(R.color.colorPrimary));
                                saveUser(state.getString("data"));
                            }
                            break;
                        case "000":
                            break;
                    }
                }
            }else{
                init();
                binding.getUserByFpSpinKitStepZero.setVisibility(View.GONE);
                binding.getUserByFpRefresh.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            init();
            binding.getUserByFpRefresh.setVisibility(View.VISIBLE);
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

    private void saveUser(String fp){
        new Thread(()->{
            viewModel.newUserByFP(device_id,fp);
        }).start();
    }

}
