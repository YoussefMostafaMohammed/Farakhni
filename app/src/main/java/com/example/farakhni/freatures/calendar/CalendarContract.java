package com.example.farakhni.freatures.calendar;

import androidx.lifecycle.LifecycleOwner;

import com.example.farakhni.model.Meal;

import java.util.List;

public interface CalendarContract {
    interface View {
        void showMeals(List<Meal> meals);
        void showError(String message);
        void showMessage(String message);
    }

    interface Presenter {
        void attachView(View view);
        void detachView();
        void setupRecyclerView(androidx.recyclerview.widget.RecyclerView recyclerView, android.content.Context context, androidx.lifecycle.LifecycleOwner owner);
        void updateRecyclerView(List<Meal> meals);
        void onDateSelected(String date, androidx.lifecycle.LifecycleOwner owner);
        void onDeleteMeal(Meal meal, String date);
        void onAddMeal(Meal meal, String date, LifecycleOwner owner);
        void cancelOperations();
    }
}