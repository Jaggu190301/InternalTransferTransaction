package com.example.internaltransfertransaction;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("Driver")
    Call<List<Driver>> getDrivers();

    @GET("Vehicle")
    Call<List<Vehicle>> getVehicle();

    @GET("Location")
    Call<List<Location>> getLocation();

    // Define endpoint for submitting data (POST request)
    @POST("InternalTransferTransaction") // Replace with the actual endpoint for submitting data
    Call<ApiResponse> postData(@Body PostDataModel postData);
}
