package com.tda.tda.ui.Main;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tda.tda.R;
import com.tda.tda.databinding.HomeFragmentBinding;
import com.tda.tda.model.adapters.DeviceAdapter;
import com.tda.tda.model.dataclass.Devices;
import com.tda.tda.ui.Splash.SplashViewModelMy;

import java.util.List;

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

        binding.homeBtnAdd.setOnClickListener(view->{
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_homeFragment_to_adddeviceFragment);
        });

        adapter=new DeviceAdapter();
        adapter.itemClickListener= device -> {
            //Do Nothing Now
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

        new Thread(() -> {
            mViewModel.getDevices();
        }).start();

        return binding.getRoot();
    }



}