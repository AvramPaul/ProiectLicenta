package com.example.car_spotting_front_end.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.car_spotting_front_end.R;
import com.google.android.material.button.MaterialButton;

public class FeedActivity extends AppCompatActivity {
    private Button createPostButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        initializeComponents();
    }

    public void initializeComponents() {
        createPostButton = findViewById(R.id.addPostButton);

        createPostButton.setOnClickListener(view -> {
            Intent intent = new Intent(FeedActivity.this, InsertImageActivity.class);
            startActivity(intent);
        });
    }
}
