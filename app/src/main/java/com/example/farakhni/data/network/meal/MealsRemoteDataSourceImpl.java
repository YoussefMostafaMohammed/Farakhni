package com.example.farakhni.data.network.meal;

import com.example.farakhni.model.MealListResponse;
import com.example.farakhni.model.Meal;
import com.example.farakhni.data.network.NetworkCallBack;
import com.example.farakhni.data.network.RetrofitClient;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// 2. Updated Remote Data Source
public class MealsRemoteDataSourceImpl implements MealsRemoteDataSoruce {
    private static MealService mealService = null;
    private static MealsRemoteDataSourceImpl instance;


    private MealsRemoteDataSourceImpl() {
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
                    networkCallBack.onSuccessResult(response.body().getAllMeals());
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
        mealService.getMealsByName(mealName).enqueue(new Callback<MealListResponse>() {
            @Override
            public void onResponse(Call<MealListResponse> call, Response<MealListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    networkCallBack.onSuccessResult(response.body().getAllMeals());
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
                    networkCallBack.onSuccessResult(response.body().getAllMeals());
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
    @Override
    public void makeNetworkCallgetMealByFirstLetter(String charFirstLetter, NetworkCallBack<List<Meal>> networkCallBack) {
        mealService.getMealsByFirstLetter(charFirstLetter).enqueue(new Callback<MealListResponse>() {
            @Override
            public void onResponse(Call<MealListResponse> call, Response<MealListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    networkCallBack.onSuccessResult(response.body().getAllMeals());
                } else {
                    networkCallBack.onFailureResult("Failed to get meal starts with this letter");
                }
            }

            @Override
            public void onFailure(Call<MealListResponse> call, Throwable t) {
                networkCallBack.onFailureResult(t.getMessage());
            }
        });
    }
    @Override
    public void makeNetworkCallFilterByIngredient(String ingredient, NetworkCallBack<List<Meal>> networkCallBack) {
        mealService.filterByIngredient(ingredient).enqueue(new Callback<MealListResponse>() {
            @Override
            public void onResponse(Call<MealListResponse> call, Response<MealListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    networkCallBack.onSuccessResult(response.body().getAllMeals());
                } else {
                    networkCallBack.onFailureResult("No meals found with this ingredient");
                }
            }
            @Override
            public void onFailure(Call<MealListResponse> call, Throwable t) {
                networkCallBack.onFailureResult(t.getMessage());
            }
        });
    }

    @Override
    public void makeNetworkCallFilterByCategory(String category, NetworkCallBack<List<Meal>> networkCallBack) {
        mealService.filterByCategory(category).enqueue(new Callback<MealListResponse>() {
            @Override
            public void onResponse(Call<MealListResponse> call, Response<MealListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    networkCallBack.onSuccessResult(response.body().getAllMeals());
                } else {
                    networkCallBack.onFailureResult("No meals found with this ingredient");
                }
            }
            @Override
            public void onFailure(Call<MealListResponse> call, Throwable t) {
                networkCallBack.onFailureResult(t.getMessage());
            }
        });
    }

    @Override
    public void makeNetworkCallFilterByArea(String area, NetworkCallBack<List<Meal>> networkCallBack) {
        mealService.filterByArea(area).enqueue(new Callback<MealListResponse>() {
            @Override
            public void onResponse(Call<MealListResponse> call, Response<MealListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    networkCallBack.onSuccessResult(response.body().getAllMeals());
                } else {
                    networkCallBack.onFailureResult("No meals found with this ingredient");
                }
            }
            @Override
            public void onFailure(Call<MealListResponse> call, Throwable t) {
                networkCallBack.onFailureResult(t.getMessage());
            }
        });
    }




}