package com.example.car_spotting_front_end.model;

public class PostRequestDTO {
    private String usrname;
    private String make;
    private String model;
    private int year;
    private byte[] image;

    public PostRequestDTO(String usrname, String make, String model, int year, byte[] image) {
        this.usrname = usrname;
        this.make = make;
        this.model = model;
        this.year = year;
        this.image = image;
    }

    public String getUsrname() {
        return usrname;
    }

    public void setUsrname(String usrname) {
        this.usrname = usrname;
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
