package com.tda.tda.di;

import com.tda.tda.helpers.BluetoothHelper;
import com.tda.tda.helpers.DB.DB;
import com.tda.tda.data.repositories.impl.DevicesRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;

@Module
@InstallIn(ViewModelComponent.class)
public class RepoModule {

    @Provides
    public DevicesRepository provideDevicesRepository(DB db){
        return new DevicesRepository(db);
    }

    @Provides
    public BluetoothHelper provideBluetoothHelper(){
        return BluetoothHelper.getInstance();
    }

}
