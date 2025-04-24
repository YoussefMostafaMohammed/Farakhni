package com.example.farakhni.network.meal;

import com.example.farakhni.model.MealListResponse;
import com.example.farakhni.model.Meal;
import com.example.farakhni.network.NetworkCallBack;
import com.example.farakhni.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// 2. Updated Remote Data Source
public class MealsRemoteDataSourceImpl implements MealsRemoteDataSoruce {
    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private static MealService mealService = null;
    private static MealsRemoteDataSourceImpl instance;


    public MealsRemoteDataSourceImpl() {
        mealService = RetrofitClient.getService(MealService.class);
    }
    public static synchronized MealsRemoteDataSourceImpl getInstance() {
        if (instance == null) {
            instance = new MealsRemoteDataSourceImpl();
        }
        return instance;
    }

    @Override
    public void makeNetworkCallgetMealByID(String mealId, NetworkCallBack<List<Meal>> networkCallBack) {
        mealService.getMealById(mealId).enqueue(new Callback<MealListResponse>() {
            @Override
            public void onResponse(Call<MealListResponse> call, Response<MealListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    networkCallBack.onSuccessResult(response.body().getMeals());
                } else {
                    networkCallBack.onFailureResult("Meal not found");
                }
            }

            @Override
            public void onFailure(Call<MealListResponse> call, Throwable t) {
                networkCallBack.onFailureResult(t.getMessage());
            }
        });
    }

    @Override
    public void makeNetworkCallgetMealByName(String mealName, NetworkCallBack<List<Meal>> networkCallBack) {
        mealService.searchMealsByName(mealName).enqueue(new Callback<MealListResponse>() {
            @Override
            public void onResponse(Call<MealListResponse> call, Response<MealListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    networkCallBack.onSuccessResult(response.body().getMeals());
                } else {
                    networkCallBack.onFailureResult("No meals found");
                }
            }

            @Override
            public void onFailure(Call<MealListResponse> call, Throwable t) {
                networkCallBack.onFailureResult(t.getMessage());
            }
        });
    }

    @Override
    public void makeNetworkCallgetRandomMeal(NetworkCallBack<List<Meal>> networkCallBack) {
        mealService.getRandomMeal().enqueue(new Callback<MealListResponse>() {
            @Override
            public void onResponse(Call<MealListResponse> call, Response<MealListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    networkCallBack.onSuccessResult(response.body().getMeals());
                } else {
                    networkCallBack.onFailureResult("Failed to get random meal");
                }
            }

            @Override
            public void onFailure(Call<MealListResponse> call, Throwable t) {
                networkCallBack.onFailureResult(t.getMessage());
            }
        });
    }
}