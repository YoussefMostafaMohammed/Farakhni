package com.example.farakhni.freatures.mealdetails;

import androidx.lifecycle.LifecycleOwner;

import com.example.farakhni.model.FavoriteMeal;
import com.example.farakhni.model.Ingredient;

import java.util.List;

public interface MealDetailsContract {
    interface View {
        void showMealDetails(String name, String instructions, String imageUrl);
        void showIngredients(List<Ingredient> ingredients);
        void showVideo(String videoUrl);
        void hideVideo();
        void setFavoriteStatus(boolean isFavorite);
        void setScheduleStatus(boolean isScheduled);
        void showDatePicker();
        void showMessage(String message);
        void showError(String message);
        void navigateBack();
    }

    interface Presenter {
        void attachView(View view);
        void detachView();
        void initialize(FavoriteMeal meal, LifecycleOwner owner);
        void onFavoriteClicked();
        void onPlanMealClicked();
        void onDeleteMealClicked();
        void onDateSelected(String date);
    }

    interface Model {
        boolean isFavorite(String mealId);
        boolean isMealPlanned(String mealId, String date);
        void insertFavoriteMeal(FavoriteMeal meal);
        void deleteFavoriteMeal(FavoriteMeal meal);
        void insertPlannedMeal(String mealId, String name, String date);
        void deletePlannedMeal(String mealId, String name, String date);
    }
}