package com.example.car_spotting_front_end.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.car_spotting_front_end.R;
import com.example.car_spotting_front_end.dto.PostsResponse;
import com.example.car_spotting_front_end.dto.PostAdapter;
import com.example.car_spotting_front_end.model.Post;
import com.example.car_spotting_front_end.services.ApiServices;
import com.example.car_spotting_front_end.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FeedActivity extends AppCompatActivity {
    private LinearLayout createPostButton;
    private LinearLayout logoutButton;
    private LinearLayout myPostsButton;
    private TextView profilePicture;
    private RecyclerView feedRecyclerView;
    private PostAdapter postAdapter;
    private List<Post> postsList = new ArrayList<>();
    private boolean isLoading = false; // To avoid duplicate network calls
    private int currentPage = 0; // Tracks the current page
    private int pageSize = 10; // Number of items per page
    private boolean pannelVisible = false;
    private VideoView videoView;

   private LinearLayout sidePanel;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        initializeComponents();
    }

    public void initializeComponents() {
        createPostButton = findViewById(R.id.addPostButton);
        logoutButton = findViewById(R.id.logoutButton);
        myPostsButton = findViewById(R.id.myPostsButton);
        feedRecyclerView = findViewById(R.id.feedRecyclerView);
        feedRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        postAdapter = new PostAdapter(postsList, this);
        feedRecyclerView.setAdapter(postAdapter);
        profilePicture = findViewById(R.id.profileButton);
        sidePanel = findViewById(R.id.sidePanel);

        videoView = findViewById(R.id.backgroudVideoView);
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
            Intent intent = new Intent(FeedActivity.this, InsertImageActivity.class);
            startActivity(intent);
        });

        logoutButton.setOnClickListener(view -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove("logged_username");
            editor.remove("jwt_token");
            editor.apply();
            Intent intent = new Intent(FeedActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
        myPostsButton.setOnClickListener(view -> {
            Intent intent = new Intent(FeedActivity.this, MyPostsActivity.class);
            startActivity(intent);
        });

        fetchPosts(currentPage ,pageSize);

        feedRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                // Check if scrolling near the end of the list
                if (!isLoading && layoutManager != null &&
                        layoutManager.findLastCompletelyVisibleItemPosition() == postsList.size() - 1) {
                    isLoading = true; // Prevent duplicate calls
                    currentPage++; // Increment the page number
                    fetchPosts(currentPage, pageSize); // Fetch the next page
                }
            }
        });
    }

    private void fetchPosts(int page, int size){

        Retrofit retrofit = RetrofitClient.getClient(this);
        ApiServices apiServices = retrofit.create(ApiServices.class);
        apiServices.getPosts(page, size).enqueue(new Callback<PostsResponse>() {
            @Override
            public void onResponse(Call<PostsResponse> call, Response<PostsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Post> newPosts = response.body().getPostList();
                    postsList.addAll(newPosts);
                    postAdapter.notifyDataSetChanged();

                    int totalPages = response.body().getPage().getTotalPages();
                    if (currentPage >= totalPages - 1) {
                        feedRecyclerView.clearOnScrollListeners();
                    }
                }
                isLoading = false; // Reset the loading state
            }

            @Override
            public void onFailure(Call<PostsResponse> call, Throwable throwable) {
                Toast.makeText(FeedActivity.this, "Error: Feed doesn't work" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                isLoading = false; // Reset the loading state
            }
        });
    }

}
