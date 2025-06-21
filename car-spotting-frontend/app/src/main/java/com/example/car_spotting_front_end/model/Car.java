package com.example.car_spotting_front_end.model;

public class Car {

    private Long id;
    private String make;
    private String model;
    private int year;
    private byte[] image;

    public Car(Long id, String make, String model, int year, byte[] image) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.year = year;
        this.image = image;
    }

    public Car() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
