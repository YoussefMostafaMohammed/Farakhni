package com.example.farakhni.data.repositories;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;

import com.example.farakhni.data.DB.FavoriteMealsLocalDataSourceImpl;
import com.example.farakhni.data.DB.PlannedMealsLocalDataSourceImpl;
import com.example.farakhni.data.network.NetworkCallBack;
import com.example.farakhni.data.network.meal.MealsRemoteDataSourceImpl;
import com.example.farakhni.model.FavoriteMeal;
import com.example.farakhni.model.Meal;
import com.example.farakhni.model.PlannedMeal;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MealRepositoryImpl implements MealRepository {
    private final Context context;
    private final MealsRemoteDataSourceImpl remoteDataSource;
    private final FavoriteMealsLocalDataSourceImpl favoriteDataSource;
    private final PlannedMealsLocalDataSourceImpl plannedDataSource;
    private static MealRepositoryImpl instance;
    private static final String PREF_NAME = "meal_of_the_day_prefs";
    private static final String KEY_MEALS_JSON = "meals_json";
    private static final String KEY_DATE = "date";

    private MealRepositoryImpl(Context context, MealsRemoteDataSourceImpl remoteDataSource,
                               FavoriteMealsLocalDataSourceImpl favoriteDataSource,
                               PlannedMealsLocalDataSourceImpl plannedDataSource) {
        this.context = context.getApplicationContext();
        this.remoteDataSource = remoteDataSource;
        this.favoriteDataSource = favoriteDataSource;
        this.plannedDataSource = plannedDataSource;
    }

    public static synchronized MealRepositoryImpl getInstance(Context context) {
        if (instance == null) {
            Context appContext = context.getApplicationContext();
            instance = new MealRepositoryImpl(appContext,
                    MealsRemoteDataSourceImpl.getInstance(),
                    FavoriteMealsLocalDataSourceImpl.getInstance(appContext),
                    PlannedMealsLocalDataSourceImpl.getInstance(appContext));
        }
        return instance;
    }

    @Override
    public LiveData<List<FavoriteMeal>> getFavoriteMeals() {
        return favoriteDataSource.getFavoriteMeals();
    }

    @Override
    public void getMealById(String mealId, NetworkCallBack<List<Meal>> callback) {
        remoteDataSource.makeNetworkCallgetMealByID(mealId, callback);
    }

    @Override
    public void searchMealsByName(String mealName, NetworkCallBack<List<Meal>> callback) {
        remoteDataSource.makeNetworkCallgetMealByName(mealName, callback);
    }

    @Override
    public void getRandomMeal(NetworkCallBack<List<Meal>> callback) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String storedDate = prefs.getString(KEY_DATE, "");
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        if (storedDate.equals(today)) {
            String mealsJson = prefs.getString(KEY_MEALS_JSON, "");
            if (!mealsJson.isEmpty()) {
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Meal>>(){}.getType();
                    List<Meal> meals = gson.fromJson(mealsJson, type);
                    callback.onSuccessResult(meals);
                    return;
                } catch (Exception e) {
                    // Parsing failed, proceed to fetch new meals
                }
            }
        }
        remoteDataSource.makeNetworkCallgetRandomMeal(new NetworkCallBack<List<Meal>>() {
            @Override
            public void onSuccessResult(List<Meal> result) {
                Gson gson = new Gson();
                String json = gson.toJson(result);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(KEY_MEALS_JSON, json);
                editor.putString(KEY_DATE, today);
                editor.apply();
                callback.onSuccessResult(result);
            }

            @Override
            public void onFailureResult(String errorMessage) {
                callback.onFailureResult(errorMessage);
            }

            @Override
            public void onLoading() {
                callback.onLoading();
            }

            @Override
            public void onNetworkError(String errorMessage) {
                callback.onNetworkError(errorMessage);
            }

            @Override
            public void onEmptyData() {
                callback.onEmptyData();
            }

            @Override
            public void onProgress(int progress) {
                callback.onProgress(progress);
            }
        });
    }

    @Override
    public void getMealsByFirstLetter(String firstLetter, NetworkCallBack<List<Meal>> callback) {
        remoteDataSource.makeNetworkCallgetMealByFirstLetter(firstLetter, callback);
    }

    @Override
    public void filterByIngredient(String ingredient, NetworkCallBack<List<Meal>> callback) {
        remoteDataSource.makeNetworkCallFilterByIngredient(ingredient, callback);
    }

    @Override
    public void filterByCategory(String category, NetworkCallBack<List<Meal>> callback) {
        remoteDataSource.makeNetworkCallFilterByCategory(category, callback);
    }

    @Override
    public void filterByArea(String area, NetworkCallBack<List<Meal>> callback) {
        remoteDataSource.makeNetworkCallFilterByArea(area, callback);
    }

    @Override
    public void insertFavoriteMeal(FavoriteMeal meal) {
        favoriteDataSource.insertMeal(meal);
    }

    @Override
    public void deleteFavoriteMeal(FavoriteMeal meal) {
        favoriteDataSource.deleteMeal(meal);
    }

    @Override
    public boolean isFavorite(String mealId) {
        return favoriteDataSource.isFavorite(mealId);
    }

    @Override
    public LiveData<List<PlannedMeal>> getAllPlannedMeals() {
        return plannedDataSource.getAllPlannedMeals();
    }

    @Override
    public void insertPlannedMeal(PlannedMeal plannedMeal) {
        plannedDataSource.insertPlannedMeal(plannedMeal);
    }

    @Override
    public void deletePlannedMeal(PlannedMeal plannedMeal) {
        plannedDataSource.deletePlannedMeal(plannedMeal);
    }

    @Override
    public boolean isMealPlanned(String mealId, String date) {
        return plannedDataSource.isMealPlanned(mealId, date);
    }
}