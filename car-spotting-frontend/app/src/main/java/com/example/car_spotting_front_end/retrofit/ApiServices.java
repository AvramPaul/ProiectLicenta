package com.example.car_spotting_front_end.retrofit;

import com.example.car_spotting_front_end.dto.LoginRequestDTO;
import com.example.car_spotting_front_end.model.Post;
import com.example.car_spotting_front_end.dto.PostRequestDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;

public interface ApiServices {

    @GET("posts/feed")
    Call<List<Post>> getAllPosts();
    @POST("posts/create")
    Call<ApiResponse> createPost(@Body PostRequestDTO postRequestDTO);
    @POST("auth/login")
    Call<ApiResponse> loginRequest(@Body LoginRequestDTO loginRequestDTO);
}
