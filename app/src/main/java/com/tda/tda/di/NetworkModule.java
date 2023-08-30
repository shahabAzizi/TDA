package com.tda.tda.di;

import android.app.Application;

import androidx.annotation.Nullable;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tda.tda.data.repositories.impl.APIInterface;
import com.tda.tda.model.Constants;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.Cache;
import okhttp3.Dispatcher;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {
    final String mBaseUrl="http://razhashop.ir/";
    public final String token="";

    NetworkModule() {}



    @Provides
    @Singleton
    Cache provideHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }

    @Provides
    @Singleton
    Dispatcher provideDispatcher(){
        Dispatcher dispatcher= new Dispatcher();
        dispatcher.setMaxRequests(1);
        return  dispatcher;
    }

    @Provides
    @Singleton
    GsonConverterFactory provideGson() {
        GsonConverterFactory gsonBuilder =  GsonConverterFactory.create(new GsonBuilder().create());
        return gsonBuilder;
    }

    @Provides
    @Singleton
    HttpLoggingInterceptor provideLoggingInterceptor(){
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkhttpClient(Cache cache,Dispatcher dispatcher,HttpLoggingInterceptor loggingInterceptor) {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.dispatcher(dispatcher);
        client.addNetworkInterceptor(loggingInterceptor);
        client.addNetworkInterceptor(chain -> {
            Request oldRequest = chain.request();
            Request.Builder newRequestBuilder = oldRequest.newBuilder();
            newRequestBuilder.addHeader("Authorization","Bearer "+Constants.token);
            return chain.proceed(newRequestBuilder.build());
        });
        client.cache(cache);
        return client.build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(GsonConverterFactory gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(gson)
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    APIInterface provideApiInterface(Retrofit retrofit) {
        return retrofit.create(APIInterface.class);
    }

}
