package com.example.farakhni.freatures.home;

import com.example.farakhni.model.Area;
import com.example.farakhni.model.Category;
import com.example.farakhni.model.Ingredient;
import com.example.farakhni.model.Meal;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class HomePresenter implements HomeContract.Presenter {
    private HomeContract.View view;
    private final HomeModel model;

    public HomePresenter(HomeModel model) {
        this.model = model;
    }

    @Override
    public void attachView(HomeContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void loadHomeData() {
        if (view == null) return;

        model.getRandomMeal(new HomeModel.Callback<List<Meal>>() {
            @Override
            public void onSuccess(List<Meal> result) {
                if (view != null) {
                    view.showRandomMeals(result);
                }
            }

            @Override
            public void onError(String message) {
                if (view != null) {
                    view.showError(message);
                }
            }

            @Override
            public void onLoading() {
            }
        });

        model.getIngredients(new HomeModel.Callback<List<Ingredient>>() {
            @Override
            public void onSuccess(List<Ingredient> result) {
                if (view != null) {
                    view.showIngredients(result);
                }
            }

            @Override
            public void onError(String message) {
                if (view != null) {
                    view.showError(message);
                }
            }

            @Override
            public void onLoading() {
            }
        });

        model.getCategories(new HomeModel.Callback<List<Category>>() {
            @Override
            public void onSuccess(List<Category> result) {
                if (view != null) {
                    view.showCategories(result);
                }
            }

            @Override
            public void onError(String message) {
                if (view != null) {
                    view.showError(message);
                }
            }

            @Override
            public void onLoading(){};

        });

        model.getAreas(new HomeModel.Callback<List<Area>>() {
            @Override
            public void onSuccess(List<Area> result) {
                if (view != null) {
                    view.showAreas(result);
                }
            }

            @Override
            public void onError(String message) {
                if (view != null) {
                    view.showError(message);
                }
            }

            @Override
            public void onLoading() {
            }
        });
    }

}