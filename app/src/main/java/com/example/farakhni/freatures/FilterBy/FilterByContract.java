package com.example.farakhni.freatures.FilterBy;

import com.example.farakhni.model.Meal;
import java.util.List;

public interface FilterByContract {
    interface View {
        void showMeals(List<Meal> meals);
        void showError(String message);
    }

    interface Presenter {
        void attachView(View v);
        void detachView();
        void loadMeals(Meal[] mealsArray);
        void onMealSelected(Meal meal);
        void onFavoriteToggled(Meal meal);
    }

    interface Model {
        void saveFavorite(Meal meal);
        void deleteFavorite(Meal meal);
    }
}
