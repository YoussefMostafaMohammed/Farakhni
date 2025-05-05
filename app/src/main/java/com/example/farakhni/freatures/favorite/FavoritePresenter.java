package com.example.farakhni.freatures.favorite;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.example.farakhni.model.FavoriteMeal;
import com.example.farakhni.model.Meal;

import java.util.ArrayList;
import java.util.List;

public class FavoritePresenter implements FavoriteContract.Presenter {
    private FavoriteContract.View view;
    private final FavoriteContract.Model model;

    public FavoritePresenter(FavoriteContract.Model model) {
        this.model = model;
    }

    @Override
    public void attachView(FavoriteContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void loadFavorites(LifecycleOwner owner) {
        model.fetchFavorites().observe(owner, new Observer<List<FavoriteMeal>>() {
            @Override
            public void onChanged(List<FavoriteMeal> meals) {
                if (view != null) {
                    if (meals == null || meals.isEmpty()) {
                        view.showError("No favorite meals found");
                        return;
                    }
                    List<Meal> mealList = new ArrayList<>();
                    for (FavoriteMeal favMeal : meals) {
                        Meal meal = new Meal(favMeal); // Assumes Meal has a constructor from FavoriteMeal
                        meal.setFavorite(true);
                        mealList.add(meal);
                    }
                    view.showFavorites(mealList);
                }
            }
        });
    }

    @Override
    public void removeFavorite(FavoriteMeal meal) {
        model.deleteFavorite(meal);
    }
}