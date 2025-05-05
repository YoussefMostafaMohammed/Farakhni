package com.example.farakhni.freatures.FilterBy;

import com.example.farakhni.model.FavoriteMeal;
import com.example.farakhni.model.Meal;
import java.util.List;

public interface FilterByContract {
    interface View {
        void showMeals(List<Meal> meals);
        void showError(String message);
    }

    interface Presenter {
        void attachView(View view);

        void detachView();
        void loadMeals(List<Meal> meals);
        void onFavoriteToggled(Meal meal);
    }

    interface Model {
        void toggleFavorite(Meal meal);
    }
}