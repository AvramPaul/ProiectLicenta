package com.example.car_spotting_front_end.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.car_spotting_front_end.MainActivity;
import com.example.car_spotting_front_end.R;
import com.example.car_spotting_front_end.dto.LoginRequestDTO;
import com.example.car_spotting_front_end.retrofit.ApiResponse;
import com.example.car_spotting_front_end.retrofit.ApiServices;
import com.example.car_spotting_front_end.retrofit.RetrofitClient;
import com.example.car_spotting_front_end.retrofit.TokenManager;
import com.example.car_spotting_front_end.services.ImageUploadService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText inputEditUsername;
    private TextInputEditText inputEditPassword;
    private MaterialButton buttonLogin;
    private MaterialButton buttonRegister;
    
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
                    Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, InsertImageActivity.class);
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
