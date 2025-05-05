package com.example.farakhni.freatures.home;

import com.example.farakhni.model.Area;
import com.example.farakhni.model.Category;
import com.example.farakhni.model.Ingredient;
import com.example.farakhni.model.Meal;

import java.util.List;

public interface HomeContract {
    interface View {
        void showRandomMeals(List<Meal> meals);
        void showIngredients(List<Ingredient> ingredients);
        void showCategories(List<Category> categories);
        void showAreas(List<Area> areas);
        void showError(String message);
        void showLoading(boolean isLoading);
    }

    interface Presenter {
        void attachView(View view);
        void detachView();
        void loadHomeData();
    }
}