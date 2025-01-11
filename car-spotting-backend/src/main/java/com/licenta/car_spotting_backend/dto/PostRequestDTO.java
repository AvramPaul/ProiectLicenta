package com.licenta.car_spotting_backend.dto;

public class PostRequestDTO {
        private String username; // Cine postează
        private String make;     // Marca mașinii
        private String model;    // Modelul mașinii
        private int year;        // Anul mașinii
        private byte[] image;    // Imaginea mașinii

        // Getters și Setters
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
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

