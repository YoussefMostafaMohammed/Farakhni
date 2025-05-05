package com.example.farakhni.freatures.favorite;

import androidx.lifecycle.LiveData;
import com.example.farakhni.data.DB.AppDataBase;
import com.example.farakhni.data.DB.FavoriteMealDAO;
import com.example.farakhni.model.FavoriteMeal;
import android.content.Context;
import java.util.List;

public class FavoriteModel implements FavoriteContract.Model {
    private final FavoriteMealDAO dao;

    public FavoriteModel(Context context) {
        AppDataBase db = AppDataBase.getInstance(context);
        this.dao = db.getFavoriteMealDAO();
    }

    @Override
    public LiveData<List<FavoriteMeal>> fetchFavorites() {
        return dao.getFavoriteMealsForUser();
    }

    @Override
    public void deleteFavorite(FavoriteMeal meal) {
        new Thread(() -> dao.deleteMeal(meal)).start();
    }
}
