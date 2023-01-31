package com.tda.tda.ui.adddevice;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tda.tda.HomeActivity;
import com.tda.tda.MyBaseFragment;
import com.tda.tda.R;
import com.tda.tda.databinding.AddDeviceFragmentBinding;
import com.tda.tda.model.adapters.BluetoothAdapter;
import com.tda.tda.model.dialogs.ShowMessage;
import com.tda.tda.model.dialogs.adddevice.AddDeviceDialog;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddDeviceFragment extends MyBaseFragment {

    private AddDeviceViewModel mViewModel;
    private AddDeviceFragmentBinding binding;
    private BluetoothAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding=AddDeviceFragmentBinding.inflate(inflater,container,false);
        mViewModel = new ViewModelProvider(this).get(AddDeviceViewModel.class);

        initToolbar();
        initBluetoothName();
        initBluetoothButton();
        initBluetoothList();
        initButtonAdd();

        //Check Bluetooth Status at first
        mViewModel.checkBluetoothStatus();

        return binding.getRoot();
    }

    private void initToolbar(){
        ((HomeActivity)getActivity()).setSupportActionBar(binding.toolbar);
        ((HomeActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((HomeActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(view -> {
            getActivity().onBackPressed();
        });
    }

    private void initBluetoothName(){
        mViewModel.getBluetoothName().observe(getViewLifecycleOwner(), aString -> {
            binding.adddeviceBluetoothName.setText(aString);
        });
    }

    private void initBluetoothButton(){
        mViewModel.getBluetoothOn().observe(getViewLifecycleOwner(), aBoolean -> {
            if(aBoolean){
                binding.adddeviceSpinKit.setVisibility(View.VISIBLE);
                binding.adddeviceLoadingText.setVisibility(View.VISIBLE);
            }else{
                binding.adddeviceSpinKit.setVisibility(View.GONE);
                binding.adddeviceLoadingText.setVisibility(View.GONE);
            }
            binding.adddeviceBluetoothBtn.setEnabled(true);
            binding.adddeviceBluetoothBtn.setChecked(aBoolean);
            changeBTStatusLabel(aBoolean);
        });
        binding.adddeviceBluetoothBtn.setOnClickListener(view -> {
            binding.adddeviceBluetoothBtn.setEnabled(false);
            mViewModel.setBluetoothOnOrOff(binding.adddeviceBluetoothBtn.isChecked());
        });
    }

    private void initBluetoothList(){
        adapter=new BluetoothAdapter();
        binding.adddeviceDeviceList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.adddeviceDeviceList.setAdapter(adapter);

        mViewModel.getBluetoothList().observe(getViewLifecycleOwner(),bluetoothDevices -> {
            binding.adddeviceSpinKit.setVisibility(View.GONE);
            binding.adddeviceLoadingText.setVisibility(View.GONE);
            adapter.differ.submitList(bluetoothDevices);
            if(bluetoothDevices.size()>0){
                binding.adddeviceEmpty.setVisibility(View.GONE);
            }else{
                binding.adddeviceEmpty.setVisibility(View.VISIBLE);
            }
        });

    }

    private void initButtonAdd(){
        binding.adddeviceBtnAdd.setOnClickListener(view -> {
            validateToSaveDate();
        });
    }


    private void validateToSaveDate(){
        if(adapter.SelectedDevice.getName()==null || adapter.SelectedDevice.getName()==""){
            ((HomeActivity)getActivity()).showMessage.showMessageAlert("لطفا یک بلوتوث را انتخاب کنید",ShowMessage.ALERT_ERROR);
        }else{

            AddDeviceDialog addDeviceDialog=new AddDeviceDialog();
            addDeviceDialog.setIp(adapter.SelectedDevice.getIp());
            addDeviceDialog.setBluetoothName(adapter.SelectedDevice.getName());
            addDeviceDialog.show(getParentFragmentManager(), String.valueOf(R.string.add_device_dialog_tag));

        }
    }

    private void changeBTStatusLabel(Boolean isOn){
        if(isOn){
            binding.adddeviceBluetoothStatus.setText("روشن");
        }else{
            binding.adddeviceBluetoothStatus.setText("خاموش");
        }
    }


}