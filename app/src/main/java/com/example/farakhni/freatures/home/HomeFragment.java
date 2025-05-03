package com.example.farakhni.freatures.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.farakhni.common.CategoryAdapter;
import com.example.farakhni.common.IngredientAdapter;
import com.example.farakhni.common.MealAdapter;
import com.example.farakhni.data.repositories.CategoryRepositoryImpl;
import com.example.farakhni.data.repositories.IngredientRepositoryImpl;
import com.example.farakhni.data.repositories.MealRepositoryImpl;
import com.example.farakhni.databinding.FragmentHomeBinding;
import com.example.farakhni.freatures.mealdetails.MealDetailsActivity;
import com.example.farakhni.model.Category;
import com.example.farakhni.model.Ingredient;
import com.example.farakhni.model.Meal;

import java.util.List;

public class HomeFragment extends Fragment implements HomeContract.View {
    private FragmentHomeBinding binding;
    private HomeContract.Presenter presenter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        MealRepositoryImpl mealRepo = MealRepositoryImpl.getInstance(getContext());
        IngredientRepositoryImpl ingRepo = IngredientRepositoryImpl.getInstance();
        CategoryRepositoryImpl catRepo = CategoryRepositoryImpl.getInstance();

        HomeContract.Model model = new HomeModel(mealRepo, ingRepo, catRepo);
        presenter = new HomePresenter(model);
        presenter.attachView(this);

        binding.randomMealRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        binding.ingredientsList.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        binding.categoriesList.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );

        presenter.loadHomeData();

        return binding.getRoot();
    }

    @Override
    public void showRandomMeals(List<Meal> meals) {
        MealAdapter adapter = new MealAdapter(requireContext(), meals);
        adapter.setOnMealClickListener(meal -> {
            Intent i = new Intent(requireContext(), MealDetailsActivity.class);
            i.putExtra("extra_meal", meal);
            startActivity(i);
        });
        adapter.setOnFavoriteToggleListener(meal -> {
            Toast.makeText(getContext(), meal.getName() + " favorite toggled", Toast.LENGTH_SHORT).show();
        });
        binding.randomMealRecyclerView.setAdapter(adapter);
    }

    @Override
    public void showIngredients(List<Ingredient> ingredients) {
        IngredientAdapter adapter = new IngredientAdapter(requireContext(), ingredients);
        binding.ingredientsList.setAdapter(adapter);
    }

    @Override
    public void showCategories(List<Category> categories) {
        CategoryAdapter adapter = new CategoryAdapter(requireContext(), categories);
        binding.categoriesList.setAdapter(adapter);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        binding = null;
        super.onDestroyView();
    }
}
