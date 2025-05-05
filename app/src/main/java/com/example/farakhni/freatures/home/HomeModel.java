package com.example.farakhni.freatures.home;

import com.example.farakhni.data.network.NetworkCallBack;
import com.example.farakhni.data.repositories.AreaRepositoryImpl;
import com.example.farakhni.data.repositories.CategoryRepositoryImpl;
import com.example.farakhni.data.repositories.IngredientRepositoryImpl;
import com.example.farakhni.data.repositories.MealRepositoryImpl;
import com.example.farakhni.model.Area;
import com.example.farakhni.model.Category;
import com.example.farakhni.model.Ingredient;
import com.example.farakhni.model.Meal;

import java.util.List;

public class HomeModel {
    private final MealRepositoryImpl mealRepository;
    private final IngredientRepositoryImpl ingredientRepository;
    private final CategoryRepositoryImpl categoryRepository;
    private final AreaRepositoryImpl areaRepository;

    public HomeModel(MealRepositoryImpl mealRepository,
                     IngredientRepositoryImpl ingredientRepository,
                     CategoryRepositoryImpl categoryRepository,
                     AreaRepositoryImpl areaRepository) {
        this.mealRepository = mealRepository;
        this.ingredientRepository = ingredientRepository;
        this.categoryRepository = categoryRepository;
        this.areaRepository = areaRepository;
    }

    public void getRandomMeal(Callback<List<Meal>> callback) {
        mealRepository.getRandomMeal(new NetworkCallBack<List<Meal>>() {
            @Override
            public void onSuccessResult(List<Meal> result) {
                callback.onSuccess(result);
            }

            @Override
            public void onFailureResult(String message) {
                callback.onError(message);
            }

            @Override
            public void onLoading() {
                callback.onLoading();
            }

            @Override
            public void onNetworkError(String errorMessage) {
                callback.onError("Network error: " + errorMessage);
            }

            @Override
            public void onEmptyData() {
                callback.onError("No meals found");
            }

            @Override
            public void onProgress(int progress) {
                // Not used
            }
        });
    }

    public void getIngredients(Callback<List<Ingredient>> callback) {
        ingredientRepository.getAllIngredients(new NetworkCallBack<List<Ingredient>>() {
            @Override
            public void onSuccessResult(List<Ingredient> result) {
                callback.onSuccess(result);
            }

            @Override
            public void onFailureResult(String message) {
                callback.onError(message);
            }

            @Override
            public void onLoading() {
                callback.onLoading();
            }

            @Override
            public void onNetworkError(String errorMessage) {
                callback.onError("Network error: " + errorMessage);
            }

            @Override
            public void onEmptyData() {
                callback.onError("No ingredients found");
            }

            @Override
            public void onProgress(int progress) {
                // Not used
            }
        });
    }

    public void getCategories(Callback<List<Category>> callback) {
        categoryRepository.getAllCategories(new NetworkCallBack<List<Category>>() {
            @Override
            public void onSuccessResult(List<Category> result) {
                callback.onSuccess(result);
            }

            @Override
            public void onFailureResult(String message) {
                callback.onError(message);
            }

            @Override
            public void onLoading() {
                callback.onLoading();
            }

            @Override
            public void onNetworkError(String errorMessage) {
                callback.onError("Network error: " + errorMessage);
            }

            @Override
            public void onEmptyData() {
                callback.onError("No categories found");
            }

            @Override
            public void onProgress(int progress) {
                // Not used
            }
        });
    }

    public void getAreas(Callback<List<Area>> callback) {
        areaRepository.getAllAreas(new NetworkCallBack<List<Area>>() {
            @Override
            public void onSuccessResult(List<Area> result) {
                callback.onSuccess(result);
            }

            @Override
            public void onFailureResult(String message) {
                callback.onError(message);
            }

            @Override
            public void onLoading() {
                callback.onLoading();
            }

            @Override
            public void onNetworkError(String errorMessage) {
                callback.onError("Network error: " + errorMessage);
            }

            @Override
            public void onEmptyData() {
                callback.onError("No areas found");
            }

            @Override
            public void onProgress(int progress) {
                // Not used
            }
        });
    }

    public interface Callback<T> {
        void onSuccess(T result);
        void onError(String message);
        void onLoading();
    }
}