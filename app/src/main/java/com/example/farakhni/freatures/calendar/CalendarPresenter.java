package com.example.farakhni.freatures.calendar;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farakhni.R;
import com.example.farakhni.model.FavoriteMeal;
import com.example.farakhni.model.Meal;
import com.example.farakhni.model.PlannedMeal;
import com.example.farakhni.utils.MealAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class CalendarPresenter implements CalendarContract.Presenter {
    private WeakReference<CalendarContract.View> viewRef;
    private final CalendarModel model;
    private String currentDate;
    private MealAdapter adapter;
    private Observer<List<PlannedMeal>> mealsObserver;

    public CalendarPresenter(CalendarModel model) {
        this.model = model;
        this.viewRef = new WeakReference<>(null);
    }

    @Override
    public void attachView(CalendarContract.View view) {
        this.viewRef = new WeakReference<>(view);
    }

    @Override
    public void detachView() {
        viewRef.clear();
    }

    @Override
    public void setupRecyclerView(RecyclerView recyclerView, Context context, LifecycleOwner owner) {
        adapter = new MealAdapter(context, new ArrayList<>(), true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
        adapter.setOnCalendarClickListener(new MealAdapter.OnCalendarClickListener() {
            @Override
            public void onCalendarIconAddClicked(Meal meal, long dateEpochMillis) {
                onAddMeal(meal, currentDate, owner);
            }

            @Override
            public void onCalendarIconDeleteClicked(Meal meal) {
                onDeleteMeal(meal, currentDate);
            }
        });


        adapter.setOnMealClickListener(meal -> {
            if (context instanceof Activity) {
                Bundle args = new Bundle();
                args.putSerializable("arg_meal", new FavoriteMeal(meal));
                NavController nav = Navigation.findNavController(
                        (Activity) context,
                        R.id.nav_host_fragment_content_app_screen);
                nav.navigate(R.id.nav_meal_details, args);
            }
        });

        adapter.setOnCalendarClickListener(new MealAdapter.OnCalendarClickListener() {
            @Override
            public void onCalendarIconAddClicked(Meal meal, long dateEpochMillis) {
                onAddMeal(meal, currentDate, owner);
            }

            @Override
            public void onCalendarIconDeleteClicked(Meal meal) {
                onDeleteMeal(meal, currentDate);
            }
        });
    }

    @Override
    public void updateRecyclerView(List<Meal> meals) {
        if (adapter != null) {
            adapter.setMealList(new ArrayList<>()); // Clear existing list
            adapter.setMealList(meals);
            adapter.notifyDataSetChanged();
            android.util.Log.d("CalendarPresenter", "Updated RecyclerView with " + meals.size() + " meals");
        }
    }

    @Override
    public void onDateSelected(String date, LifecycleOwner owner) {
        currentDate = date;
        if (mealsObserver != null) {
            model.getPlannedMealsForDate(date).removeObserver(mealsObserver);
        }
        mealsObserver = plannedMeals -> {
            List<Meal> meals = new ArrayList<>();
            if (plannedMeals != null) {
                android.util.Log.d("CalendarPresenter", "Received " + plannedMeals.size() + " planned meals for " + date);
                for (PlannedMeal plannedMeal : plannedMeals) {
                    try {
                        meals.add(new Meal(plannedMeal));
                    } catch (Exception e) {
                        android.util.Log.e("CalendarPresenter", "Failed to convert PlannedMeal to Meal: " + e.getMessage());
                    }
                }
            } else {
                android.util.Log.w("CalendarPresenter", "No planned meals received for " + date);
            }
            CalendarContract.View view = viewRef.get();
            if (view != null) {
                view.showMeals(meals);
            }
        };
        model.getPlannedMealsForDate(date).observe(owner, mealsObserver);
    }

    @Override
    public void onDeleteMeal(Meal meal, String date) {
        model.deletePlannedMeal(meal, date);
        showMessage(meal.getName() + " removed from " + date);
        onDateSelected(date, (LifecycleOwner) viewRef.get());
    }

    @Override
    public void onAddMeal(Meal meal, String date, LifecycleOwner owner) {
        LiveData<List<PlannedMeal>> existingMeals = model.getPlannedMealsForDate(date);
        existingMeals.observe(owner, plannedMeals -> {
            if (plannedMeals != null) {
                for (PlannedMeal pm : plannedMeals) {
                    if (pm.getMealId().equals(meal.getId())) {
                        showError("Meal " + meal.getName() + " is already planned for " + date);
                        return;
                    }
                }
            }
            model.addPlannedMeal(meal,date);
            android.util.Log.d("CalendarPresenter", "Added meal " + meal.getName() + " for " + date);
            showMessage("Added " + meal.getName() + " to " + date);
            onDateSelected(date, owner);
        });
    }

    @Override
    public void cancelOperations() {
        if (mealsObserver != null && currentDate != null) {
            model.getPlannedMealsForDate(currentDate).removeObserver(mealsObserver);
            mealsObserver = null;
        }
    }

    private void showError(String message) {
        CalendarContract.View view = viewRef.get();
        if (view != null && view instanceof Fragment) {
            view.showError(message);
            Toast.makeText(((Fragment) view).requireContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    private void showMessage(String message) {
        CalendarContract.View view = viewRef.get();
        if (view != null && view instanceof Fragment) {
            view.showMessage(message);
            Toast.makeText(((Fragment) view).requireContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}