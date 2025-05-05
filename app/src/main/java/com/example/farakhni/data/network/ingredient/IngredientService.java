package com.example.farakhni.data.network.ingredient;

import com.example.farakhni.model.IngredientListResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IngredientService {
    @GET("list.php")
    Call<IngredientListResponse> getIngredients(@Query("i") String strIngredient);
}