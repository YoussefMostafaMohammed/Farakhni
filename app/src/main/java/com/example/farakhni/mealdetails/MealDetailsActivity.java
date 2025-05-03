package com.example.farakhni.mealdetails;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.farakhni.R;
import com.example.farakhni.model.Ingredient;
import com.example.farakhni.model.Meal;

import java.util.List;

public class MealDetailsActivity extends AppCompatActivity {
    private ImageView mealImage;
    private TextView mealName;
    private TextView calories;
    private TextView protein;
    private TextView carbs;
    private TextView instructions;

    private RecyclerView ingredientsRecycler;
    private IngredientDetailsAdapter ingredientAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_details);

        // 1) Bind views
        mealImage    = findViewById(R.id.mealImage);
        mealName     = findViewById(R.id.mealName);
        calories     = findViewById(R.id.calories);
        protein      = findViewById(R.id.protein);
        carbs        = findViewById(R.id.carbs);
        instructions = findViewById(R.id.instructions);

        ingredientsRecycler = findViewById(R.id.ingredientsList);

        // 2) Retrieve the Meal object
        Meal meal = (Meal) getIntent().getSerializableExtra("extra_meal");
        if (meal == null) {
            Toast.makeText(this, "Failed to load meal details.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 3) Populate the top section
        mealName.setText(meal.getName() != null
                ? meal.getName()
                : "Unknown Meal");
        instructions.setText(meal.getInstructions() != null
                ? meal.getInstructions()
                : "No instructions available");
        calories.setText("N/A Calories");
        protein.setText("N/A Protein");
        carbs.setText("N/A Carbs");
        Glide.with(this)
                .load(meal.getMealThumb())
                .placeholder(R.drawable.app_logo)
                .error(R.drawable.app_logo)
                .into(mealImage);

        List<Ingredient> ingredientList = meal.getIngredientsList();
        ingredientAdapter = new IngredientDetailsAdapter(this, ingredientList);
        ingredientsRecycler.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );
        ingredientsRecycler.setAdapter(ingredientAdapter);
    }
}
