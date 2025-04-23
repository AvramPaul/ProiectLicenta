package com.example.car_spotting_front_end.retrofit;

import com.example.car_spotting_front_end.activity.LoginActivity;
import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


import android.content.Context;

import com.example.car_spotting_front_end.retrofit.TokenManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.example.car_spotting_front_end.retrofit.TokenManager;
public class RetrofitClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            TokenManager tokenManager = new TokenManager(context);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request original = chain.request();
                            String token = tokenManager.getToken(); // Obținem token-ul salvat
                            Request.Builder requestBuilder = original.newBuilder()
                                    .header("Authorization", "Bearer " + token) // Adăugăm token-ul în Header
                                    .method(original.method(), original.body());
                            return chain.proceed(requestBuilder.build());
                        }
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8081/") // Pentru emulator (schimbă cu IP-ul local pe telefon)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}

