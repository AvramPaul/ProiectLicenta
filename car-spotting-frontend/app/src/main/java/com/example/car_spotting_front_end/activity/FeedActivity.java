package com.example.car_spotting_front_end.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.car_spotting_front_end.R;
import com.example.car_spotting_front_end.Response.PostsResponse;
import com.example.car_spotting_front_end.dto.PostAdapter;
import com.example.car_spotting_front_end.model.Post;
import com.example.car_spotting_front_end.retrofit.ApiServices;
import com.example.car_spotting_front_end.retrofit.RetrofitClient;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FeedActivity extends AppCompatActivity {
   // private Button createPostButton;
    private RecyclerView feedRecyclerView;
    private PostAdapter postAdapter;
    private List<Post> postsList = new ArrayList<>();
    private boolean isLoading = false; // To avoid duplicate network calls
    private int currentPage = 0; // Tracks the current page
    private int pageSize = 10; // Number of items per page



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        initializeComponents();
    }

    public void initializeComponents() {
      //  createPostButton = findViewById(R.id.addPostButton);
        feedRecyclerView = findViewById(R.id.feedRecyclerView);
        feedRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        postAdapter = new PostAdapter(postsList);
        feedRecyclerView.setAdapter(postAdapter);

        /*createPostButton.setOnClickListener(view -> {
            Intent intent = new Intent(FeedActivity.this, InsertImageActivity.class);
            startActivity(intent);
        });*/

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
