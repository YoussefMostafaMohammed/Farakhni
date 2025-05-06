package com.example.farakhni.freatures.calendar;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.farakhni.R;
import com.example.farakhni.common.MealAdapter;
import com.example.farakhni.data.repositories.MealRepositoryImpl;
import com.example.farakhni.databinding.FragmentSlideshowBinding;
import com.example.farakhni.model.FavoriteMeal;
import com.example.farakhni.model.Meal;
import com.example.farakhni.model.PlannedMeal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SlideshowFragment extends Fragment {
    private FragmentSlideshowBinding binding;
    private MealAdapter mealAdapter;
    private MealRepositoryImpl mealRepo;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSlideshowBinding.inflate(inflater, container, false);

        mealAdapter = new MealAdapter(requireContext(), new ArrayList<>());
        mealRepo = MealRepositoryImpl.getInstance(requireContext());

        binding.mealsRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.mealsRecycler.setAdapter(mealAdapter);

        mealAdapter.setOnFavoriteToggleListener(meal -> {
            FavoriteMeal fav = new FavoriteMeal(meal);
            if (meal.isFavorite()) {
                mealRepo.insertFavoriteMeal(fav);
            } else {
                mealRepo.deleteFavoriteMeal(fav);
            }
            Toast.makeText(requireContext(),
                    meal.getName() + (meal.isFavorite() ? " added" : " removed"),
                    Toast.LENGTH_SHORT).show();
        });

        mealAdapter.setOnMealClickListener(meal -> {
            if (requireContext() instanceof Activity) {
                Bundle args = new Bundle();
                args.putSerializable("arg_meal", new FavoriteMeal(meal));
                NavController nav = Navigation.findNavController(
                        (Activity) requireContext(),
                        R.id.nav_host_fragment_content_app_screen);
                nav.navigate(R.id.nav_meal_details, args);
            }
        });

        mealRepo.getFavoriteMeals().observe(getViewLifecycleOwner(), mealAdapter::setFavoriteMeals);

        binding.calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String selectedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, dayOfMonth);
            loadPlannedMealsForDate(selectedDate);
        });

        String today = sdf.format(new Date());
        loadPlannedMealsForDate(today);

        return binding.getRoot();
    }

    private void loadPlannedMealsForDate(String date) {
        List<Meal> mealList = new ArrayList<>();
        mealRepo.getPlannedMealsForDate(date).observe(getViewLifecycleOwner(), plannedMeals -> {
            if (!plannedMeals.isEmpty()) {
                for (PlannedMeal plannedMeal : plannedMeals) {
                            mealList.add(new Meal(plannedMeal));
                }
                mealAdapter.setMealList(mealList);
            } else {
                mealAdapter.setMealList(new ArrayList<>());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}