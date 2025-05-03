package com.example.farakhni.freatures.home;

import com.example.farakhni.data.network.NetworkCallBack;
import com.example.farakhni.model.Area;
import com.example.farakhni.model.Category;
import com.example.farakhni.model.Ingredient;
import com.example.farakhni.model.Meal;

import java.util.List;

public class HomePresenter implements HomeContract.Presenter {
    private HomeContract.View view;
    private final HomeContract.Model model;

    public HomePresenter(HomeContract.Model model) {
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

        model.getAllAreas(new NetworkCallBack<List<Area>>() {
            @Override
            public void onSuccessResult(List<Area> result) {
                if(view!=null) view.showAreas(result);
            }

            @Override
            public void onFailureResult(String failureMessage) {
                if (view != null) view.showError(failureMessage);
            }

            @Override
            public void onLoading() {

            }

            @Override
            public void onNetworkError(String errorMessage) {

            }

            @Override
            public void onEmptyData() {

            }

            @Override
            public void onProgress(int progress) {

            }
        });

        model.getRandomMeal(new NetworkCallBack<List<Meal>>() {
            @Override
            public void onSuccessResult(List<Meal> result) {
                if (view != null) view.showRandomMeals(result);
            }
            @Override
            public void onFailureResult(String errorMessage) {
                if (view != null) view.showError(errorMessage);
            }

            @Override
            public void onLoading() {

            }

            @Override
            public void onNetworkError(String errorMessage) {

            }

            @Override
            public void onEmptyData() {

            }

            @Override
            public void onProgress(int progress) {

            }
        });
        model.getAllIngredients(new NetworkCallBack<List<Ingredient>>() {
            @Override
            public void onSuccessResult(List<Ingredient> result) {
                if (view != null) view.showIngredients(result);
            }
            @Override
            public void onFailureResult(String errorMessage) {
                if (view != null) view.showError(errorMessage);
            }

            @Override
            public void onLoading() {

            }

            @Override
            public void onNetworkError(String errorMessage) {

            }

            @Override
            public void onEmptyData() {

            }

            @Override
            public void onProgress(int progress) {

            }
        });

        model.getAllCategories(new NetworkCallBack<List<Category>>() {
            @Override
            public void onSuccessResult(List<Category> result) {
                if (view != null) view.showCategories(result);
            }
            @Override
            public void onFailureResult(String errorMessage) {
                if (view != null) view.showError(errorMessage);
            }

            @Override
            public void onLoading() {

            }

            @Override
            public void onNetworkError(String errorMessage) {

            }

            @Override
            public void onEmptyData() {

            }

            @Override
            public void onProgress(int progress) {

            }
        });
    }
}

