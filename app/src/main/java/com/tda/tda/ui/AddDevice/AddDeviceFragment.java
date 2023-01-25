package com.tda.tda.ui.AddDevice;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tda.tda.MyBaseFragment;
import com.tda.tda.R;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddDeviceFragment extends MyBaseFragment {

    private AddDeviceViewModel mViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(AddDeviceViewModel.class);
        return inflater.inflate(R.layout.add_device_fragment, container, false);
    }


}