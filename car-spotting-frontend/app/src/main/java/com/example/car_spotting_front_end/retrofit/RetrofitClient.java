package com.example.car_spotting_front_end.retrofit;

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
    private static final String JWT_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJQYXVsIEhvcnZhdGgiLCJpYXQiOjE3NDUyMjI3MTEsImV4cCI6MTc0NTMwOTExMX0.r5IGrmxYep0aprKBtUiJNfUkiGKTwn03c5a20COj5Ms";
    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            TokenManager tokenManager = new TokenManager(context);
            String token = tokenManager.getToken(); // Obținem token-ul salvat

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request original = chain.request();
                            Request.Builder requestBuilder = original.newBuilder()
                                    .header("Authorization", JWT_TOKEN) // Adăugăm token-ul în Header
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

