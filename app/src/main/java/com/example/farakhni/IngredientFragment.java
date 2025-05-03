package com.example.farakhni;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.farakhni.databinding.FragmentIngredientBinding;
import com.example.farakhni.model.Meal;
import com.example.farakhni.ui.favorite.FavoriteViewModel;
import com.example.farakhni.ui.home.MealAdapter;

import java.util.ArrayList;

public class IngredientFragment extends Fragment {
    private static final String ARG_MEALS = "meals_list";

    private FragmentIngredientBinding binding;
    private MealAdapter mealAdapter;
    private ArrayList<Meal> mealsList;

    public IngredientFragment() {}

    public static IngredientFragment newInstance(ArrayList<Meal> meals) {
        IngredientFragment fragment = new IngredientFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MEALS, meals);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        new ViewModelProvider(this).get(FavoriteViewModel.class);
        binding = FragmentIngredientBinding.inflate(inflater, container, false);

        if (getArguments() != null) {
            mealsList = (ArrayList<Meal>) getArguments().getSerializable(ARG_MEALS);
        }

        mealAdapter = new MealAdapter(requireContext(), mealsList);
        binding.mealsList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.mealsList.setAdapter(mealAdapter);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
