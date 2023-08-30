package com.tda.tda.di;

import android.content.Context;

import com.github.ivbaranov.rxbluetooth.RxBluetooth;
import com.tda.tda.data.repositories.impl.DevicesRepository;
import com.tda.tda.helpers.BluetoothHelper;
import com.tda.tda.helpers.BluetoothUtils;
import com.tda.tda.helpers.DB.DB;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;
import dagger.hilt.android.qualifiers.ActivityContext;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public final class BluetoothModule {


    @Provides
    @Singleton
    public static RxBluetooth provideBluetoothUtils(@ApplicationContext Context context){
        return new RxBluetooth(context);
    }

}
