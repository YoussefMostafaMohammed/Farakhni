package com.example.farakhni.freatures.FilterBy;

import android.content.Context;
import com.example.farakhni.data.repositories.MealRepositoryImpl;
import com.example.farakhni.model.FavoriteMeal;
import com.example.farakhni.model.Meal;

public class FilterByModel implements FilterByContract.Model {
    private final MealRepositoryImpl mealRepository;

    public FilterByModel(Context context) {
        this.mealRepository = MealRepositoryImpl.getInstance(context);
    }

    @Override
    public void toggleFavorite(Meal meal) {
        FavoriteMeal fav = new FavoriteMeal(meal);
        if (meal.isFavorite()) {
            mealRepository.insertFavoriteMeal(fav);
        } else {
            mealRepository.deleteFavoriteMeal(fav);
        }
    }
}