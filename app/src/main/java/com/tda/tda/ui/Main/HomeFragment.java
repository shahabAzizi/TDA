package com.tda.tda.ui.main;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.bumptech.glide.Glide;
import com.tda.tda.HomeActivity;
import com.tda.tda.MyBaseViewModel;
import com.tda.tda.R;
import com.tda.tda.databinding.HomeFragmentBinding;
import com.tda.tda.helpers.DB.Models.User;
import com.tda.tda.model.adapters.DeviceAdapter;
import com.tda.tda.model.dataclass.Result;
import com.tda.tda.model.dialogs.ShowMessage;
import com.tda.tda.ui.device.DeviceFragment;

import org.w3c.dom.Text;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;
    private HomeFragmentBinding binding;
    private DeviceAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding=HomeFragmentBinding.inflate(inflater,container,false);
        mViewModel=new ViewModelProvider(this).get(HomeViewModel.class);

        initToolbar();
        initButtonAdd();
        initDeviceList();
        initNavigation();
        initLoading();
        initResult();

        ((HomeActivity)requireActivity()).checkLocationPermission(requireActivity());
        new Thread(() -> {
            mViewModel.getDevices();
        }).start();

        checkVPN();
        return binding.getRoot();
    }

    private void initToolbar(){
        ((HomeActivity) requireActivity()).toolbarMenu = 0;
        ((HomeActivity)getActivity()).setSupportActionBar(binding.homeToolbar);
//        ((HomeActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
//        ((HomeActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.homeToolbar.setNavigationOnClickListener(view -> {
            binding.homeFragment.open();
        });
    }

    private void initButtonAdd(){
        binding.homeBtnAdd.setOnClickListener(view->{
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_homeFragment_to_adddeviceFragment);
        });
    }

    private void initDeviceList(){
        adapter=new DeviceAdapter();
        adapter.itemClickListener= device -> {
             Navigation.findNavController(binding.getRoot()).navigate(HomeFragmentDirections.actionNavFragmentHomeToNavFragmentDevice(device.getId(), device.getName(),device.getIp()));
        };

        binding.homeList.setAdapter(adapter);
        binding.homeList.setLayoutManager(new LinearLayoutManager(this.getContext()));

        mViewModel.getDeviceLiveData().observe(this.getViewLifecycleOwner(), devices -> {
            adapter.differ.submitList(devices);
            if(devices.size()>0){
                binding.homeEmpty.setVisibility(View.GONE);
            }else{
                binding.homeEmpty.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initLoading(){
        mViewModel.getLoading().observe(this.getViewLifecycleOwner(), aBoolean -> {
            if(aBoolean){
                TextView txtLoading=binding.navView.getHeaderView(0).findViewById(R.id.nav_textloading);
                txtLoading.setText("در حال بکاپ گیری/ریستور");
                binding.navView.getHeaderView(0).findViewById(R.id.nav_loading_lin).setVisibility(View.VISIBLE);
            }else{
                binding.navView.getHeaderView(0).findViewById(R.id.nav_loading_lin).setVisibility(View.GONE);
            }
        });
    }

    private void initResult(){
        mViewModel.getResult().observe(this.getViewLifecycleOwner(), result -> {
            if(result.code==Result.CODE_CREATE_BACKUP){
                if(result.result==true){
                    ((HomeActivity)requireActivity()).showMessage.showMessageAlert("فرآیند بکاپ گیری با موفقیت انجام شد",ShowMessage.ALERT_SUCCESS);
                }else{
                    ((HomeActivity)requireActivity()).showMessage.showMessageAlert(result.stringData,ShowMessage.ALERT_ERROR);
                }
                mViewModel.getResult().setValue(new Result());
            }
            if(result.code==Result.CODE_CREATE_RESTORE){
                if(result.result==true){
                    ((HomeActivity)requireActivity()).showMessage.showMessageAlert("فرآیند ریستور با موفقیت انجام شد",ShowMessage.ALERT_SUCCESS);
                }else{
                    ((HomeActivity)requireActivity()).showMessage.showMessageAlert(result.stringData,ShowMessage.ALERT_ERROR);
                }
                mViewModel.getResult().setValue(new Result());
            }
        });
    }

    private void initNavigation(){
        mViewModel.isUserLoggedin.observe(this.getViewLifecycleOwner(), aBoolean -> {
            if(aBoolean){
                TextView name = binding.navView.getHeaderView(0).findViewById(R.id.nav_textname);
                name.setText(mViewModel.UserName.getValue()!=null?mViewModel.UserName.getValue():"بدون نام");
                binding.navView.getMenu().findItem(R.id.nav_home).setVisible(false);
            }
        });
        new Thread(() -> { mViewModel.checkUser(); }).start();
//        Glide.with(requireContext()).load("http://razhashop.ir/storage/app/public/slider.png").into((ImageView) binding.homePart1.findViewById(R.id.home_img_slider));
        binding.navView.setNavigationItemSelectedListener(item -> {
            if(item.getItemId()==R.id.nav_home){
                Navigation.findNavController(binding.getRoot()).navigate(HomeFragmentDirections.actionNavFragmentHomeToNavFragmentLogin());
            }else
            if(item.getItemId()==R.id.nav_create_backup){
                if(!mViewModel.isUserLoggedin.getValue()) {
                    ((HomeActivity) requireActivity()).showMessage.showMessageAlert("لطفا ابتدا لاگین کنید", ShowMessage.ALERT_ERROR);
                }else{
                    new Thread(()->{mViewModel.startBackup();}).start();
                }
            }else
            if(item.getItemId()==R.id.nav_restore_backup){
                if(!mViewModel.isUserLoggedin.getValue()) {
                    ((HomeActivity) requireActivity()).showMessage.showMessageAlert("لطفا ابتدا لاگین کنید", ShowMessage.ALERT_ERROR);
                }else{
                    new Thread(() -> {
                        mViewModel.startRestore();
                    }).start();
                }
            }
            return false;
        });


    }

    private void checkVPN(){
        boolean result = ((HomeActivity)requireActivity()).isVPNTurnOn();
        if(result){
            ((HomeActivity) requireActivity()).showMessage.showMessageAlert("لطفا وی پی ان را قطع کنید", ShowMessage.ALERT_ERROR);
        }
    }


}