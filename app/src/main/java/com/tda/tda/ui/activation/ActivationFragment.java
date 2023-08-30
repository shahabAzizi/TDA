package com.tda.tda.ui.activation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.tda.tda.HomeActivity;
import com.tda.tda.MyBaseFragment;
import com.tda.tda.databinding.ActivationFragmentBinding;
import com.tda.tda.databinding.LoginFragmentBinding;
import com.tda.tda.model.dialogs.ShowMessage;
import com.tda.tda.ui.device.DeviceFragmentArgs;
import com.tda.tda.ui.login.LoginFragmentDirections;
import com.tda.tda.ui.login.LoginViewModel;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ActivationFragment extends MyBaseFragment {

    private ActivationViewModel mViewModel;
    private ActivationFragmentBinding binding;
    private ActivationFragmentArgs args;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding=ActivationFragmentBinding.inflate(inflater,container,false);
        mViewModel=new ViewModelProvider(this).get(ActivationViewModel.class);

        args = ActivationFragmentArgs.fromBundle(getArguments());

        initLoading();
        initResult();
        initButton();

        return binding.getRoot();
    }

    public void initLoading(){
        mViewModel.getLoading().observe(this.getViewLifecycleOwner(), aBoolean -> {
            if(aBoolean){
                binding.loginLoading.getRoot().setVisibility(View.VISIBLE);
                binding.loginBtnAdd.setEnabled(false);
            }else{
                binding.loginLoading.getRoot().setVisibility(View.GONE);
                binding.loginBtnAdd.setEnabled(true);
            }
        });
    }

    public void initResult(){
        mViewModel.getResult().observe(this.getViewLifecycleOwner(), result -> {
            if(result.result){
                Navigation.findNavController(binding.getRoot()).navigate(ActivationFragmentDirections.actionNavFragmentActivationToNavFragmentHome());
            }else{
                ((HomeActivity)requireActivity()).showMessage.showMessageAlert(result.stringData, ShowMessage.ALERT_ERROR);
            }
        });
    }

    private void initButton(){
        binding.loginBtnAdd.setOnClickListener(view -> {
            binding.loginMobile.setError(null);
            if(Objects.requireNonNull(binding.loginMobile.getText()).toString().length()==6 ){
                mViewModel.activation(args.getMobile(),binding.loginMobile.getText().toString());
            }else{
                binding.loginMobile.setError("کد باید 6 رقم باشد");
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}