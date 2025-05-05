package com.example.farakhni.freatures.home;

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
import com.example.farakhni.common.AreaAdapter;
import com.example.farakhni.common.CategoryAdapter;
import com.example.farakhni.common.IngredientAdapter;
import com.example.farakhni.common.MealAdapter;
import com.example.farakhni.data.repositories.AreaRepositoryImpl;
import com.example.farakhni.data.repositories.CategoryRepositoryImpl;
import com.example.farakhni.data.repositories.IngredientRepositoryImpl;
import com.example.farakhni.data.repositories.MealRepositoryImpl;
import com.example.farakhni.databinding.FragmentHomeBinding;
import com.example.farakhni.model.Area;
import com.example.farakhni.model.Category;
import com.example.farakhni.model.FavoriteMeal;
import com.example.farakhni.model.Ingredient;
import com.example.farakhni.model.Meal;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements HomeContract.View {
    private FragmentHomeBinding binding;
    private HomeContract.Presenter presenter;
    private MealAdapter mealAdapter;
    private IngredientAdapter ingredientAdapter;
    private CategoryAdapter categoryAdapter;
    private AreaAdapter areaAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        // Initialize adapters
        mealAdapter = new MealAdapter(requireContext(), new ArrayList<>());
        ingredientAdapter = new IngredientAdapter(requireContext(), new ArrayList<>());
        categoryAdapter = new CategoryAdapter(requireContext(), new ArrayList<>());
        areaAdapter = new AreaAdapter(requireContext(), new ArrayList<>());

        MealRepositoryImpl mealRepo = MealRepositoryImpl.getInstance(requireContext());

        // Handle favorite toggle from adapter
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

        // Handle meal click navigation
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

        // Setup RecyclerViews
        binding.randomMealRecyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.randomMealRecyclerView.setAdapter(mealAdapter);

        binding.ingredientsList.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.ingredientsList.setAdapter(ingredientAdapter);

        binding.categoriesList.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.categoriesList.setAdapter(categoryAdapter);

        binding.areasList.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.areasList.setAdapter(areaAdapter);

        // Observe favorites and update all adapters
        mealRepo.getFavoriteMeals().observe(getViewLifecycleOwner(), favMeals -> {
            mealAdapter.setFavoriteMeals(favMeals);
            ingredientAdapter.setFavoriteMeals(favMeals);
            categoryAdapter.setFavoriteMeals(favMeals);
            areaAdapter.setFavoriteMeals(favMeals);
        });

        // Load data through presenter
        presenter = new HomePresenter(new HomeModel(
                mealRepo,
                IngredientRepositoryImpl.getInstance(),
                CategoryRepositoryImpl.getInstance(),
                AreaRepositoryImpl.getInstance()
        ));
        presenter.attachView(this);
        presenter.loadHomeData();

        return binding.getRoot();
    }

    @Override
    public void showRandomMeals(List<Meal> meals) {
        mealAdapter.setMealList(meals);
    }

    @Override
    public void showIngredients(List<Ingredient> ingredients) {
        ingredientAdapter.setIngredientList(ingredients);
    }

    @Override
    public void showCategories(List<Category> categories) {
        categoryAdapter.updateCategories(categories);
    }

    @Override
    public void showAreas(List<Area> areas) {
        areaAdapter.updateAreas(areas);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(requireContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading(boolean isLoading) {
        // optional loading indicator
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
        binding = null;
    }
}

