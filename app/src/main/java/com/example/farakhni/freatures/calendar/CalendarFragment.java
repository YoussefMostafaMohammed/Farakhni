package com.example.farakhni.freatures.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.farakhni.databinding.FragmentCalendarBinding;
import com.example.farakhni.model.Meal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarFragment extends Fragment implements CalendarContract.View {
    private FragmentCalendarBinding binding;
    private CalendarContract.Presenter presenter;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private String selectedDate;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCalendarBinding.inflate(inflater, container, false);

        presenter = new CalendarPresenter(new CalendarModel(
                com.example.farakhni.data.repositories.MealRepositoryImpl.getInstance(requireContext())
        ));
        presenter.attachView(this);

        selectedDate = sdf.format(new Date());
        binding.calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, dayOfMonth);
            presenter.onDateSelected(selectedDate, getViewLifecycleOwner());
        });

        presenter.setupRecyclerView(binding.mealsRecycler, requireContext(), getViewLifecycleOwner());
        presenter.onDateSelected(selectedDate, getViewLifecycleOwner());

        return binding.getRoot();
    }

    @Override
    public void showMeals(List<Meal> meals) {
        android.util.Log.d("CalendarFragment", "Received " + meals.size() + " meals for display");
        if (meals.isEmpty()) {
            showError("No meals found for the selected date");
        }
        presenter.updateRecyclerView(meals);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
        presenter.cancelOperations();
        binding = null;
    }
}