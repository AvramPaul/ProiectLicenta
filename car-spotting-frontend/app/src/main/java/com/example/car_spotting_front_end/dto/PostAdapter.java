package com.example.car_spotting_front_end.dto;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.car_spotting_front_end.R;
import com.example.car_spotting_front_end.model.Post;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> posts;

    public PostAdapter(List<Post> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.usernameTextView.setText(post.getUsername());
        holder.scoreTextView.setText(String.valueOf(post.getScore()));
        holder.carMakeTextView.setText(post.getCarMake());
        holder.carModelTextView.setText(post.getCarModel());
        holder.carYearTextView.setText(String.valueOf(post.getCarYear()));

        String imageUrl = "http://10.0.2.2:8081/posts/images/" + post.getpostImagePath();
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.placeholder) // imagine fallback
                .into(holder.postImageView);

        // Buttons do nothing for now
        holder.upvoteButton.setOnClickListener(v -> {
            // To be implemented
        });

        holder.downvoteButton.setOnClickListener(v -> {
            // To be implemented
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
            Button upvoteButton, downvoteButton;

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
            }
        }
}
