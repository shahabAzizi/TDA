package com.tda.tda.model.dialogs.deleteuser;

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
import com.tda.tda.helpers.DB.Models.DeviceDetails;
import com.tda.tda.model.dataclass.Result;
import com.tda.tda.model.dialogs.ShowMessage;
import com.tda.tda.ui.device.DeviceFragment;

import org.json.JSONObject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DeleteUserDialog extends DialogFragment {

    private DeleteUserDialogViewModel viewModel;
    private DeleteUserDialogBinding binding;
    private DeviceFragment deviceFragment;
    private int device_id = 0;
    private DeviceDetails user ;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= DeleteUserDialogBinding.inflate(inflater,container,false);
        viewModel= new ViewModelProvider(this).get(DeleteUserDialogViewModel.class);

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
        binding.deleteUserSpinKitStepZero.setVisibility(View.VISIBLE);
        binding.deleteUserStepZero.setTextColor(getResources().getColor(R.color.gray_light));
        binding.deleteUserRefresh.setVisibility(View.GONE);
        binding.deleteUserSpinKitStepOne.setVisibility(View.GONE);
        binding.deleteUserStepOne.setTextColor(getResources().getColor(R.color.gray_light));
    }

    private void initButtonRefresh(){
        binding.deleteUserRefresh.setOnClickListener(view -> {
            start();
        });
    }

    private void initResult(){
        viewModel.getResult().observe(getViewLifecycleOwner(),result -> {
            if(result.code== Result.CODE_BLUETOOTH_DELETE_USER){
                if(result.result){
                    ((HomeActivity)requireActivity()).showMessage.showMessageAlert("کاربر حذف گردید",ShowMessage.ALERT_SUCCESS);
                    DeleteUserDialog.this.dismiss();
                    deviceFragment.initUserList();
                }else{
                    ((HomeActivity)requireActivity()).showMessage.showMessageAlert("کاربر حذف نشد. دوباره تلاش کنید",ShowMessage.ALERT_ERROR);
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
        deviceFragment.deleteUser(user);
    }

    public void onRecieveData(boolean res,String data){
        try {
            if(res) {
                JSONObject state = new JSONObject(data);
                Log.e("Frg", state.toString());
                if (state.has("state")) {
                    switch (state.getString("state")) {
                        case "01":
                            if (state.getString("res").equals("00")) { //Get Admin Finger Print
                                binding.deleteUserSpinKitStepZero.setVisibility(View.GONE);
                                binding.deleteUserStepZero.setTextColor(getResources().getColor(R.color.colorPrimary));
                                binding.deleteUserSpinKitStepOne.setVisibility(View.VISIBLE);
                                binding.deleteUserStepOne.setTextColor(getResources().getColor(R.color.gray_light));
                            }
                            if (state.getString("res").equals("01")) { //Admin Finger Print Accept
                                binding.deleteUserSpinKitStepOne.setVisibility(View.GONE);
                                binding.deleteUserStepOne.setTextColor(getResources().getColor(R.color.colorPrimary));
                            }
                            if (state.getString("res").equals("-1")) { // Admin Finger Print Failed
                                binding.deleteUserSpinKitStepOne.setVisibility(View.GONE);
                                binding.deleteUserStepOne.setTextColor(getResources().getColor(R.color.red));
                                binding.deleteUserRefresh.setVisibility(View.VISIBLE);
                            }
                            if (state.getString("res").equals("-2")) { // Delete ID from FP device not Success
                                DeleteUserDialog.this.dismiss();
                                ((HomeActivity)requireActivity()).showMessage.showMessageAlert("اثر انگشت در دستگاه قابل خذف نیست. کد کاربر در دستگاه یافت نشد",ShowMessage.ALERT_ERROR);
                            }
                            if (state.getString("res").equals("-3")) { // Delete ID of Admin FP, failed !!
                                DeleteUserDialog.this.dismiss();
                                ((HomeActivity)requireActivity()).showMessage.showMessageAlert("اثر انگشت مدیر قابل خذف نیست",ShowMessage.ALERT_ERROR);
                            }
                            if (state.getString("res").equals("02")) { // Deleteing Success !!
                                binding.deleteUserSpinKitStepOne.setVisibility(View.GONE);
                                binding.deleteUserStepOne.setTextColor(getResources().getColor(R.color.colorPrimary));
                                viewModel.deleteUser(user.id);
                            }
                            break;
                        case "000":
                            break;
                    }
                }
            }else{
                init();
                binding.deleteUserSpinKitStepZero.setVisibility(View.GONE);
                binding.deleteUserRefresh.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            init();
            binding.deleteUserRefresh.setVisibility(View.VISIBLE);
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

    public void setUser(DeviceDetails user) {
        this.user = user;
    }
}
