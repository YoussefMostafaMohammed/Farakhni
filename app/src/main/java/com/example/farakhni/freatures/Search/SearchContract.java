package com.example.farakhni.freatures.Search;

import androidx.lifecycle.LifecycleOwner;

import com.example.farakhni.data.network.NetworkCallBack;
import com.example.farakhni.model.Area;
import com.example.farakhni.model.Category;
import com.example.farakhni.model.FavoriteMeal;
import com.example.farakhni.model.Ingredient;
import com.example.farakhni.model.Meal;

import java.util.List;

public interface SearchContract {
    interface View {
        void showProgressBar();
        void hideProgressBar();
        void showMeals(List<Meal> meals);
        void showAreas(List<Area> areas);
        void showCategories(List<Category> categories);
        void showIngredients(List<Ingredient> ingredients);
        void setMealAdapter();
        void setAreaAdapter();
        void setCategoryAdapter();
        void setIngredientAdapter();
        void showMessage(String message);
        void showError(String message);
        void navigateToMealDetails(FavoriteMeal meal);
        void observeFavoriteMeals(LifecycleOwner owner);
    }

    interface Presenter {
        void attachView(View view);
        void detachView();
        void initialize(LifecycleOwner owner);
        void onSearchQuery(String query);
        void onFilterChanged(String filterType);
        void onFavoriteToggled(Meal meal);
        void onMealClicked(Meal meal);
        void observeFavoriteMeals(LifecycleOwner owner, SearchContract.View view);
    }

    interface Model {
        void getMealsByName(String name, NetworkCallBack<List<Meal>> callback);
        void getMealsByFirstLetter(String letter, NetworkCallBack<List<Meal>> callback);
        void getMealById(String id, NetworkCallBack<List<Meal>> callback);
        void getAllAreas(NetworkCallBack<List<Area>> callback);
        void getAllCategories(NetworkCallBack<List<Category>> callback);
        void getAllIngredients(NetworkCallBack<List<Ingredient>> callback);
        void insertFavoriteMeal(FavoriteMeal meal);
        void deleteFavoriteMeal(FavoriteMeal meal);
        List<Area> filterAreas(List<Area> areas, String query);
        List<Category> filterCategories(List<Category> categories, String query);
        List<Ingredient> filterIngredients(List<Ingredient> ingredients, String query);
        androidx.lifecycle.LiveData<List<FavoriteMeal>> getFavoriteMeals();
    }
}