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
    private String currentFilter = FILTER_ALL; // Track the active filter
    private List<Ingredient> cachedIngredients = new ArrayList<>(); // Cache for ingredients
    private List<Area> cachedAreas = new ArrayList<>(); // Cache for areas
    private List<Category> cachedCategories = new ArrayList<>(); // Cache for categories
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
        cachedIngredients.clear();
        cachedAreas.clear();
        cachedCategories.clear();
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
            if (currentFilter.equals(FILTER_INGREDIENT)) {
                view.showIngredients(cachedIngredients);
            } else if (currentFilter.equals(FILTER_CATEGORY)) {
                view.showCategories(cachedCategories);
            } else if (currentFilter.equals(FILTER_COUNTRY)) {
                view.showAreas(cachedAreas);
            } else if (currentFilter.equals(FILTER_ALL)) {
                view.showMeals(new ArrayList<>());
            }
            view.hideProgressBar();
            return;
        }

        view.showProgressBar();
        if (currentFilter.equals(FILTER_INGREDIENT)) {
            if (!cachedIngredients.isEmpty()) {
                List<Ingredient> filtered = model.filterIngredients(cachedIngredients, query);
                view.showIngredients(filtered);
                view.hideProgressBar();
                if (filtered.isEmpty()) {
                    view.showMessage("No ingredients found");
                }
            } else {
                String finalQuery2 = query;
                model.getAllIngredients(new NetworkCallBack<List<Ingredient>>() {
                    @Override
                    public void onSuccessResult(List<Ingredient> result) {
                        cachedIngredients = result;
                        List<Ingredient> filtered = model.filterIngredients(result, finalQuery2);
                        view.showIngredients(filtered);
                        view.hideProgressBar();
                        if (filtered.isEmpty()) {
                            view.showMessage("No ingredients found");
                        }
                    }

                    @Override
                    public void onFailureResult(String message) {
                        view.hideProgressBar();
                        view.showError("Failed to load ingredients: " + message);
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
                        view.showIngredients(new ArrayList<>());
                        view.hideProgressBar();
                        view.showMessage("No ingredients found");
                    }

                    @Override
                    public void onProgress(int progress) {}
                });
            }
        } else if (currentFilter.equals(FILTER_CATEGORY)) {
            if (!cachedCategories.isEmpty()) {
                List<Category> filtered = model.filterCategories(cachedCategories, query);
                view.showCategories(filtered);
                view.hideProgressBar();
                if (filtered.isEmpty()) {
                    view.showMessage("No categories found");
                }
            } else {
                String finalQuery1 = query;
                model.getAllCategories(new NetworkCallBack<List<Category>>() {
                    @Override
                    public void onSuccessResult(List<Category> result) {
                        cachedCategories = result;
                        List<Category> filtered = model.filterCategories(result, finalQuery1);
                        view.showCategories(filtered);
                        view.hideProgressBar();
                        if (filtered.isEmpty()) {
                            view.showMessage("No categories found");
                        }
                    }

                    @Override
                    public void onFailureResult(String message) {
                        view.hideProgressBar();
                        view.showError("Failed to load categories: " + message);
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
                        view.showCategories(new ArrayList<>());
                        view.hideProgressBar();
                        view.showMessage("No categories found");
                    }

                    @Override
                    public void onProgress(int progress) {}
                });
            }
        } else if (currentFilter.equals(FILTER_COUNTRY)) {
            if (!cachedAreas.isEmpty()) {
                List<Area> filtered = model.filterAreas(cachedAreas, query);
                view.showAreas(filtered);
                view.hideProgressBar();
                if (filtered.isEmpty()) {
                    view.showMessage("No areas found");
                }
            } else {
                String finalQuery = query;
                model.getAllAreas(new NetworkCallBack<List<Area>>() {
                    @Override
                    public void onSuccessResult(List<Area> result) {
                        cachedAreas = result;
                        List<Area> filtered = model.filterAreas(result, finalQuery);
                        view.showAreas(filtered);
                        view.hideProgressBar();
                        if (filtered.isEmpty()) {
                            view.showMessage("No areas found");
                        }
                    }

                    @Override
                    public void onFailureResult(String message) {
                        view.hideProgressBar();
                        view.showError("Failed to load areas: " + message);
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
                        view.showAreas(new ArrayList<>());
                        view.hideProgressBar();
                        view.showMessage("No areas found");
                    }

                    @Override
                    public void onProgress(int progress) {}
                });
            }
        } else if (currentFilter.equals(FILTER_ALL)) {
            model.getMealsByName(query, new NetworkCallBack<List<Meal>>() {
                @Override
                public void onSuccessResult(List<Meal> meals) {
                    if (meals != null) {
                        if (meals.isEmpty()) {
                            view.showMeals(new ArrayList<>());
                            view.hideProgressBar();
                            view.showMessage("Can't find meal");
                        } else {
                            fetchFullMealsAndDisplay(meals);
                        }
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
                    view.showMessage("Can't find meal");
                }

                @Override
                public void onProgress(int progress) {}
            });
        }
    }

    @Override
    public void onFilterChanged(String filterType) {
        SearchContract.View view = viewRef.get();
        if (view == null) return;

        currentFilter = filterType; // Update the current filter
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
                cachedIngredients.clear(); // Clear cache to refresh
                model.getAllIngredients(new NetworkCallBack<List<Ingredient>>() {
                    @Override
                    public void onSuccessResult(List<Ingredient> result) {
                        cachedIngredients = result;
                        view.showIngredients(result);
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
                cachedCategories.clear(); // Clear cache to refresh
                model.getAllCategories(new NetworkCallBack<List<Category>>() {
                    @Override
                    public void onSuccessResult(List<Category> result) {
                        cachedCategories = result;
                        view.showCategories(result);
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
                cachedAreas.clear(); // Clear cache to refresh
                model.getAllAreas(new NetworkCallBack<List<Area>>() {
                    @Override
                    public void onSuccessResult(List<Area> result) {
                        cachedAreas = result;
                        view.showAreas(result);
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
                    if (result != null && !result.isEmpty()) {
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