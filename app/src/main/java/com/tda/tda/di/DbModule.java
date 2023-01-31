package com.tda.tda.di;

import android.content.Context;

import com.tda.tda.helpers.BluetoothHelper;
import com.tda.tda.helpers.DB.DB;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
final class DbModule {

    @Provides
    @Singleton
    public static DB provideDB(@ApplicationContext Context context){
        return DB.getInstance(context);
    }


}
