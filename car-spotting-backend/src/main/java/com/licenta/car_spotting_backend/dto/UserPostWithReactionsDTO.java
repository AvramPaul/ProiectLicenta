package com.licenta.car_spotting_backend.dto;

import java.util.List;

public class UserPostWithReactionsDTO {
    private Long id;
    private float score;
    private String carMake;
    private String carModel;
    private int carYear;
    private String carImagePath;
    private List<String> usersThatLiked;
    private List<String> usersThatDisliked;

    public UserPostWithReactionsDTO(Long id, float score, String carMake, String carModel, int carYear, String carImagePath, List<String> usersThatLiked, List<String> usersThatDisliked) {
        this.id = id;
        this.score = score;
        this.carMake = carMake;
        this.carModel = carModel;
        this.carYear = carYear;
        this.carImagePath = carImagePath;
        this.usersThatLiked = usersThatLiked;
        this.usersThatDisliked = usersThatDisliked;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
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

    public List<String> getUsersThatLiked() {
        return usersThatLiked;
    }

    public void setUsersThatLiked(List<String> usersThatLiked) {
        this.usersThatLiked = usersThatLiked;
    }

    public List<String> getUsersThatDisliked() {
        return usersThatDisliked;
    }

    public void setUsersThatDisliked(List<String> usersThatDisliked) {
        this.usersThatDisliked = usersThatDisliked;
    }
}
