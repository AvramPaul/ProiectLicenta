package com.example.car_spotting_front_end.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.car_spotting_front_end.R;
import com.example.car_spotting_front_end.dto.MyPostsAdapter;
import com.example.car_spotting_front_end.dto.UserPostsWithReactionsDTO;
import com.example.car_spotting_front_end.retrofit.RetrofitClient;
import com.example.car_spotting_front_end.services.ApiServices;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyPostsActivity extends AppCompatActivity {

    private LinearLayout createPostButton;
    private LinearLayout logoutButton;
    private LinearLayout feedButton;
    private TextView profilePicture;

    private boolean pannelVisible = false;

    private LinearLayout sidePanel;

    private RecyclerView recyclerView;
    private MyPostsAdapter adapter;
    private List<UserPostsWithReactionsDTO> myPostsList = new ArrayList<>();
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_my_posts);
        initializeComponents();
        fetchMyPosts();
    }

    private void initializeComponents() {
        createPostButton = findViewById(R.id.addPostButton);
        logoutButton = findViewById(R.id.logoutButton);
        feedButton = findViewById(R.id.feedButton);
        profilePicture = findViewById(R.id.profileButton);
        sidePanel = findViewById(R.id.sidePanel);
        recyclerView = findViewById(R.id.recycleViewMyPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyPostsAdapter(myPostsList, this);
        recyclerView.setAdapter(adapter);

        videoView = findViewById(R.id.background_VideoView);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.bg_mooving_video;
        videoView.setVideoURI(Uri.parse(videoPath));
        videoView.setOnPreparedListener(mp -> {
            mp.setLooping(true);
            mp.setVolume(0f, 0f);
            videoView.start();
        });

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        String username = prefs.getString("logged_username", "");
        profilePicture.setText(username.substring(0, 1));

        profilePicture.setOnClickListener(view -> {
            if (!pannelVisible) {
                sidePanel.setVisibility(View.VISIBLE);
                sidePanel.animate().translationX(0).setDuration(300);
                pannelVisible = true;
            } else {
                sidePanel.animate().translationX(80).setDuration(300);
                sidePanel.setVisibility(View.GONE);
                pannelVisible = false;
            }
        });
        createPostButton.setOnClickListener(view -> {
            Intent intent = new Intent(MyPostsActivity.this, InsertImageActivity.class);
            startActivity(intent);
        });

        logoutButton.setOnClickListener(view -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove("logged_username");
            editor.remove("jwt_token");
            editor.apply();
            Intent intent = new Intent(MyPostsActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
        feedButton.setOnClickListener(view -> {
            Intent intent = new Intent(MyPostsActivity.this, FeedActivity.class);
            startActivity(intent);
        });

    }
    private void fetchMyPosts() {

        Retrofit retrofit = RetrofitClient.getClient(this);
        ApiServices apiServices = retrofit.create(ApiServices.class);
        apiServices.getMyPosts().enqueue(new Callback<List<UserPostsWithReactionsDTO>>() {
            @Override
            public void onResponse(Call<List<UserPostsWithReactionsDTO>> call, Response<List<UserPostsWithReactionsDTO>> response) {
                if(response.isSuccessful() && response.body() != null){
                    myPostsList.clear();
                    myPostsList.addAll(response.body());
                    adapter.notifyDataSetChanged();
            }
        }
            @Override
            public void onFailure(Call<List<UserPostsWithReactionsDTO>> call, Throwable throwable) {
                // Handle failure
            }
        });
    }
}
