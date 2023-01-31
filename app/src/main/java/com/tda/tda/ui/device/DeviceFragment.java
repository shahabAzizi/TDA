package com.tda.tda.ui.device;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tda.tda.HomeActivity;
import com.tda.tda.MyBaseFragment;
import com.tda.tda.R;
import com.tda.tda.databinding.AddDeviceFragmentBinding;
import com.tda.tda.databinding.DeviceFragmentBinding;
import com.tda.tda.model.adapters.BluetoothAdapter;
import com.tda.tda.model.adapters.DeviceAdapter;
import com.tda.tda.model.adapters.UsersAdapter;
import com.tda.tda.model.dialogs.ShowMessage;
import com.tda.tda.model.dialogs.adddevice.AddDeviceDialog;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DeviceFragment extends MyBaseFragment {

    private DeviceViewModel mViewModel;
    private DeviceFragmentBinding binding;
    private UsersAdapter adapter;
    private DeviceFragmentArgs args;
    private int triesToConnect = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DeviceFragmentBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(DeviceViewModel.class);

        args = DeviceFragmentArgs.fromBundle(getArguments());

        initToolbar();
        initBluetoothName();
        initBluetoothButton();
        initConnectionButton();
        initBluetoothList();
        initButtonAdd();

        //Check Bluetooth Status at first
        mViewModel.checkBluetoothStatus();

        return binding.getRoot();
    }

    private void initToolbar() {
        ((HomeActivity) getActivity()).setSupportActionBar(binding.toolbar);
        ((HomeActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((HomeActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(view -> {
            getActivity().onBackPressed();
        });
    }

    private void initBluetoothName() {
        mViewModel.getBluetoothName().observe(getViewLifecycleOwner(), aString -> {
            binding.deviceBluetoothName.setText(aString);
        });
        binding.deviceBluetoothDeviceName.setText(args.getDeviceName());
    }

    private void initBluetoothButton() {
        mViewModel.getBluetoothOn().observe(getViewLifecycleOwner(), aBoolean -> {
            binding.deviceBluetoothBtn.setChecked(aBoolean);
            binding.deviceBluetoothBtn.setEnabled(true);
            changeBTStatusLabel(aBoolean);
        });
        binding.deviceBluetoothBtn.setOnClickListener(view -> {
            mViewModel.setBluetoothOnOrOff(binding.deviceBluetoothBtn.isChecked());
            binding.deviceBluetoothBtn.setEnabled(false);
        });
    }

    private void initConnectionButton() {
        binding.deviceBtnConnection.setOnClickListener(view -> {
            if(mViewModel.isBluetoothTurnOn()) {
                startConnection();
            }else{
                ((HomeActivity)getActivity()).showMessage.showMessageAlert("لطفا بلوتوث را روشن کنید",ShowMessage.ALERT_ERROR);
            }
        });
        mViewModel.getIsConnected().observe(getViewLifecycleOwner(), aBoolean -> {
            binding.deviceBtnConnection.setEnabled(true);
            binding.deviceSpinKit.setVisibility(View.GONE);
            if (aBoolean) {
                binding.deviceBtnConnection.setText(getResources().getString(R.string.device_btn_connection_success));
                binding.deviceBtnConnection.setBackground(getResources().getDrawable(R.drawable.style_btn_connection_fail));
                binding.deviceBluetoothDeviceStatus.setText(getResources().getString(R.string.connection_success));
                binding.deviceBluetoothStatusView.setBackground(getResources().getDrawable(R.drawable.style_turnon_round_txt));
            } else {
                binding.deviceBtnConnection.setText(getResources().getString(R.string.device_btn_connection_fail));
                binding.deviceBtnConnection.setBackground(getResources().getDrawable(R.drawable.style_btn_connection_success));
                binding.deviceBluetoothDeviceStatus.setText(getResources().getString(R.string.connection_fail));
                binding.deviceBluetoothStatusView.setBackground(getResources().getDrawable(R.drawable.style_turnoff_round_txt));
            }
        });
        mViewModel.getShowConnectionError().observe(getViewLifecycleOwner(),aBoolean -> {
            ((HomeActivity)getActivity()).showMessage.showMessageAlert("امکان اتصال وجود ندارد!" + "\n" + "یکبار بلوتوث گوشی یا دستگاه را خاموش و سپس روشن کنید" ,ShowMessage.ALERT_ERROR);
        });
    }

    private void initBluetoothList() {
        adapter = new UsersAdapter();
        binding.deviceDeviceList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.deviceDeviceList.setAdapter(adapter);

        mViewModel.getBluetoothList().observe(getViewLifecycleOwner(), bluetoothDevices -> {
            binding.deviceSpinKit.setVisibility(View.GONE);
            adapter.differ.submitList(bluetoothDevices);
            if (bluetoothDevices.size() > 0) {
                binding.deviceEmpty.setVisibility(View.GONE);
            } else {
                binding.deviceEmpty.setVisibility(View.VISIBLE);
            }
        });

        mViewModel.getDeviceUsers(args.getDeviceId());
    }

    private void initButtonAdd() {
        binding.deviceBtnAdd.setOnClickListener(view -> {

        });
    }

    private void startConnection() {
        binding.deviceBtnConnection.setEnabled(false);
        binding.deviceSpinKit.setVisibility(View.VISIBLE);
        binding.deviceBluetoothStatusView.setBackground(getResources().getDrawable(R.drawable.style_status_round_txt));
        binding.deviceBluetoothDeviceStatus.setText(getResources().getText(R.string.connection_progress));
        new Thread(() -> {
            mViewModel.connectToDevice(args.getDeviceIp());
        }).start();
    }


    private void changeBTStatusLabel(Boolean isOn) {
        if (isOn) {
            binding.deviceBluetoothStatus.setText("روشن");
        } else {
            binding.deviceBluetoothStatus.setText("خاموش");
        }
    }


}