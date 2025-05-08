package com.example.farakhni.freatures.Search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.farakhni.R;
import com.example.farakhni.data.repositories.AreaRepositoryImpl;
import com.example.farakhni.data.repositories.CategoryRepositoryImpl;
import com.example.farakhni.data.repositories.IngredientRepositoryImpl;
import com.example.farakhni.data.repositories.MealRepositoryImpl;
import com.example.farakhni.databinding.FragmentSearchBinding;
import com.example.farakhni.model.Area;
import com.example.farakhni.model.Category;
import com.example.farakhni.model.FavoriteMeal;
import com.example.farakhni.model.Ingredient;
import com.example.farakhni.model.Meal;
import com.example.farakhni.utils.AreaAdapter;
import com.example.farakhni.utils.CategoryAdapter;
import com.example.farakhni.utils.IngredientAdapter;
import com.example.farakhni.utils.MealAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements SearchContract.View {
    private FragmentSearchBinding binding;
    private SearchContract.Presenter presenter;
    private MealAdapter mealAdapter;
    private AreaAdapter areaAdapter;
    private CategoryAdapter categoryAdapter;
    private IngredientAdapter ingredientAdapter;
    private static final String FILTER_ALL = "all";
    private static final String FILTER_INGREDIENT = "ingredient";
    private static final String FILTER_CATEGORY = "category";
    private static final String FILTER_COUNTRY = "country";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeAdapters();
        presenter = new SearchPresenter(new SearchModel(
                MealRepositoryImpl.getInstance(requireContext()),
                AreaRepositoryImpl.getInstance(),
                CategoryRepositoryImpl.getInstance(),
                IngredientRepositoryImpl.getInstance()
        ));
        presenter.attachView(this);
        presenter.initialize(getViewLifecycleOwner());

        setupSearchView();
        setupFilterGroup();
    }

    private void initializeAdapters() {
        mealAdapter = new MealAdapter(requireContext(), new ArrayList<>(), false);
        areaAdapter = new AreaAdapter(requireContext(), new ArrayList<>());
        categoryAdapter = new CategoryAdapter(requireContext(), new ArrayList<>());
        ingredientAdapter = new IngredientAdapter(requireContext(), new ArrayList<>());

        mealAdapter.setOnFavoriteToggleListener(meal -> presenter.onFavoriteToggled(meal));
        mealAdapter.setOnMealClickListener(meal -> presenter.onMealClicked(meal));
    }

    private void setupSearchView() {
        binding.searchView.setQueryHint(getString(R.string.search_hint));
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                presenter.onSearchQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                presenter.onSearchQuery(newText);
                return true;
            }
        });
    }

    private void setupFilterGroup() {
        binding.filterGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.filterAll) {
                presenter.onFilterChanged(FILTER_ALL);
            } else if (checkedId == R.id.filterIngredient) {
                presenter.onFilterChanged(FILTER_INGREDIENT);
            } else if (checkedId == R.id.filterCategory) {
                presenter.onFilterChanged(FILTER_CATEGORY);
            } else if (checkedId == R.id.filterCountry) {
                presenter.onFilterChanged(FILTER_COUNTRY);
            }
        });
    }

    @Override
    public void showProgressBar() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        binding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showMeals(List<Meal> meals) {
        mealAdapter.setMealList(meals);
    }

    @Override
    public void showAreas(List<Area> areas) {
        areaAdapter.updateAreas(areas);
    }

    @Override
    public void showCategories(List<Category> categories) {
        categoryAdapter.updateCategories(categories);
    }

    @Override
    public void showIngredients(List<Ingredient> ingredients) {
        ingredientAdapter.setIngredientList(ingredients);
    }

    @Override
    public void setMealAdapter() {
        binding.searchResults.setAdapter(mealAdapter);
        binding.searchResults.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    @Override
    public void setAreaAdapter() {
        binding.searchResults.setAdapter(areaAdapter);
        binding.searchResults.setLayoutManager(new GridLayoutManager(requireContext(), 2));
    }

    @Override
    public void setCategoryAdapter() {
        binding.searchResults.setAdapter(categoryAdapter);
        binding.searchResults.setLayoutManager(new GridLayoutManager(requireContext(), 2));
    }

    @Override
    public void setIngredientAdapter() {
        binding.searchResults.setAdapter(ingredientAdapter);
        binding.searchResults.setLayoutManager(new GridLayoutManager(requireContext(), 2));
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToMealDetails(FavoriteMeal meal) {
        Bundle args = new Bundle();
        args.putSerializable("arg_meal", meal);
        NavController nav = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_app_screen);
        nav.navigate(R.id.nav_meal_details, args);
    }

    @Override
    public void observeFavoriteMeals(LifecycleOwner owner) {
        presenter.observeFavoriteMeals(owner, this);
    }

    public MealAdapter getMealAdapter() {
        return mealAdapter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
        binding = null;
    }
}