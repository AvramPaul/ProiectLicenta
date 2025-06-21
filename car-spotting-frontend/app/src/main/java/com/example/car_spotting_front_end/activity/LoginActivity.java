package com.example.car_spotting_front_end.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.car_spotting_front_end.R;
import com.example.car_spotting_front_end.dto.LoginRequestDTO;
import com.example.car_spotting_front_end.retrofit.ApiResponse;
import com.example.car_spotting_front_end.services.ApiServices;
import com.example.car_spotting_front_end.retrofit.RetrofitClient;
import com.example.car_spotting_front_end.retrofit.TokenManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText inputEditUsername;
    private TextInputEditText inputEditPassword;
    private MaterialButton buttonLogin;
    private MaterialTextView buttonRegister;
    private VideoView videoView;
    
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_login);
        InitializeComponents();
    }

    private void InitializeComponents() {
        inputEditUsername = findViewById(R.id.login_textFieldUsername);
        inputEditPassword = findViewById(R.id.login_textFieldPassword);
        buttonLogin = findViewById(R.id.login_buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);
        VideoView videoView = findViewById(R.id.backgroundVideoView);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.bg_mooving_car;
        videoView.setVideoURI(Uri.parse(videoPath));
        videoView.setOnPreparedListener(mp -> {
            mp.setLooping(true);
            mp.setVolume(0f, 0f);
            videoView.start();
        });

        buttonLogin.setOnClickListener(view -> {sendLoginRequest();});
        buttonRegister.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void sendLoginRequest() {
        String username = inputEditUsername.getText().toString();
        String password = inputEditPassword.getText().toString();

        LoginRequestDTO loginRequest = new LoginRequestDTO(username, password);

        ApiServices apiService = RetrofitClient.getClient(getApplicationContext()).create(ApiServices.class);
        Call<ApiResponse> call = apiService.loginRequest(loginRequest);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    String token = response.body().getMessage();
                    TokenManager tokenManager = new TokenManager(getApplicationContext());
                    tokenManager.saveToken(token);

                    SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("logged_username", username);
                    editor.apply();

                    Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, FeedActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Log.e("LOGIN_ERROR", "Error: "+response.message());
                    Toast.makeText(LoginActivity.this, "Login failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable throwable) {
                Log.e("LOGIN ERROR", throwable.getMessage());
                Toast.makeText(LoginActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
