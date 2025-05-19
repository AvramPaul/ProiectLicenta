package com.example.car_spotting_front_end.retrofit;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.example.car_spotting_front_end.retrofit.TokenManager;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;
import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@GlideModule
public class GlideHeaderModule extends AppGlideModule {
    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        TokenManager tokenManager = new TokenManager(context);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    String token = tokenManager.getToken(); // Ia JWT-ul

                    Request newRequest = original.newBuilder()
                            .header("Authorization", "Bearer " + token)
                            .build();

                    return chain.proceed(newRequest);
                })
                .build();

        registry.replace(
                GlideUrl.class, // NU HttpUrl.class
                InputStream.class,
                new OkHttpUrlLoader.Factory(client)
        );
    }
}

