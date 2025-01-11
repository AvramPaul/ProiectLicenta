package com.licenta.car_spotting_backend.dto;

public class PostDetailsDTO {
    private Long post_id;
    private String userName;
    private String carMake;
    private String carModel;
    private int carYear;
    private byte[] carImage;
    private float postScore;

    public PostDetailsDTO(Long post_id, String userName, String carMake, String carModel, int carYear, byte[] carImage, float postScore) {
        this.post_id = post_id;
        this.userName = userName;
        this.carMake = carMake;
        this.carModel = carModel;
        this.carYear = carYear;
        this.carImage = carImage;
        this.postScore = postScore;
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

    public byte[] getCarImage() {
        return carImage;
    }

    public void setCarImage(byte[] carImage) {
        this.carImage = carImage;
    }

    public float getPostScore() {
        return postScore;
    }

    public void setPostScore(float postScore) {
        this.postScore = postScore;
    }
}
