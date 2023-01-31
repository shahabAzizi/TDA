package com.tda.tda.ui.splash;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tda.tda.MyBaseFragment;
import com.tda.tda.R;
import com.tda.tda.databinding.SplashFragmentBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SplashFragment extends MyBaseFragment {

    private SplashViewModelMy mViewModel;
    private SplashFragmentBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding=SplashFragmentBinding.inflate(inflater,container,false);
        mViewModel=new ViewModelProvider(this).get(SplashViewModelMy.class);

        new Handler().postDelayed(runAfterSeconds,2000);
        return binding.getRoot();
    }

    Runnable runAfterSeconds= () -> {
        Navigation.findNavController(this.getView()).navigate(R.id.action_splashFragment_to_homeFragment);
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}