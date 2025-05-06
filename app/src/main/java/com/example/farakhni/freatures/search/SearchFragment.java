package com.example.farakhni.freatures.search;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.farakhni.R;
import com.example.farakhni.common.MealAdapter;
import com.example.farakhni.data.repositories.MealRepositoryImpl;
import com.example.farakhni.databinding.FragmentSearchBinding;
import com.example.farakhni.model.FavoriteMeal;
import com.example.farakhni.model.Meal;
import com.example.farakhni.data.network.NetworkCallBack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private MealAdapter mealAdapter;
    private MealRepositoryImpl mealRepo;

    private static final String FILTER_ALL = "all";
    private static final String FILTER_INGREDIENT = "ingredient";
    private static final String FILTER_AREA = "area";
    private static final String FILTER_COUNTRY = "country";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize repository and adapter
        mealRepo = MealRepositoryImpl.getInstance(requireContext());
        mealAdapter = new MealAdapter(requireContext(), new ArrayList<>());
        binding.searchResults.setAdapter(mealAdapter);
        binding.searchResults.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Observe favorite meals
        mealRepo.getFavoriteMeals().observe(getViewLifecycleOwner(), mealAdapter::setFavoriteMeals);

        // Handle favorite toggle
        mealAdapter.setOnFavoriteToggleListener(meal -> {
            FavoriteMeal fav = new FavoriteMeal(meal);
            if (meal.isFavorite()) {
                mealRepo.insertFavoriteMeal(fav);
            } else {
                mealRepo.deleteFavoriteMeal(fav);
            }
            Toast.makeText(requireContext(),
                    meal.getName() + (meal.isFavorite() ? " added to favorites" : " removed from favorites"),
                    Toast.LENGTH_SHORT).show();
        });

        // Handle meal click navigation
        mealAdapter.setOnMealClickListener(meal -> {
            Bundle args = new Bundle();
            args.putSerializable("arg_meal", new FavoriteMeal(meal));
            NavController nav = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_app_screen);
            nav.navigate(R.id.nav_meal_details, args);
        });

        // Handle search input
            binding.searchInput.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    Log.e("search", "Message");

                    return true;
                }
                return false;
            });

        return root;
    }

    private void performSearch() {
        String term = Objects.requireNonNull(binding.searchInput.getText()).toString().trim();
        if (term.isEmpty()) {
            Toast.makeText(getContext(), "Please enter a search term", Toast.LENGTH_SHORT).show();
            return;
        }
        int checkedId = binding.filterGroup.getCheckedChipId();
        String filterType;
        if (checkedId == R.id.filterAll) {
            filterType = FILTER_ALL;
        } else if (checkedId == R.id.filterIngredient) {
            filterType = FILTER_INGREDIENT;
        } else if (checkedId == R.id.filterArea) {
            filterType = FILTER_AREA;
        } else if (checkedId == R.id.filterCountry) {
            filterType = FILTER_COUNTRY;
        } else {
            Toast.makeText(getContext(), "Please select a filter", Toast.LENGTH_SHORT).show();
            return;
        }
        binding.progressBar.setVisibility(View.VISIBLE);
        NetworkCallBack<List<Meal>> callback = new NetworkCallBack<List<Meal>>() {
            @Override
            public void onSuccessResult(List<Meal> meals) {
                if (meals.isEmpty()) {
                    mealAdapter.setMealList(new ArrayList<>());
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "No meals found", Toast.LENGTH_SHORT).show();
                } else {
                    fetchFullMealsAndDisplay(meals);
                }
            }

            @Override
            public void onFailureResult(String message) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Search failed: " + message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoading() {}

            @Override
            public void onNetworkError(String errorMessage) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Network error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEmptyData() {
                mealAdapter.setMealList(new ArrayList<>());
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "No meals found", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProgress(int progress) {}
        };

        switch (filterType) {
            case FILTER_ALL:
                mealRepo.searchMealsByName(term, callback);
                break;
            case FILTER_INGREDIENT:
                mealRepo.filterByIngredient(term, callback);
                break;
            case FILTER_AREA:
            case FILTER_COUNTRY:
                mealRepo.filterByArea(term, callback);
                break;
        }
    }

    private void fetchFullMealsAndDisplay(List<Meal> simpleMeals) {
        List<Meal> fullMeals = new ArrayList<>();
        int total = simpleMeals.size();
        AtomicInteger counter = new AtomicInteger(0);
        for (Meal m : simpleMeals) {
            mealRepo.getMealById(m.getId(), new NetworkCallBack<List<Meal>>() {
                @Override
                public void onSuccessResult(List<Meal> result) {
                    if (!result.isEmpty()) {
                        fullMeals.add(result.get(0));
                    }
                    if (counter.incrementAndGet() == total) {
                        mealAdapter.setMealList(fullMeals);
                        binding.progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailureResult(String message) {
                    if (counter.incrementAndGet() == total) {
                        mealAdapter.setMealList(fullMeals);
                        binding.progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onLoading() {}

                @Override
                public void onNetworkError(String errorMessage) {
                    if (counter.incrementAndGet() == total) {
                        mealAdapter.setMealList(fullMeals);
                        binding.progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onEmptyData() {
                    if (counter.incrementAndGet() == total) {
                        mealAdapter.setMealList(fullMeals);
                        binding.progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onProgress(int progress) {}
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}