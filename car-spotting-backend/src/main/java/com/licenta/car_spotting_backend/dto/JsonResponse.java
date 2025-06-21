package com.licenta.car_spotting_backend.dto;

public class JsonResponse {
    public int status;
    public String message;

    public JsonResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public static JsonResponse create(int status, String message){
        return new JsonResponse(status, message);
    }
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
