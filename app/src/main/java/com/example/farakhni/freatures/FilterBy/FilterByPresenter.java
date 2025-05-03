// com/example/farakhni/freatures/FilterBy/FilterByPresenter.java
package com.example.farakhni.freatures.FilterBy;

import androidx.annotation.Nullable;
import com.example.farakhni.model.Meal;
import java.util.Arrays;
import java.util.List;

public class FilterByPresenter implements FilterByContract.Presenter {
    @Nullable private FilterByContract.View view;
    private final FilterByContract.Model model;

    public FilterByPresenter(FilterByContract.Model model) {
        this.model = model;
    }

    @Override
    public void attachView(FilterByContract.View v) {
        this.view = v;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void loadMeals(Meal[] mealsArray) {
        if (view == null) return;
        if (mealsArray == null || mealsArray.length == 0) {
            view.showError("No meals to display");
        } else {
            List<Meal> list = Arrays.asList(mealsArray);
            view.showMeals(list);
        }
    }

    @Override
    public void onMealSelected(Meal meal) {
        if (view == null) return;
        // Delegate navigation back to the View
        // View will handle fragment navigation
    }

    @Override
    public void onFavoriteToggled(Meal meal) {
        if (meal.isFavorite()) {
            model.saveFavorite(meal);
        } else {
            model.deleteFavorite(meal);
        }
    }
}
