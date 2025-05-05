package com.example.farakhni.freatures.favorite;

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
import com.example.farakhni.databinding.FragmentFavoriteBinding;
import com.example.farakhni.model.FavoriteMeal;
import com.example.farakhni.model.Meal;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment implements FavoriteContract.View {
    private FragmentFavoriteBinding binding;
    private FavoriteContract.Presenter presenter;
    private MealAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false);

        presenter = new FavoritePresenter(new FavoriteModel(requireContext()));
        presenter.attachView(this);

        binding.mealsList.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false)
        );
        adapter = new MealAdapter(requireContext(), new ArrayList<>());
        adapter.setOnMealClickListener(meal -> {
            if (requireContext() instanceof Activity) {
                try {
                    Bundle args = new Bundle();
                    args.putSerializable("arg_meal", new FavoriteMeal(meal));
                    NavController nav = Navigation.findNavController(
                            (Activity) requireContext(),
                            R.id.nav_host_fragment_content_app_screen);
                    nav.navigate(R.id.nav_meal_details, args);
                } catch (Exception e) {
                    Toast.makeText(requireContext(), "Navigation error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        adapter.setOnFavoriteToggleListener(meal -> {
            presenter.removeFavorite(new FavoriteMeal(meal));
            Toast.makeText(requireContext(),
                    meal.getName() + " removed from favorites",
                    Toast.LENGTH_SHORT).show();
        });

        binding.mealsList.setAdapter(adapter);
        presenter.loadFavorites(getViewLifecycleOwner());

        return binding.getRoot();
    }

    @Override
    public void showFavorites(List<Meal> favorites) {
        adapter.setMealList(favorites);
        List<FavoriteMeal> favMeals = new ArrayList<>();
        for (Meal meal : favorites) {
            favMeals.add(new FavoriteMeal(meal));
        }
        adapter.setFavoriteMeals(favMeals);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        adapter.setMealList(new ArrayList<>());
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        binding = null;
        super.onDestroyView();
    }
}