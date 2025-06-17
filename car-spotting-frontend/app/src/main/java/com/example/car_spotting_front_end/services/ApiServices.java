package com.example.car_spotting_front_end.services;

import com.example.car_spotting_front_end.dto.PostsResponse;
import com.example.car_spotting_front_end.dto.LoginRequestDTO;

import com.example.car_spotting_front_end.dto.RegisterRequestDTO;
import com.example.car_spotting_front_end.dto.PostRequestDTO;
import com.example.car_spotting_front_end.dto.UserPostsWithReactionsDTO;
import com.example.car_spotting_front_end.retrofit.ApiResponse;

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

    @GET("posts/feed")
    Call<PostsResponse> getPosts(
            @Query("page") int page,
            @Query("size") int size
    );

    @GET("posts/myposts")
    Call <List<UserPostsWithReactionsDTO>> getMyPosts();

    @PUT("posts/{postId}/upvote")
    Call<ApiResponse> likePost(@Path("postId") long postId);

    @PUT("posts/{postId}/downvote")
    Call<ApiResponse> dislikePost(@Path("postId") long postId);
}
