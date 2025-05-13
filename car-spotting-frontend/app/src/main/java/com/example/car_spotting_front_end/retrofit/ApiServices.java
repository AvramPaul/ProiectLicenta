package com.example.car_spotting_front_end.retrofit;

import com.example.car_spotting_front_end.dto.LoginRequestDTO;
import com.example.car_spotting_front_end.dto.PostDetailsDTO;
import com.example.car_spotting_front_end.dto.RegisterRequestDTO;
import com.example.car_spotting_front_end.model.Post;
import com.example.car_spotting_front_end.dto.PostRequestDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiServices {
    @POST("posts/create")
    Call<ApiResponse> createPost(@Body PostRequestDTO postRequestDTO);
    @POST("auth/login")
    Call<ApiResponse> loginRequest(@Body LoginRequestDTO loginRequestDTO);
    @POST("auth/register")
    Call<ApiResponse> registerRequest(@Body RegisterRequestDTO registerRequestDTO);
}
