package com.example.farakhni.data.network.category;

import com.example.farakhni.model.CategoryListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CategoryService {
    @GET("categories.php")
    Call<CategoryListResponse> getCategories(); // Returns ProductResponse instead of List
}