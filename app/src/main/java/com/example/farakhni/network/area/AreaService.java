package com.example.farakhni.network.area;


import com.example.farakhni.model.AreaListResponse;
import com.example.farakhni.model.MealListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AreaService {
    @GET("list.php")
    Call<AreaListResponse> getAllAreas(@Query("a") String strArea); // Returns ProductListResponse instead of List
}