package com.example.farakhni.ui.home;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.farakhni.AppScreen;
import com.example.farakhni.DB.FavoriteMealDAO;
import com.example.farakhni.DB.FavoriteMealDataBase;
import com.example.farakhni.login.LoginScreen;
import com.example.farakhni.mealdetails.MealDetailsActivity;
import com.example.farakhni.model.Meal;
import com.example.farakhni.R;
import com.example.farakhni.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {
    private Context context;
    private FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();    // ← add this

    public void setMealList(List<Meal> mealList) {
        this.mealList = mealList;
    }

    private List<Meal> mealList;

    public MealAdapter(Context context, List<Meal> mealList) {
        this.context = context;
        this.mealList = mealList;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.meal_item, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        Meal meal = mealList.get(position);
        holder.mealName.setText(meal.getName() != null ? meal.getName() : "Unknown Meal");
        String instructions = meal.getInstructions() != null ? meal.getInstructions() : "No description available";
        holder.mealDescription.setText(instructions.length() > 100 ? instructions.substring(0, 100) + "..." : instructions);
        holder.calories.setText("N/A Calories");
        holder.protein.setText("N/A Protein");
        holder.carbs.setText("N/A Carbs");

        Glide.with(context)
                .load(meal.getMealThumb())
                .placeholder(R.drawable.app_logo)
                .error(R.drawable.app_logo)
                .into(holder.mealImage);

        holder.mealImage.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), MealDetailsActivity.class);
            intent.putExtra("extra_meal",meal);
            v.getContext().startActivity(intent);
        });

        holder.heartIcon.setSelected(meal.isFavorite());
        holder.heartIcon.setOnClickListener(v -> {
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            if (currentUser == null) {
                Toast.makeText(v.getContext(), "Please log in to add favorites", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(v.getContext(), "username: "+currentUser.getDisplayName()+" id: "+currentUser.getUid(), Toast.LENGTH_SHORT).show();
            boolean newState = !meal.isFavorite();
            meal.setFavorite(newState);
            v.setSelected(newState);
            FavoriteMealDataBase db = FavoriteMealDataBase.getInstance(context);
            FavoriteMealDAO dao = db.getFavoriteMealDAO();
            if (newState) {
                new Thread(() -> dao.insertMeal(meal)).start();

                Toast.makeText(v.getContext(), "Meal Added To Favorites", Toast.LENGTH_SHORT).show();
            } else {
                new Thread(() -> dao.deleteMeal(meal)).start();
                Toast.makeText(v.getContext(), "Meal Removed From Favorites", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    public static class MealViewHolder extends RecyclerView.ViewHolder {
        ImageView mealImage, heartIcon;
        TextView mealName, mealDescription, calories, protein, carbs;
        ImageView heart;
        FavoriteMealDataBase favoriteMealDataBase;
        FavoriteMealDAO favoriteMealDAO;
        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            mealImage = itemView.findViewById(R.id.mealImage);
            mealName = itemView.findViewById(R.id.mealName);
            mealDescription = itemView.findViewById(R.id.mealDescription);
            calories = itemView.findViewById(R.id.calories);
            protein = itemView.findViewById(R.id.protein);
            carbs = itemView.findViewById(R.id.carbs);
            heartIcon = itemView.findViewById(R.id.heartIcon);
            favoriteMealDataBase = FavoriteMealDataBase.getInstance(itemView.getContext());
            favoriteMealDAO=favoriteMealDataBase.getFavoriteMealDAO();

        }
    }
}