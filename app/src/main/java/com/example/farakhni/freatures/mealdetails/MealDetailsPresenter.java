package com.example.farakhni.freatures.mealdetails;

import androidx.lifecycle.LifecycleOwner;

import com.example.farakhni.model.FavoriteMeal;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MealDetailsPresenter implements MealDetailsContract.Presenter {
    private WeakReference<MealDetailsContract.View> viewRef;
    private final MealDetailsContract.Model model;
    private FavoriteMeal meal;
    private String userId;

    public MealDetailsPresenter(MealDetailsContract.Model model, String userId) {
        this.model = model;
        this.userId = userId;
        this.viewRef = new WeakReference<>(null);
    }

    @Override
    public void attachView(MealDetailsContract.View view) {
        this.viewRef = new WeakReference<>(view);
    }

    @Override
    public void detachView() {
        viewRef.clear();
    }

    @Override
    public void initialize(FavoriteMeal meal, LifecycleOwner owner) {
        this.meal = meal;
        MealDetailsContract.View view = viewRef.get();
        if (view == null) return;

        if (meal == null) {
            view.showError("Failed to load meal details.");
            view.navigateBack();
            return;
        }

        view.showMealDetails(
                meal.getName() != null ? meal.getName() : "Unknown Meal",
                meal.getInstructions() != null ? meal.getInstructions() : "No instructions available",
                meal.getMealThumb()
        );
        view.showIngredients(meal.getIngredientsList());

        String videoUrl = meal.getYoutube();
        if (videoUrl != null && !videoUrl.isEmpty() && videoUrl.contains("watch?v=")) {
            videoUrl = videoUrl.replace("watch?v=", "embed/");
            view.showVideo(videoUrl);
        } else {
            view.hideVideo();
        }

        boolean isFavorite = model.isFavorite(meal.getId());
        view.setFavoriteStatus(isFavorite);

        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        boolean isScheduled = model.isMealPlanned(meal.getId(), today);
        view.setScheduleStatus(isScheduled);
    }

    @Override
    public void onFavoriteClicked() {
        MealDetailsContract.View view = viewRef.get();
        if (view == null || meal == null) return;

        if (userId == null) {
            view.showMessage("Please log in to favorite meals");
            return;
        }

        boolean isFavorite = model.isFavorite(meal.getId());
        if (isFavorite) {
            model.deleteFavoriteMeal(meal);
            view.setFavoriteStatus(false);
            view.showMessage(meal.getName() + " removed from favorites");
        } else {
            model.insertFavoriteMeal(meal);
            view.setFavoriteStatus(true);
            view.showMessage(meal.getName() + " added to favorites");
        }
    }

    @Override
    public void onPlanMealClicked() {
        MealDetailsContract.View view = viewRef.get();
        if (view == null) return;

        if (userId == null) {
            view.showMessage("Please log in to plan meals");
            return;
        }

        view.showDatePicker();
    }

    @Override
    public void onDeleteMealClicked() {
        MealDetailsContract.View view = viewRef.get();
        if (view == null || meal == null) return;

        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        model.deletePlannedMeal(meal.getId(), meal.getName(), today);
        view.setScheduleStatus(false);
        view.showMessage(meal.getName() + " removed from plan");
    }

    @Override
    public void onDateSelected(String date) {
        MealDetailsContract.View view = viewRef.get();
        if (view == null || meal == null) return;

        if (model.isMealPlanned(meal.getId(), date)) {
            view.showMessage(meal.getName() + " Already Planned for " + date);
        } else {
            model.insertPlannedMeal(meal.getId(), meal.getName(), date);
            view.showMessage(meal.getName() + " planned for " + date);
            String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            if (date.equals(today)) {
                view.setScheduleStatus(true);
            }
        }
    }
}