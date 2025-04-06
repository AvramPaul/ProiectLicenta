package com.example.car_spotting_front_end.model;

public class Post  {
    private long id;
    private float score;
    private Car car;
    private User user;
    public Post() {
    }

    public Post(long id, float score, Car car, User user) {
        this.id = id;
        this.score = score;
        this.car = car;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
