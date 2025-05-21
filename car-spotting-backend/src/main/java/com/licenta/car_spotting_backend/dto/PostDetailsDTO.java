package com.licenta.car_spotting_backend.dto;

import com.licenta.car_spotting_backend.enums.ReactionType;

public class PostDetailsDTO {
    private Long post_id;
    private String userName;
    private String carMake;
    private String carModel;
    private int carYear;
    private String carImagePath;
    private float postScore;
    private ReactionType reactionType;

    public PostDetailsDTO(Long post_id, String userName, String carMake, String carModel, int carYear, String carImagePath, float postScore, ReactionType reactionType) {
        this.post_id = post_id;
        this.userName = userName;
        this.carMake = carMake;
        this.carModel = carModel;
        this.carYear = carYear;
        this.carImagePath = carImagePath;
        this.postScore = postScore;
        this.reactionType = reactionType;
    }

    public ReactionType getReactionType() {
        return reactionType;
    }

    public void setReactionType(ReactionType reactionType) {
        this.reactionType = reactionType;
    }

    public Long getPost_id() {
        return post_id;
    }

    public void setPost_id(Long post_id) {
        this.post_id = post_id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCarMake() {
        return carMake;
    }

    public void setCarMake(String carMake) {
        this.carMake = carMake;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public int getCarYear() {
        return carYear;
    }

    public void setCarYear(int carYear) {
        this.carYear = carYear;
    }

    public String getCarImagePath() {
        return carImagePath;
    }

    public void setCarImagePath(String carImagePath) {
        this.carImagePath = carImagePath;
    }

    public float getPostScore() {
        return postScore;
    }

    public void setPostScore(float postScore) {
        this.postScore = postScore;
    }
}
