package com.example.car_spotting_front_end.dto;


public class PostDetailsDTO {
    private Long id;
    private String userName;
    private String carMake;
    private String carModel;
    private int carYear;
    private byte[] carImage;
    private float postScore;

    // Constructor
    public PostDetailsDTO(Long id, String userName, String carMake,
                          String carModel, int carYear, byte[] carImage, float postScore) {
        this.id = id;
        this.userName = userName;
        this.carMake = carMake;
        this.carModel = carModel;
        this.carYear = carYear;
        this.carImage = carImage;
        this.postScore = postScore;
    }

    // Getters
    public Long getId() { return id; }
    public String getUserName() { return userName; }
    public String getCarMake() { return carMake; }
    public String getCarModel() { return carModel; }
    public int getCarYear() { return carYear; }
    public byte[] getCarImage() { return carImage; }
    public float getPostScore() { return postScore; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setUserName(String userName) { this.userName = userName; }
    public void setCarMake(String carMake) { this.carMake = carMake; }
    public void setCarModel(String carModel) { this.carModel = carModel; }
    public void setCarYear(int carYear) { this.carYear = carYear; }
    public void setCarImage(byte[] carImage) { this.carImage = carImage; }
    public void setPostScore(float postScore) { this.postScore = postScore; }
}