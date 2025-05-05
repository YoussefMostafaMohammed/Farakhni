package com.example.farakhni.data.network.meal;

import com.example.farakhni.model.MealListResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealService {
    @GET("search.php")
    Call<MealListResponse> getMealsByName(@Query("s") String searchQuery);

    @GET("search.php")
    Call<MealListResponse> getMealsByFirstLetter(@Query("f") String charFirstLetter);

    @GET("lookup.php")
    Call<MealListResponse> getMealById(@Query("i") String mealId);

    @GET("random.php")
    Call<MealListResponse> getRandomMeal();

    @GET("filter.php")
    Call<MealListResponse> filterByCategory(@Query("c") String category);

    @GET("filter.php")
    Call<MealListResponse> filterByArea(@Query("a") String area);

    @GET("filter.php")
    Call<MealListResponse> filterByIngredient(@Query("i") String ingredient);
}