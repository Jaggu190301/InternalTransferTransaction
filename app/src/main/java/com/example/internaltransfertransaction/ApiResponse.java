package com.example.internaltransfertransaction;

import com.google.gson.annotations.SerializedName;

public class ApiResponse {
  private String message;

    public ApiResponse(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
