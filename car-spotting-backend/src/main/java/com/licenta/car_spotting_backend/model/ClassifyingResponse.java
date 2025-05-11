package com.licenta.car_spotting_backend.model;

public class ClassifyingResponse {
    private String carName;
    private String confidence;

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getConfidence() {
        return confidence;
    }

    public void setConfidence(String confidence) {
        this.confidence = confidence;
    }
}
