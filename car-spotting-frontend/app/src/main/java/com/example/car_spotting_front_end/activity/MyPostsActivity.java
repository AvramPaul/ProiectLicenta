package com.example.car_spotting_front_end.activity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.car_spotting_front_end.R;
import com.example.car_spotting_front_end.Response.PostsResponse;
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
    }
    private void fetchMyPosts() {
        //ApiServices apiServices = RetrofitClient.getClient(this).create(ApiServices.class);
        //Call<List<UserPostsWithReactionsDTO>> call = apiServices.getMyPosts();
        //call.enqueue(new Callback<List<UserPostsWithReactionsDTO>>(){
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
