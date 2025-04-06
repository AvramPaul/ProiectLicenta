package com.example.car_spotting_front_end.retrofit;

import com.example.car_spotting_front_end.model.Post;
import com.example.car_spotting_front_end.model.PostRequestDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;

import java.util.List;

public interface PostApi {

    @GET("/posts/feed")
    Call<List<Post>> getAllPosts();
    @POST("posts/create")
    Call<String> createPost(@Body PostRequestDTO postRequestDTO);
}
