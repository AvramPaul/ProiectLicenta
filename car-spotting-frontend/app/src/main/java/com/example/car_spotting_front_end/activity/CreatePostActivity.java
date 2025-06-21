package com.example.car_spotting_front_end.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.car_spotting_front_end.R;
import com.example.car_spotting_front_end.dto.PostRequestDTO;
import com.example.car_spotting_front_end.retrofit.ApiResponse;
import com.example.car_spotting_front_end.services.ApiServices;
import com.example.car_spotting_front_end.retrofit.RetrofitClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePostActivity  extends AppCompatActivity {

    private TextInputEditText inputEditMake;
    private TextInputEditText inputEditModel;
    private TextInputEditText inputEditYear;
    private MaterialButton buttonCreate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        initializeComponents();
    }

    private void initializeComponents() {
        inputEditMake = findViewById(R.id.form_textFieldCarMake);
        inputEditModel = findViewById(R.id.form_textFieldCarModel);
        inputEditYear = findViewById(R.id.form_textFieldCarYear);
        buttonCreate = findViewById(R.id.form_buttonCreatePost);

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
                    Toast.makeText(com.example.car_spotting_front_end.activity.CreatePostActivity.this, "Postare creată!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("API_RESPONSE_ERROR", "Răspuns: " + response.errorBody());
                    Toast.makeText(com.example.car_spotting_front_end.activity.CreatePostActivity.this, "Eroare la creare! " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(com.example.car_spotting_front_end.activity.CreatePostActivity.this, "Eroare de rețea!", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Error: " + t.getMessage());
            }
        });
    }
}