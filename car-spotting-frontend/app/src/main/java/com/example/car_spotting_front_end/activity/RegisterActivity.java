package com.example.car_spotting_front_end.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.car_spotting_front_end.R;
import com.example.car_spotting_front_end.dto.RegisterRequestDTO;
import com.example.car_spotting_front_end.retrofit.ApiResponse;
import com.example.car_spotting_front_end.retrofit.ApiServices;
import com.example.car_spotting_front_end.retrofit.RetrofitClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText inputEditEmail;
    private TextInputEditText inputEditUsername;
    private TextInputEditText inputEditPassword;
    private MaterialButton buttonRegister;
    private MaterialButton buttonLogin;
    private TextInputLayout emailLayout;
    private TextInputLayout usernameLayout;
    private TextInputLayout passwordLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ;
        setContentView(R.layout.activity_register);
        InitializeComponents();
    }


    private void InitializeComponents() {
        inputEditEmail = findViewById(R.id.inputEditEmail);
        inputEditUsername = findViewById(R.id.inputEditUsername);
        inputEditPassword = findViewById(R.id.inputEditPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonLogin = findViewById(R.id.buttonLogin);
        emailLayout = findViewById(R.id.emailLayout);
        usernameLayout = findViewById(R.id.usernameLayout);
        passwordLayout = findViewById(R.id.passwordLayout);


        buttonRegister.setOnClickListener(v -> {
            if(validateInput()){
                sendRegisterRequest();
            }
        });
        buttonLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

    }
    private boolean validateInput() {
        boolean isValid = true;
        // Validare email
        String email = inputEditEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            emailLayout.setError("Email is required");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Invalid email format");
            isValid = false;
        } else {
            emailLayout.setError(null);
        }

        // Validare username
        String username = inputEditUsername.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            usernameLayout.setError("Username is required");
            isValid = false;
        } else if (username.length() < 3) {
            usernameLayout.setError("Username must be at least 3 characters");
            isValid = false;
        } else {
            usernameLayout.setError(null);
        }

        // Validare password
        String password = inputEditPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordLayout.setError("Password is required");
            isValid = false;
        } else if (password.length() < 3) {
            passwordLayout.setError("Password must be at least 4 characters");
            isValid = false;
        } else {
            passwordLayout.setError(null);
        }

        return isValid;
    }

    private void sendRegisterRequest() {
        String email = inputEditEmail.getText().toString();
        String username = inputEditUsername.getText().toString();
        String password = inputEditPassword.getText().toString();

        RegisterRequestDTO registerRequest = new RegisterRequestDTO(username, email, password);

        ApiServices apiService = RetrofitClient.getClient(getApplicationContext()).create(ApiServices.class);
        Call<ApiResponse> call = apiService.registerRequest(registerRequest);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()  && response.body() != null ){
                    String successMessage = response.body().getMessage();
                    Toast.makeText(getApplicationContext(), successMessage, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable throwable) {
                Log.e("Registration Error", throwable.getMessage());
                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
            }


        });

    }
}
