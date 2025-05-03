package com.example.farakhni.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farakhni.data.DB.AppDataBase;
import com.example.farakhni.databinding.FragmentHomeBinding;
import com.example.farakhni.model.Category;
import com.example.farakhni.model.Ingredient;
import com.example.farakhni.model.Meal;
import com.example.farakhni.data.network.NetworkCallBack;
import com.example.farakhni.data.network.category.CategoryRemoteDataSourceImpl;
import com.example.farakhni.data.network.ingredient.IngredientsRemoteDataSourceImpl;
import com.example.farakhni.data.network.meal.MealsRemoteDataSourceImpl;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private RecyclerView randomMealRecyclerView;
    private RecyclerView ingredientsRecyclerView;
    private RecyclerView categoriesRecyclerView;
    private MealAdapter mealAdapter;
    private IngredientAdapter ingredientAdapter;
    private CategoryAdapter categoryAdapter;

    private final List<Meal> mealList = new ArrayList<>();
    private final List<Ingredient> ingredientList = new ArrayList<>();
    private final List<Category> categoryList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Random meals
        randomMealRecyclerView = binding.randomMealRecyclerView;
        randomMealRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mealAdapter = new MealAdapter(getContext(), mealList);
        randomMealRecyclerView.setAdapter(mealAdapter);

        MealsRemoteDataSourceImpl.getInstance().makeNetworkCallgetRandomMeal(new NetworkCallBack<List<Meal>>() {
            @Override
            public void onSuccessResult(List<Meal> result) {
                mealList.clear();
                mealList.addAll(result);
                mealAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailureResult(String errorMessage) {
                Toast.makeText(getContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        // Ingredients
        ingredientsRecyclerView = binding.ingredientsList;
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        ingredientAdapter = new IngredientAdapter(getContext(), ingredientList);
        ingredientsRecyclerView.setAdapter(ingredientAdapter);

        IngredientsRemoteDataSourceImpl.getInstance().makeNetworkCall(new NetworkCallBack<List<Ingredient>>() {
            @Override
            public void onSuccessResult(List<Ingredient> result) {
                ingredientList.clear();
                ingredientList.addAll(result);
                ingredientAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailureResult(String errorMessage) {
                Toast.makeText(getContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        // Categories
        categoriesRecyclerView = binding.categoriesList;
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryAdapter = new CategoryAdapter(getContext(), categoryList);
        categoriesRecyclerView.setAdapter(categoryAdapter);

        CategoryRemoteDataSourceImpl.getInstance().makeNetworkCall(new NetworkCallBack<List<Category>>() {
            @Override
            public void onSuccessResult(List<Category> result) {
                categoryList.clear();
                categoryList.addAll(result);
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailureResult(String errorMessage) {
                Toast.makeText(getContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AppDataBase.getInstance(getContext()).forceCheckpoint();
        binding = null;
    }
}
