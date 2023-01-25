package com.tda.tda.di;

import android.content.Context;

import androidx.lifecycle.ViewModelProvider;

import com.tda.tda.DB.DB;
import com.tda.tda.data.repositories.impl.DevicesRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(ViewModelComponent.class)
public class RepoModule {

    @Provides
    public DevicesRepository provideDevicesRepository(DB db){
        return new DevicesRepository(db);
    }

}
