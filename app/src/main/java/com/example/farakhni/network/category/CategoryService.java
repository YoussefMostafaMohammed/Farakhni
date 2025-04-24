package com.example.farakhni.network.category;

import com.example.farakhni.model.CategoryListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CategoryService {
    @GET("list.php")
    Call<CategoryListResponse> getCategories(@Query("c") String strCategory); // Returns ProductResponse instead of List
}