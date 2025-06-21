package com.example.car_spotting_front_end.model;

import com.example.car_spotting_front_end.enums.ReactionType;

public class Post {
    private long post_id;
    private String userName;
    private String carMake;
    private String carModel;
    private int carYear;
    private String carImagePath;
    private float postScore;
    private ReactionType reactionType;

    // Constructors, getters, and setters
    public Post(long id, String username, String carMake, String carModel, int carYear, String imagePath, float score, ReactionType reactionType) {
        this.post_id = id;
        this.userName = username;
        this.carMake = carMake;
        this.carModel = carModel;
        this.carYear = carYear;
        this.carImagePath = imagePath;
        this.postScore = score;
        this.reactionType = reactionType;
    }
    public void setPostScore(float postScore) {
        this.postScore = postScore;
    }
    public ReactionType getReactionType() {
        return reactionType;
    }

    public void setReactionType(ReactionType reactionType) {
        this.reactionType = reactionType;
    }

    public long getPostId() {
        return post_id;
    }

    public String getUsername() {
        return userName;
    }

    public String getCarMake() {
        return carMake;
    }

    public String getCarModel() {
        return carModel;
    }

    public int getCarYear() {
        return carYear;
    }

    public String getpostImagePath() {
        return carImagePath;
    }

    public float getScore() {
        return postScore;
    }
}