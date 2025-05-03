package com.example.farakhni.freatures.home;

import com.example.farakhni.model.Area;
import com.example.farakhni.model.Category;
import com.example.farakhni.model.Ingredient;
import com.example.farakhni.model.Meal;
import com.example.farakhni.data.network.NetworkCallBack;

import java.util.List;

public interface HomeContract {
    interface View {
        void showRandomMeals(List<Meal> meals);
        void showIngredients(List<Ingredient> ingredients);
        void showCategories(List<Category> categories);
        void showAreas(List<Area> areas);
        void showError(String message);
    }

    interface Presenter {
        void attachView(View view);
        void detachView();
        void loadHomeData();
    }

    interface Model {
        void getRandomMeal(NetworkCallBack<List<Meal>> callback);
        void getAllIngredients(NetworkCallBack<List<Ingredient>> callback);
        void getAllCategories(NetworkCallBack<List<Category>> callback);
        void getAllAreas(NetworkCallBack<List<Area>> callback);
    }
}
