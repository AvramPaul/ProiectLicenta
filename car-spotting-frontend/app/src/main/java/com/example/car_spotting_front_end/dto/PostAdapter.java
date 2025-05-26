package com.example.car_spotting_front_end.dto;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.car_spotting_front_end.R;
import com.example.car_spotting_front_end.enums.ReactionType;
import com.example.car_spotting_front_end.model.Post;
import com.example.car_spotting_front_end.retrofit.ApiResponse;
import com.example.car_spotting_front_end.retrofit.ApiServices;
import com.example.car_spotting_front_end.retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> posts;
    private ApiServices apiServices;
    private Context context;

    public PostAdapter(List<Post> posts, Context context) {
        this.posts = posts;
        this.context = context;
        Retrofit retrofit = RetrofitClient.getClient(context);
        apiServices = retrofit.create(ApiServices.class);
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Post post = posts.get(position);
        String username = post.getUsername();
        String initial = username != null && !username.isEmpty() ? username.substring(0, 1).toUpperCase() : "?";
        holder.avatarTextView.setText(initial);
        holder.usernameTextView.setText(post.getUsername());
        holder.scoreTextView.setText(String.valueOf((int)post.getScore()));
        holder.carMakeTextView.setText(post.getCarMake());
        holder.carModelTextView.setText(post.getCarModel());
        holder.carYearTextView.setText(String.valueOf(post.getCarYear()));

        String imageUrl = "http://10.0.2.2:8081/posts/images/" + post.getpostImagePath();
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.placeholder) // imagine fallback
                .into(holder.postImageView);

        holder.upvoteButton.setImageResource(R.drawable.ic_upvote);
        holder.downvoteButton.setImageResource(R.drawable.ic_downvote);

        if (ReactionType.LIKE.equals(post.getReactionType())) {
            holder.upvoteButton.setImageResource(R.drawable.ic_upvote_filled);
        } else if (ReactionType.DISLIKE.equals(post.getReactionType())) {
            holder.downvoteButton.setImageResource(R.drawable.ic_downvote_filled);
        }


        holder.upvoteButton.setOnClickListener(v -> {
            Call<ApiResponse> call = apiServices.likePost(post.getPostId());
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable throwable) {
                    Toast.makeText(context, "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            if (ReactionType.LIKE.equals(post.getReactionType())) {
                post.setReactionType(null);
                post.setPostScore(post.getScore() - 1);
                holder.upvoteButton.setImageResource(R.drawable.ic_upvote);
                notifyItemChanged(position);
                return;
            } else if (ReactionType.DISLIKE.equals(post.getReactionType())) {
                post.setReactionType(ReactionType.LIKE);
                post.setPostScore(post.getScore() + 2);
                holder.upvoteButton.setImageResource(R.drawable.ic_upvote_filled);
                holder.downvoteButton.setImageResource(R.drawable.ic_downvote);
                notifyItemChanged(position);
                return;
            }
            post.setReactionType(ReactionType.LIKE);
            post.setPostScore(post.getScore() + 1);
            notifyItemChanged(position);
        });

        holder.downvoteButton.setOnClickListener(v -> {

            Call<ApiResponse> call = apiServices.dislikePost(post.getPostId());
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable throwable) {
                    Toast.makeText(context, "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            if (ReactionType.DISLIKE.equals(post.getReactionType())) {
                post.setReactionType(null);
                post.setPostScore(post.getScore() + 1);
                holder.downvoteButton.setImageResource(R.drawable.ic_downvote);
                notifyItemChanged(position);
                return;
            } else if (ReactionType.LIKE.equals(post.getReactionType())) {
                post.setReactionType(ReactionType.DISLIKE);
                post.setPostScore(post.getScore() - 2);
                holder.upvoteButton.setImageResource(R.drawable.ic_upvote);
                holder.downvoteButton.setImageResource(R.drawable.ic_downvote_filled);
                notifyItemChanged(position);
                return;
            }
            post.setReactionType(ReactionType.DISLIKE);
            post.setPostScore(post.getScore() - 1);
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

     public static class PostViewHolder extends RecyclerView.ViewHolder {

            TextView usernameTextView, scoreTextView;
            TextView carMakeTextView, carModelTextView, carYearTextView;
            ImageView postImageView;
            ImageView upvoteButton, downvoteButton;
            TextView avatarTextView;

            public PostViewHolder(@NonNull View itemView) {
                super(itemView);
                usernameTextView = itemView.findViewById(R.id.usernameTextView);
                postImageView = itemView.findViewById(R.id.postImageView);
                scoreTextView = itemView.findViewById(R.id.scoreTextView);
                carMakeTextView = itemView.findViewById(R.id.carMakeTextView);
                carModelTextView = itemView.findViewById(R.id.carModelTextView);
                carYearTextView = itemView.findViewById(R.id.carYearTextView);
                postImageView = itemView.findViewById(R.id.postImageView);
                upvoteButton = itemView.findViewById(R.id.upvoteButton);
                downvoteButton = itemView.findViewById(R.id.downvoteButton);
                avatarTextView = itemView.findViewById(R.id.avatarTextView);
            }
        }
}
