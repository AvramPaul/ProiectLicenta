package com.licenta.car_spotting_backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import org.springframework.boot.autoconfigure.web.WebProperties;

@Entity
@Table(name = "post")
public class Post  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private float score;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "car_id", referencedColumnName = "id", nullable = false)
    private Car car;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
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
