package com.example.farakhni.freatures.FilterBy;

import com.example.farakhni.model.Meal;
import java.util.ArrayList;
import java.util.List;

public class FilterByPresenter implements FilterByContract.Presenter {
    private FilterByContract.View view;
    private final FilterByContract.Model model;

    public FilterByPresenter(FilterByContract.Model model) {
        this.model = model;
    }

    @Override
    public void attachView(FilterByContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void loadMeals(List<Meal> meals) {
        if (view == null) return;
        if (meals == null || meals.isEmpty()) {
            view.showError("No meals provided");
        } else {
            view.showMeals(new ArrayList<>(meals));
        }
    }

    @Override
    public void onFavoriteToggled(Meal meal) {
        model.toggleFavorite(meal);
    }
}
