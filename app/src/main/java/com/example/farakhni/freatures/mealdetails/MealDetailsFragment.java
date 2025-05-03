package com.example.farakhni.freatures.mealdetails;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.farakhni.R;
import com.example.farakhni.common.IngredientDetailsAdapter;
import com.example.farakhni.databinding.FragmentMealDetailsBinding;
import com.example.farakhni.model.Ingredient;
import com.example.farakhni.model.Meal;

import java.util.List;

public class MealDetailsFragment extends Fragment {
    private static final String ARG_MEAL = "arg_meal";

    private FragmentMealDetailsBinding binding;
    private IngredientDetailsAdapter ingredientAdapter;

    public static MealDetailsFragment newInstance(Meal meal) {
        MealDetailsFragment f = new MealDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MEAL, meal);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMealDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1) Retrieve the Meal from arguments
        Meal meal = null;
        if (getArguments() != null) {
            meal = (Meal) getArguments().getSerializable(ARG_MEAL);
        }
        if (meal == null) {
            Toast.makeText(requireContext(), "Failed to load meal details.", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
            return;
        }

        // 2) Populate header
        binding.mealName.setText(
                meal.getName() != null ? meal.getName() : "Unknown Meal"
        );
        binding.instructions.setText(
                meal.getInstructions() != null
                        ? meal.getInstructions()
                        : "No instructions available"
        );
        binding.calories.setText("N/A Calories");
        binding.protein.setText("N/A Protein");
        binding.carbs.setText("N/A Carbs");
        Glide.with(this)
                .load(meal.getMealThumb())
                .placeholder(R.drawable.app_logo)
                .error(R.drawable.app_logo)
                .into(binding.mealImage);

        List<Ingredient> ingredients = meal.getIngredientsList();
        ingredientAdapter = new IngredientDetailsAdapter(requireContext(), ingredients);
        binding.ingredientsList.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        binding.ingredientsList.setAdapter(ingredientAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
