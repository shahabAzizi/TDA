package com.tda.tda.model.dialogs.adddevice;

import android.os.Bundle;
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
import com.tda.tda.model.dialogs.ShowMessage;
import com.tda.tda.model.listeners.AddDeviceListener;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddDeviceDialog extends DialogFragment {

    private AddDeviceDialogViewModel viewModel;
    private LayoutDialogAddDeviceBinding binding;
    private String ip;
    private String bluetoothName;
    private AddDeviceListener addDeviceListener;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= LayoutDialogAddDeviceBinding.inflate(inflater,container,false);
        viewModel= new ViewModelProvider(this).get(AddDeviceDialogViewModel.class);


        initResult();

        binding.adddeviceDialogBtnAdd.setOnClickListener(view -> {
            saveDevice();
        });

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

    private void saveDevice(){
        binding.adddeviceDialogName.setError(null);
        binding.adddeviceDialogPassword.setError(null);
        if(binding.adddeviceDialogName.getText().toString().length()>1 &&
                binding.adddeviceDialogPassword.getText().toString().length()>1){

            new Thread(() -> {
                viewModel.saveNewDevice(
                        binding.adddeviceDialogName.getText().toString(),
                        binding.adddeviceDialogPassword.getText().toString(),
                        bluetoothName,
                        ip
                );
            }).start();

        }else{
            if(binding.adddeviceDialogName.getText().toString().length()<=1)binding.adddeviceDialogName.setError("مقدار این فیلد مجاز نیست");
            if(binding.adddeviceDialogPassword.getText().toString().length()<=1)binding.adddeviceDialogPassword.setError("مقدار این فیلد مجاز نیست");
        }
    }

    private void initResult(){
        viewModel.getResult().observe(getViewLifecycleOwner(),aBoolean -> {
            if(aBoolean){
                ((HomeActivity)requireActivity()).showMessage.showMessageAlert("دستگاه با موفقیت ثبت شد", ShowMessage.ALERT_SUCCESS);
                AddDeviceDialog.this.getDialog().dismiss();
                if(this.addDeviceListener!=null){addDeviceListener.onDeviceAdd();}
            }else{
                ((HomeActivity)requireActivity()).showMessage.showMessageAlert("نام دستگاه یا آدرس تکراری است",ShowMessage.ALERT_ERROR);
            }
        });
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setBluetoothName(String bluetoothName) {
        this.bluetoothName = bluetoothName;
    }

    @Override
    public int getTheme() {
        return R.style.Theme_Dialog;
    }

    public void setAddDeviceListener(AddDeviceListener addDeviceListener) {
        this.addDeviceListener = addDeviceListener;
    }
}
