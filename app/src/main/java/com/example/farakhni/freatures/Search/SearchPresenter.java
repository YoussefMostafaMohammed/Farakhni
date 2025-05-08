package com.example.farakhni.freatures.Search;

import androidx.lifecycle.LifecycleOwner;

import com.example.farakhni.data.network.NetworkCallBack;
import com.example.farakhni.model.Area;
import com.example.farakhni.model.Category;
import com.example.farakhni.model.FavoriteMeal;
import com.example.farakhni.model.Ingredient;
import com.example.farakhni.model.Meal;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SearchPresenter implements SearchContract.Presenter {
    private WeakReference<SearchContract.View> viewRef;
    private final SearchContract.Model model;
    private static final String FILTER_ALL = "all";
    private static final String FILTER_INGREDIENT = "ingredient";
    private static final String FILTER_CATEGORY = "category";
    private static final String FILTER_COUNTRY = "country";

    public SearchPresenter(SearchContract.Model model) {
        this.model = model;
        this.viewRef = new WeakReference<>(null);
    }

    @Override
    public void attachView(SearchContract.View view) {
        this.viewRef = new WeakReference<>(view);
    }

    @Override
    public void detachView() {
        viewRef.clear();
    }

    @Override
    public void initialize(LifecycleOwner owner) {
        SearchContract.View view = viewRef.get();
        if (view == null) return;

        view.observeFavoriteMeals(owner);
        view.setMealAdapter();
        model.getMealsByFirstLetter("a", new NetworkCallBack<List<Meal>>() {
            @Override
            public void onSuccessResult(List<Meal> result) {
                view.showMeals(result);
                view.hideProgressBar();
            }

            @Override
            public void onFailureResult(String failureMessage) {
                view.hideProgressBar();
                view.showError("Failed to load meals: " + failureMessage);
            }

            @Override
            public void onLoading() {
                view.showProgressBar();
            }

            @Override
            public void onNetworkError(String errorMessage) {
                view.hideProgressBar();
                view.showError("Network error: " + errorMessage);
            }

            @Override
            public void onEmptyData() {
                view.showMeals(new ArrayList<>());
                view.hideProgressBar();
                view.showMessage("No meals found");
            }

            @Override
            public void onProgress(int progress) {}
        });
    }

    @Override
    public void onSearchQuery(String query) {
        SearchContract.View view = viewRef.get();
        if (view == null) return;

        query = query.trim();
        if (query.isEmpty()) {
            view.showMeals(new ArrayList<>());
            return;
        }

        view.showProgressBar();
        model.getMealsByName(query, new NetworkCallBack<List<Meal>>() {
            @Override
            public void onSuccessResult(List<Meal> meals) {
                if (meals.isEmpty()) {
                    view.showMeals(new ArrayList<>());
                    view.hideProgressBar();
                    view.showMessage("No meals found");
                } else {
                    fetchFullMealsAndDisplay(meals);
                }
            }

            @Override
            public void onFailureResult(String message) {
                view.hideProgressBar();
                view.showError("Search failed: " + message);
            }

            @Override
            public void onLoading() {}

            @Override
            public void onNetworkError(String errorMessage) {
                view.hideProgressBar();
                view.showError("Network error: " + errorMessage);
            }

            @Override
            public void onEmptyData() {
                view.showMeals(new ArrayList<>());
                view.hideProgressBar();
                view.showMessage("No meals found");
            }

            @Override
            public void onProgress(int progress) {}
        });
    }

    @Override
    public void onFilterChanged(String filterType) {
        SearchContract.View view = viewRef.get();
        if (view == null) return;

        view.showProgressBar();
        switch (filterType) {
            case FILTER_ALL:
                view.setMealAdapter();
                model.getMealsByFirstLetter("a", new NetworkCallBack<List<Meal>>() {
                    @Override
                    public void onSuccessResult(List<Meal> result) {
                        view.showMeals(result);
                        view.hideProgressBar();
                    }

                    @Override
                    public void onFailureResult(String failureMessage) {
                        view.hideProgressBar();
                        view.showError("Failed to load meals: " + failureMessage);
                    }

                    @Override
                    public void onLoading() {}

                    @Override
                    public void onNetworkError(String errorMessage) {
                        view.hideProgressBar();
                        view.showError("Network error: " + errorMessage);
                    }

                    @Override
                    public void onEmptyData() {
                        view.showMeals(new ArrayList<>());
                        view.hideProgressBar();
                        view.showMessage("No meals found");
                    }

                    @Override
                    public void onProgress(int progress) {}
                });
                break;
            case FILTER_INGREDIENT:
                view.setIngredientAdapter();
                model.getAllIngredients(new NetworkCallBack<List<Ingredient>>() {
                    @Override
                    public void onSuccessResult(List<Ingredient> result) {
                        List<Ingredient> filtered = model.filterIngredients(result, "");
                        view.showIngredients(filtered);
                        view.hideProgressBar();
                    }

                    @Override
                    public void onFailureResult(String message) {
                        view.hideProgressBar();
                        view.showError("Failed to load ingredients");
                    }

                    @Override
                    public void onLoading() {}

                    @Override
                    public void onNetworkError(String errorMessage) {
                        view.hideProgressBar();
                        view.showError("Network error");
                    }

                    @Override
                    public void onEmptyData() {
                        view.showIngredients(new ArrayList<>());
                        view.hideProgressBar();
                        view.showMessage("No ingredients found");
                    }

                    @Override
                    public void onProgress(int progress) {}
                });
                break;
            case FILTER_CATEGORY:
                view.setCategoryAdapter();
                model.getAllCategories(new NetworkCallBack<List<Category>>() {
                    @Override
                    public void onSuccessResult(List<Category> result) {
                        List<Category> filtered = model.filterCategories(result, "");
                        view.showCategories(filtered);
                        view.hideProgressBar();
                    }

                    @Override
                    public void onFailureResult(String message) {
                        view.hideProgressBar();
                        view.showError("Failed to load categories");
                    }

                    @Override
                    public void onLoading() {}

                    @Override
                    public void onNetworkError(String errorMessage) {
                        view.hideProgressBar();
                        view.showError("Network error");
                    }

                    @Override
                    public void onEmptyData() {
                        view.showCategories(new ArrayList<>());
                        view.hideProgressBar();
                        view.showMessage("No categories found");
                    }

                    @Override
                    public void onProgress(int progress) {}
                });
                break;
            case FILTER_COUNTRY:
                view.setAreaAdapter();
                model.getAllAreas(new NetworkCallBack<List<Area>>() {
                    @Override
                    public void onSuccessResult(List<Area> result) {
                        List<Area> filtered = model.filterAreas(result, "");
                        view.showAreas(filtered);
                        view.hideProgressBar();
                    }

                    @Override
                    public void onFailureResult(String failureMessage) {
                        view.hideProgressBar();
                        view.showError("Failed to load areas");
                    }

                    @Override
                    public void onLoading() {}

                    @Override
                    public void onNetworkError(String errorMessage) {
                        view.hideProgressBar();
                        view.showError("Network error");
                    }

                    @Override
                    public void onEmptyData() {
                        view.showAreas(new ArrayList<>());
                        view.hideProgressBar();
                        view.showMessage("No areas found");
                    }

                    @Override
                    public void onProgress(int progress) {}
                });
                break;
            default:
                view.hideProgressBar();
                view.showError("Please select a filter");
                break;
        }
    }

    @Override
    public void onFavoriteToggled(Meal meal) {
        SearchContract.View view = viewRef.get();
        if (view == null) return;

        FavoriteMeal fav = new FavoriteMeal(meal);
        if (meal.isFavorite()) {
            model.insertFavoriteMeal(fav);
            view.showMessage(meal.getName() + " added to favorites");
        } else {
            model.deleteFavoriteMeal(fav);
            view.showMessage(meal.getName() + " removed from favorites");
        }
    }

    @Override
    public void onMealClicked(Meal meal) {
        SearchContract.View view = viewRef.get();
        if (view == null) return;

        try {
            view.navigateToMealDetails(new FavoriteMeal(meal));
        } catch (IllegalArgumentException e) {
            view.showError("Navigation error: " + e.getMessage());
        }
    }

    @Override
    public void observeFavoriteMeals(LifecycleOwner owner, SearchContract.View view) {
        model.getFavoriteMeals().observe(owner, favoriteMeals -> {
            if (view != null) {
                ((SearchFragment) view).getMealAdapter().setFavoriteMeals(favoriteMeals);
            }
        });
    }

    private void fetchFullMealsAndDisplay(List<Meal> simpleMeals) {
        SearchContract.View view = viewRef.get();
        if (view == null) return;

        List<Meal> fullMeals = new ArrayList<>();
        int total = simpleMeals.size();
        AtomicInteger counter = new AtomicInteger(0);
        for (Meal m : simpleMeals) {
            model.getMealById(m.getId(), new NetworkCallBack<List<Meal>>() {
                @Override
                public void onSuccessResult(List<Meal> result) {
                    if (!result.isEmpty()) {
                        fullMeals.add(result.get(0));
                    }
                    if (counter.incrementAndGet() == total) {
                        view.showMeals(fullMeals);
                        view.hideProgressBar();
                    }
                }

                @Override
                public void onFailureResult(String message) {
                    if (counter.incrementAndGet() == total) {
                        view.showMeals(fullMeals);
                        view.hideProgressBar();
                    }
                }

                @Override
                public void onLoading() {}

                @Override
                public void onNetworkError(String errorMessage) {
                    if (counter.incrementAndGet() == total) {
                        view.showMeals(fullMeals);
                        view.hideProgressBar();
                    }
                }

                @Override
                public void onEmptyData() {
                    if (counter.incrementAndGet() == total) {
                        view.showMeals(fullMeals);
                        view.hideProgressBar();
                    }
                }

                @Override
                public void onProgress(int progress) {}
            });
        }
    }
}