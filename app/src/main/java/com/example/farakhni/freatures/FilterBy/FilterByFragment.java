// com/example/farakhni/freatures/FilterBy/FilterByFragment.java
package com.example.farakhni.freatures.FilterBy;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.farakhni.R;
import com.example.farakhni.common.MealAdapter;
import com.example.farakhni.databinding.FragmentMealsBinding;
import com.example.farakhni.model.Meal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilterByFragment extends Fragment implements FilterByContract.View {
    private static final String ARG_MEALS = "meals";

    private FragmentMealsBinding binding;
    private MealAdapter adapter;
    private FilterByContract.Presenter presenter;

    public static FilterByFragment newInstance(@Nullable Meal[] meals) {
        FilterByFragment f = new FilterByFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MEALS, meals);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMealsBinding.inflate(inflater, container, false);

        // 1) MVP wiring
        presenter = new FilterByPresenter(new FilterByModel(requireContext()));
        presenter.attachView(this);

        // 2) Recycler setup
        adapter = new MealAdapter(requireContext(), new ArrayList<>());
        adapter.setOnFavoriteToggleListener(meal -> {
            presenter.onFavoriteToggled(meal);
            Toast.makeText(requireContext(),
                    meal.getName() + (meal.isFavorite() ? " added" : " removed"),
                    Toast.LENGTH_SHORT).show();
        });
        adapter.setOnMealClickListener(meal -> {
            Bundle args = new Bundle();
            args.putSerializable("arg_meal", meal);
            NavController nav = Navigation.findNavController(
                    (Activity) requireContext(),
                    R.id.nav_host_fragment_content_app_screen
            );
            nav.navigate(R.id.nav_meal_details, args);
        });

        binding.mealsList.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );
        binding.mealsList.setAdapter(adapter);

        // 3) Load initial data
        Meal[] mealsArray = null;
        if (getArguments() != null) {
            mealsArray = (Meal[]) getArguments().getSerializable(ARG_MEALS);
        }
        presenter.loadMeals(mealsArray);

        return binding.getRoot();
    }

    @Override
    public void showMeals(List<Meal> meals) {
        adapter.setMealList(meals);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(requireContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        binding = null;
        super.onDestroyView();
    }
}
