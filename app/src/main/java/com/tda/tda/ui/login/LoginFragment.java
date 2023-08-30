package com.tda.tda.ui.login;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.tda.tda.HomeActivity;
import com.tda.tda.MyBaseFragment;
import com.tda.tda.R;
import com.tda.tda.databinding.LoginFragmentBinding;
import com.tda.tda.databinding.SplashFragmentBinding;
import com.tda.tda.model.dataclass.Result;
import com.tda.tda.model.dialogs.ShowMessage;
import com.tda.tda.ui.main.HomeFragmentDirections;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginFragment extends MyBaseFragment {

    private LoginViewModel mViewModel;
    private LoginFragmentBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding=LoginFragmentBinding.inflate(inflater,container,false);
        mViewModel=new ViewModelProvider(this).get(LoginViewModel.class);

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
                Navigation.findNavController(binding.getRoot()).navigate(LoginFragmentDirections.actionNavFragmentLoginToNavFragmentActivation(binding.loginMobile.getText().toString()));
            }else{
                ((HomeActivity)requireActivity()).showMessage.showMessageAlert(result.stringData, ShowMessage.ALERT_ERROR);
            }
        });
    }

    private void initButton(){
        binding.loginBtnAdd.setOnClickListener(view -> {
            ((HomeActivity)requireActivity()).internetPermission(requireActivity());
            binding.loginMobile.setError(null);
            if(Objects.requireNonNull(binding.loginMobile.getText()).toString().length()==11 && binding.loginMobile.getText().toString().startsWith("09")){
                mViewModel.login(binding.loginMobile.getText().toString());
            }else{
                binding.loginMobile.setError("شماره موبایل صحیح نیست");
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}