package com.project.app.models;

public class ResponseModel {
    private int code;
    private String message;

    // Constructor
    public ResponseModel(int code, String message) {
        this.code = code;
        this.message = message;
    }

    // Getters and setters
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
