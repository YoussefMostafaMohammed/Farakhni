package com.example.farakhni.freatures.FilterBy;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.farakhni.R;
import com.example.farakhni.common.MealAdapter;
import com.example.farakhni.data.network.NetworkCallBack;
import com.example.farakhni.data.repositories.MealRepository;
import com.example.farakhni.data.repositories.MealRepositoryImpl;
import com.example.farakhni.databinding.FragmentMealsBinding;
import com.example.farakhni.model.FavoriteMeal;
import com.example.farakhni.model.Meal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilterByFragment extends Fragment implements FilterByContract.View {
    private static final String ARG_MEALS = "meals";
    private FragmentMealsBinding binding;
    private MealAdapter adapter;
    private FilterByContract.Presenter presenter;
    private MealRepositoryImpl mealRepository;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMealsBinding.inflate(inflater, container, false);
        presenter = new FilterByPresenter(new FilterByModel(requireContext()));
        presenter.attachView(this);

        Meal[] mealsArray = (Meal[]) requireArguments().getSerializable(ARG_MEALS);
        List<Meal> mealsList = Arrays.asList(mealsArray != null ? mealsArray : new Meal[0]);
        mealRepository = MealRepositoryImpl.getInstance(requireContext());
        setupRecycler();
        presenter.loadMeals(mealsList);
        return binding.getRoot();
    }

    private void setupRecycler() {
        adapter = new MealAdapter(requireContext(), new ArrayList<>());
        adapter.setOnFavoriteToggleListener(meal -> {
            presenter.onFavoriteToggled(meal);
            Toast.makeText(requireContext(),
                    meal.getName() + (meal.isFavorite() ? " added" : " removed"),
                    Toast.LENGTH_SHORT).show();
            Log.d("FilterByFragment", "Favorite toggled: " + meal.getName());
        });

        adapter.setOnMealClickListener(meal -> {
            if (requireContext() instanceof Activity) {
                mealRepository.getMealById(meal.getId(), new NetworkCallBack<List<Meal>>() {
                    @Override
                    public void onSuccessResult(List<Meal> result) {
                        Bundle args = new Bundle();
                        args.putSerializable("arg_meal", new FavoriteMeal(result.get(0)));
                        NavController nav = Navigation.findNavController(
                                (Activity) requireContext(),
                                R.id.nav_host_fragment_content_app_screen);
                        nav.navigate(R.id.nav_meal_details, args);
                    }

                    @Override
                    public void onFailureResult(String failureMessage) {

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
        });

        binding.mealsList.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        binding.mealsList.setAdapter(adapter);
    }

    @Override
    public void showMeals(List<Meal> meals) {
        adapter.setMealList(meals);
        mealRepository.getFavoriteMeals().observe(getViewLifecycleOwner(), new Observer<List<FavoriteMeal>>() {
            @Override
            public void onChanged(List<FavoriteMeal> favoriteMeals) {
                adapter.setFavoriteMeals(favoriteMeals);
            }
        });
    }

    @Override
    public void showError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        binding = null;
        super.onDestroyView();
    }
}
