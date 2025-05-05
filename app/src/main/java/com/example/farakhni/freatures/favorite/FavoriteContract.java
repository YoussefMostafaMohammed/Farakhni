package com.example.farakhni.freatures.favorite;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.example.farakhni.model.FavoriteMeal;
import com.example.farakhni.model.Meal;

import java.util.List;

public interface FavoriteContract {
    interface View {
        void showFavorites(List<Meal> favorites);
        void showError(String message);
    }

    interface Presenter {
        void attachView(View view);
        void detachView();
        void loadFavorites(LifecycleOwner owner);
        void removeFavorite(FavoriteMeal meal);
    }

    interface Model {
        LiveData<List<FavoriteMeal>> fetchFavorites();
        void deleteFavorite(FavoriteMeal meal);
    }
}