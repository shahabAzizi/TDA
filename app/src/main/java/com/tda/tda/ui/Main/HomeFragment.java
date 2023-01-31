package com.tda.tda.ui.main;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tda.tda.HomeActivity;
import com.tda.tda.R;
import com.tda.tda.databinding.HomeFragmentBinding;
import com.tda.tda.model.adapters.DeviceAdapter;
import com.tda.tda.ui.device.DeviceFragment;

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

        new Thread(() -> {
            mViewModel.getDevices();
        }).start();

        return binding.getRoot();
    }

    private void initToolbar(){
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

    private void initNavigation(){
        binding.navView.setNavigationItemSelectedListener(item -> {
//                if(item.getItemId()==R.id.nav_slideshow){
//            Toast.makeText(HomeActivity.this, "Yes", Toast.LENGTH_SHORT).show();
//                }
            return false;
        });
    }


}