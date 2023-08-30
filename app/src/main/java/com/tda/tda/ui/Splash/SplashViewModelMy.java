package com.tda.tda.ui.splash;

import com.tda.tda.MyApp;
import com.tda.tda.MyBaseViewModel;
import com.tda.tda.data.repositories.impl.DevicesRepository;
import com.tda.tda.helpers.DB.Models.User;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SplashViewModelMy extends MyBaseViewModel {

    private DevicesRepository devicesRepository;

    @Inject
    public SplashViewModelMy(DevicesRepository devicesRepository){
        this.devicesRepository=devicesRepository;
    }

    public void checkUser(){
        checkUserInfo(devicesRepository);
    }

}