package com.tda.tda.ui.device;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tda.tda.HomeActivity;
import com.tda.tda.MyBaseFragment;
import com.tda.tda.R;
import com.tda.tda.databinding.AddDeviceFragmentBinding;
import com.tda.tda.databinding.DeviceFragmentBinding;
import com.tda.tda.helpers.BluetoothHelper;
import com.tda.tda.helpers.DB.Models.Device;
import com.tda.tda.helpers.DB.Models.DeviceDetails;
import com.tda.tda.model.adapters.BluetoothAdapter;
import com.tda.tda.model.adapters.DeviceAdapter;
import com.tda.tda.model.adapters.UsersAdapter;
import com.tda.tda.model.dataclass.Result;
import com.tda.tda.model.dialogs.ShowMessage;
import com.tda.tda.model.dialogs.adddevice.AddDeviceDialog;
import com.tda.tda.model.dialogs.changeadmin.ChangeAdminDialog;
import com.tda.tda.model.dialogs.deleteuser.DeleteUserDialog;
import com.tda.tda.model.dialogs.getuserbyfp.GetUserByFPDialog;
import com.tda.tda.model.dialogs.newuser.NewUserDialog;
import com.tda.tda.model.dialogs.resetdevice.ResetDialog;
import com.tda.tda.ui.adddevice.AddDeviceFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DeviceFragment extends MyBaseFragment implements DialogInterface {

    private DeviceViewModel mViewModel;
    private DeviceFragmentBinding binding;
    private UsersAdapter adapter;
    private DeviceFragmentArgs args;
    private NewUserDialog newUserDialog;
    private DeleteUserDialog deleteUserDialog;
    private GetUserByFPDialog getUserByFPDialog;
    private ResetDialog resetDialog;
    private ChangeAdminDialog changeAdminDialog;
    private boolean isConnected = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DeviceFragmentBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(DeviceViewModel.class);

        args = DeviceFragmentArgs.fromBundle(getArguments());

        initBluetoothName();
        initBluetoothButton();
        initConnectionButton();
        initBluetoothList();
        initButtonAdd();
        initResult();

        //Check Bluetooth Status at first
        mViewModel.prepareBluetooth();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        initToolbar();
        initUserList();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void initToolbar() {

        ((HomeActivity) requireActivity()).toolbarMenu = R.menu.home;
        ((HomeActivity) requireActivity()).getDelegate().setSupportActionBar(binding.toolbar);
        ((HomeActivity) requireActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((HomeActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.toolbar.setNavigationOnClickListener(view -> {
            getActivity().onBackPressed();
        });
        binding.toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    prepareDeleteDevice();
                    break;
                case R.id.action_get_users_from_device:
                    prepareGetUserByFP();
                    break;
                case R.id.action_reset_device:
                    prepareResetDevice();
                    break;
            }
            return true;
        });
    }

    private void initBluetoothName() {
        binding.deviceBluetoothDeviceName.setText(args.getDeviceName());
    }

    private void initBluetoothButton() {
        mViewModel.getBluetoothOn().observe(getViewLifecycleOwner(), aBoolean -> {
            binding.deviceBluetoothBtn.setChecked(aBoolean);
            changeBTStatusLabel(aBoolean);
        });
        binding.deviceBluetoothBtn.setOnClickListener(view -> {
            mViewModel.setBluetoothOnOrOff(DeviceFragment.this,binding.deviceBluetoothBtn.isChecked());
        });
    }

    private void changeBTStatusLabel(Boolean isOn) {
        if (isOn) {
            binding.deviceBluetoothStatus.setText("روشن");
        } else {
            binding.deviceBluetoothStatus.setText("خاموش");
        }
    }

    private void initConnectionButton() {
        binding.deviceBtnConnection.setOnClickListener(view -> {
            if (mViewModel.getBluetoothOn().getValue()) {
                ((HomeActivity)requireActivity()).checkBluetoothConnectPermission(requireActivity());
                startConnection();
            } else {
                ((HomeActivity) requireActivity()).showMessage.showMessageAlert("لطفا بلوتوث را روشن کنید", ShowMessage.ALERT_ERROR);
            }
        });
    }

    private void initBluetoothList() {
        adapter = new UsersAdapter();
        binding.deviceDeviceList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.deviceDeviceList.setAdapter(adapter);
        adapter.setListener((item, object) -> {
            if(item == R.id.action_user_delete)prepareDeleteUser(object);
            if(item == R.id.action_user_admin)prepareNewAdmin(object);
        });
    }

    public void initUserList(){
        mViewModel.getUsersList().observe(getViewLifecycleOwner(),deviceDetails -> {
            adapter.differ.submitList(deviceDetails);
            if(deviceDetails.size()>0){
                binding.deviceEmpty.setVisibility(View.GONE);
            }else{
                binding.deviceEmpty.setVisibility(View.VISIBLE);
            }
        });
        new Thread(()->{
            mViewModel.getDeviceUsersList(args.getDeviceId());
        }).start();
    }

    private void initButtonAdd() {
        binding.deviceBtnAdd.setOnClickListener(view -> {
            if(isConnected) {
                newUserDialog = new NewUserDialog();
                newUserDialog.setFragment(this);
                newUserDialog.setDevice_id(args.getDeviceId());
                newUserDialog.show(getParentFragmentManager(), "NewUser");

            }else{
                ((HomeActivity)requireActivity()).showMessage.showMessageAlert("به دستگاه متصل نیستید",ShowMessage.ALERT_ERROR);
            }
        });
    }


    private void initResult(){
        mViewModel.getResult().observe(getViewLifecycleOwner(),result -> {
            switch (result.code) {
                case Result.CODE_DELETE_DEVICE:
                    deleteDone(result.result);
                    break;
                case Result.CODE_BLUETOOTH_ON_OFF:
                    binding.deviceBluetoothBtn.setChecked(result.result);
                    binding.deviceBluetoothBtn.setEnabled(result.result);
                    changeBTStatusLabel(result.result);
                    break;
                case Result.CODE_BLUETOOTH_LIST_DEVICE_USERS:
                    showList(result.data);
                    break;
                case Result.CODE_BLUETOOTH_ADD_NEW_USER:
                    getCurrentStateNewUser(result.result,result.stringData);
                    break;
                case Result.CODE_BLUETOOTH_GET_USER_BY_FP:
                    getCurrentStateGetUserByFP(result.result,result.stringData);
                    break;
                case Result.CODE_BLUETOOTH_DELETE_USER:
                    getCurrentStateDeleteUser(result.result,result.stringData);
                    break;
                case Result.CODE_BLUETOOTH_RESET_DEVICE:
                    getCurrentStateResetDevice(result.result,result.stringData);
                    break;
                case Result.CODE_BLUETOOTH_CHANGE_ADMIN:
                    getCurrentStateChangeAdmin(result.result,result.stringData);
                    break;
                case Result.CODE_BLUETOOTH_CONNECT:
                    onConnectionChanged(result.result);
                    break;
                case Result.CODE_BLUETOOTH_NAME:
                    binding.deviceBluetoothName.setText(result.stringData);
                    break;
                case Result.CODE_COMMAND_FAILED:
                    if(result.stringData.equals(BluetoothHelper.COM_ADD_NEW_USER))getCurrentStateNewUser(false,"");
                    if(result.stringData.contains("Delete"))getCurrentStateDeleteUser(false,"");
                    if(result.stringData.contains("NewAdmin"))getCurrentStateChangeAdmin(false,"");
                    if(result.stringData.equals(BluetoothHelper.COM_RESET_DEVICE))getCurrentStateResetDevice(false,"");
                    if(result.stringData.equals(BluetoothHelper.COM_GET_USER_BY_FP))getCurrentStateGetUserByFP(false,"");
                    break;
                case Result.CODE_BLUETOOTH_BOUND:
                    ((HomeActivity)requireActivity()).showMessage.showMessageAlert("بلوتوث در حال جفت سازی است.",ShowMessage.ALERT_SUCCESS);
                    break;
            }
        });
    }

    private void startConnection() {
        binding.deviceBtnConnection.setEnabled(false);
        binding.deviceSpinKit.setVisibility(View.VISIBLE);
        binding.deviceBluetoothStatusView.setBackground(getResources().getDrawable(R.drawable.style_status_round_txt));
        binding.deviceBluetoothDeviceStatus.setText(getResources().getText(R.string.connection_progress));
        mViewModel.startConnecting(args.getDeviceIp());
    }

    private void onConnectionChanged(boolean aBoolean) {
        isConnected = aBoolean;
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
    }

    private void prepareDeleteDevice() {
        ((HomeActivity) requireActivity())
                .showMessage
                .showConfirm("آیا از حذف دستگاه مطمئن هستید؟",
                        (dialog) -> {
                            dialog.dismiss();
                            new Thread(()->{
                                mViewModel.deleteDevice(args.getDeviceId());
                            }).start();
                        });
    }

    private void deleteDone(boolean ans){
        if(ans)
            requireActivity().onBackPressed();
        else
            ((HomeActivity) requireActivity()).showMessage.showMessageAlert("امکان حذف دستگاه وجود ندارد", ShowMessage.ALERT_ERROR);
    }

    private void showList(List<DeviceDetails> list){
        binding.deviceSpinKit.setVisibility(View.GONE);
        adapter.differ.submitList(list);
        if (list.size() > 0) binding.deviceEmpty.setVisibility(View.GONE);
        else binding.deviceEmpty.setVisibility(View.VISIBLE);
    }

    private void getCurrentStateNewUser(Boolean res,String data){
        if(newUserDialog!=null)newUserDialog.onRecieveData(res,data);
    }

    private void getCurrentStateDeleteUser(Boolean res,String data){
        if(deleteUserDialog!=null)deleteUserDialog.onRecieveData(res,data);
    }

    private void getCurrentStateGetUserByFP(Boolean res,String data){
        if(getUserByFPDialog!=null)getUserByFPDialog.onRecieveData(res,data);
    }

    private void getCurrentStateResetDevice(Boolean res,String data){
        if(resetDialog!=null)resetDialog.onRecieveData(res,data);
    }

    private void getCurrentStateChangeAdmin(Boolean res,String data){
        if(changeAdminDialog!=null)changeAdminDialog.onRecieveData(res,data);
    }

    public void newUser(){
        mViewModel.newUser();
    }

    public void getUserByFP(){
        mViewModel.getUserByFP();
    }

    public void resetDevice(){
        mViewModel.resetDevice();
    }

    public void deleteUser(DeviceDetails item){
        mViewModel.deleteUser(item.fp_user_id);
    }

    public void changeAdmin(String fp){ mViewModel.changeAdmin(fp);}

    private void prepareDeleteUser(DeviceDetails item){
        if(isConnected) {
            deleteUserDialog = new DeleteUserDialog();
            deleteUserDialog.setFragment(this);
            deleteUserDialog.setDevice_id(args.getDeviceId());
            deleteUserDialog.setUser(item);
            deleteUserDialog.show(getParentFragmentManager(), "DeleteUser");

        }else{
            ((HomeActivity)requireActivity()).showMessage.showMessageAlert("به دستگاه متصل نیستید",ShowMessage.ALERT_ERROR);
        }
    }

    private void prepareGetUserByFP(){
        if(isConnected) {
            getUserByFPDialog = new GetUserByFPDialog();
            getUserByFPDialog.setDevice_id(args.getDeviceId());
            getUserByFPDialog.setFragment(DeviceFragment.this);
            getUserByFPDialog.show(getParentFragmentManager(), "GetUserByFP");
        }else{
            ((HomeActivity)requireActivity()).showMessage.showMessageAlert("به دستگاه متصل نیستید",ShowMessage.ALERT_ERROR);
        }
    }

    private void prepareResetDevice(){
        if(isConnected) {
            resetDialog = new ResetDialog();
            resetDialog.setDevice_id(args.getDeviceId());
            resetDialog.setFragment(DeviceFragment.this);
            resetDialog.show(getParentFragmentManager(), "ResetDevice");
        }else{
            ((HomeActivity)requireActivity()).showMessage.showMessageAlert("به دستگاه متصل نیستید",ShowMessage.ALERT_ERROR);
        }
    }

    private void prepareNewAdmin(DeviceDetails item){
        if(isConnected) {
            changeAdminDialog = new ChangeAdminDialog();
            changeAdminDialog.setDevice_id(args.getDeviceId());
            changeAdminDialog.setFragment(DeviceFragment.this);
            changeAdminDialog.setUser(item);
            changeAdminDialog.show(getParentFragmentManager(), "ChangeAdmin");
        }else{
            ((HomeActivity)requireActivity()).showMessage.showMessageAlert("به دستگاه متصل نیستید",ShowMessage.ALERT_ERROR);
        }
    }

    @Override
    public void cancel() {

    }

    @Override
    public void dismiss() {
        mViewModel.disconnect();
//        initUserList();
    }
}