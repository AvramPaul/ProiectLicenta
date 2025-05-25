package com.example.car_spotting_front_end;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.car_spotting_front_end.activity.LoginActivity;
import com.example.car_spotting_front_end.activity.RegisterActivity;
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

    private MaterialButton buttonLogin;
    private MaterialButton buttonRegister;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeComponents();
    }

    private void initializeComponents() {
        buttonLogin = findViewById(R.id.form_buttonLogin);
        buttonRegister = findViewById(R.id.form_buttonRegister);
        videoView = findViewById(R.id.backgroudVideoView);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.bg_drift_video;
        videoView.setVideoURI(Uri.parse(videoPath));
        videoView.setOnPreparedListener(mp -> {
            mp.setLooping(true);
            mp.setVolume(0f, 0f);
            videoView.start();
        });


        buttonLogin.setOnClickListener(view -> {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
        });
        buttonRegister.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);

        });

    }
}

