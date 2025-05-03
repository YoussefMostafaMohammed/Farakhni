package com.example.farakhni.freatures.favorite;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import com.example.farakhni.model.Meal;
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
        model.fetchFavorites().observe(owner, new Observer<List<Meal>>() {
            @Override
            public void onChanged(List<Meal> meals) {
                if (view != null) {
                    for (Meal m : meals) m.setFavorite(true);
                    view.showFavorites(meals);
                }
            }
        });
    }

    @Override
    public void removeFavorite(Meal meal) {
        model.deleteFavorite(meal);
    }
}
