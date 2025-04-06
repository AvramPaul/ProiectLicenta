package com.example.car_spotting_front_end;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.car_spotting_front_end.retrofit.TokenManager;
import com.example.car_spotting_front_end.retrofit.RetrofitClient;
import com.example.car_spotting_front_end.model.PostRequestDTO;
import com.example.car_spotting_front_end.retrofit.PostApi;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText inputEditMake;
    private TextInputEditText inputEditModel;
    private TextInputEditText inputEditYear;
    private MaterialButton buttonCreate;

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

        buttonCreate.setOnClickListener(view -> {
            sendPostRequest();
        });
    }

    private void sendPostRequest() {
        String make = inputEditMake.getText().toString();
        String model = inputEditModel.getText().toString();
        int year = Integer.parseInt(inputEditYear.getText().toString());

        // Username-ul trebuie preluat din sesiunea curentă (backend-ul folosește SecurityContextHolder)
        PostRequestDTO postRequestDTO = new PostRequestDTO(null, make, model, year, null);

        PostApi apiService = RetrofitClient.getClient(getApplicationContext()).create(PostApi.class);
        Call<String> call = apiService.createPost(postRequestDTO);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Postare creată!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Eroare la creare! " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Eroare de rețea!", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Error: " + t.getMessage());
            }
        });
    }
}

