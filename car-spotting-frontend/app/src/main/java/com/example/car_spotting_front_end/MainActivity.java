package com.example.car_spotting_front_end;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.car_spotting_front_end.activity.LoginActivity;
import com.example.car_spotting_front_end.activity.RegisterActivity;
import com.google.android.material.button.MaterialButton;

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

