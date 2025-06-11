package com.example.car_spotting_front_end.dto;

import android.app.AlertDialog;
import android.content.Context;
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

import java.util.List;

public class MyPostsAdapter extends RecyclerView.Adapter<MyPostsAdapter.MyPostViewHolder> {

    private List<UserPostsWithReactionsDTO> posts;
    private Context context;

    public MyPostsAdapter(List<UserPostsWithReactionsDTO> posts, Context context) {
        this.posts = posts;
        this.context = context;
    }

    @NonNull
    @Override
    public MyPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_post, parent, false);
        return new MyPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPostViewHolder holder, int position) {
        UserPostsWithReactionsDTO post = posts.get(position);

        holder.carMakeTextView.setText(post.getCarMake());
        holder.carModelTextView.setText(post.getCarModel());
        holder.carYearTextView.setText(String.valueOf(post.getCarYear()));
        holder.scoreTextView.setText(String.valueOf(post.getScore()));

        String imageUrl = "http://10.0.2.2:8081/posts/images/" + post.getCarImagePath();
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .into(holder.carImageView);

        holder.btnLikes.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Users who liked")
                    .setItems(post.getUsersThatLiked().toArray(new String[0]), null)
                    .setPositiveButton("Close", null)
                    .show();
        });

        holder.btnDislikes.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Users who disliked")
                    .setItems(post.getUsersThatDisliked().toArray(new String[0]), null)
                    .setPositiveButton("Close", null)
                    .show();
        });
    }

    public static class MyPostViewHolder extends RecyclerView.ViewHolder {
        TextView carMakeTextView, carModelTextView, carYearTextView, scoreTextView;
        ImageView carImageView;
        Button btnLikes, btnDislikes;

        public MyPostViewHolder(@NonNull View itemView) {
            super(itemView);

            carMakeTextView = itemView.findViewById(R.id.carMakeTextView);
            carModelTextView = itemView.findViewById(R.id.carModelTextView);
            carYearTextView = itemView.findViewById(R.id.carYearTextView);
            scoreTextView = itemView.findViewById(R.id.scoreTextView);
            carImageView = itemView.findViewById(R.id.carImageView);
            btnLikes = itemView.findViewById(R.id.btnLikes);
            btnDislikes = itemView.findViewById(R.id.btnDislikes);
        }
    }
    @Override
    public int getItemCount() {
        return posts.size();
    }
}

