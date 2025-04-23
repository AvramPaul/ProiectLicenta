package com.example.car_spotting_front_end;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.car_spotting_front_end.activity.LoginActivity;
import com.example.car_spotting_front_end.retrofit.ApiResponse;
import com.example.car_spotting_front_end.retrofit.RetrofitClient;
import com.example.car_spotting_front_end.dto.PostRequestDTO;
import com.example.car_spotting_front_end.retrofit.ApiServices;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText inputEditMake;
    private TextInputEditText inputEditModel;
    private TextInputEditText inputEditYear;
    private MaterialButton buttonCreate;
    private MaterialButton buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeComponents();
    }

    private void initializeComponents() {
        inputEditMake = findViewById(R.id.form_textFieldCarMake);
        inputEditModel = findViewById(R.id.form_textFieldCarModel);
        inputEditYear = findViewById(R.id.form_textFieldCarYear);
        buttonCreate = findViewById(R.id.form_buttonCreatePost);
        buttonLogin = findViewById(R.id.form_buttonLogin);
        buttonLogin.setOnClickListener(view -> {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                });
        buttonCreate.setOnClickListener(view -> {
            sendPostRequest();
        });
    }

    private void sendPostRequest() {
        String make = Objects.requireNonNull(inputEditMake.getText()).toString();
        String model = Objects.requireNonNull(inputEditModel.getText()).toString();
        int year = Integer.parseInt(Objects.requireNonNull(inputEditYear.getText()).toString());

        // Username-ul trebuie preluat din sesiunea curentă (backend-ul folosește SecurityContextHolder)
        PostRequestDTO postRequestDTO = new PostRequestDTO(null, make, model, year, null);

        ApiServices apiService = RetrofitClient.getClient(getApplicationContext()).create(ApiServices.class);
        Call<ApiResponse> call = apiService.createPost(postRequestDTO);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Postare creată!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("API_RESPONSE_ERROR", "Răspuns: " + response.errorBody());
                    Toast.makeText(MainActivity.this, "Eroare la creare! " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Eroare de rețea!", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Error: " + t.getMessage());
            }
        });
    }
}

