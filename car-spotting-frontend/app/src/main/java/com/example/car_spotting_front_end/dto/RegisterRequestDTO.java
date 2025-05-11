package com.example.car_spotting_front_end.dto;

public class RegisterRequestDTO {
    private String username;
    private String email;
    private String password;

    public RegisterRequestDTO(String username, String email, String password){
        this.email = email;
        this.username = username;
        this.password = password;
    }
}
