package com.example.farakhni.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.farakhni.R;
import com.example.farakhni.model.Meal;
import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {
    private final Context context;
    private List<Meal> mealList;
    private OnMealClickListener mealClickListener;
    private OnFavoriteToggleListener favoriteToggleListener;

    public MealAdapter(Context context, List<Meal> mealList) {
        this.context = context;
        this.mealList = mealList;
    }

    public void setMealList(List<Meal> meals) {
        this.mealList = meals;
        notifyDataSetChanged();
    }

    public void setOnMealClickListener(OnMealClickListener listener) {
        this.mealClickListener = listener;
    }

    public void setOnFavoriteToggleListener(OnFavoriteToggleListener listener) {
        this.favoriteToggleListener = listener;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.meal_item, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        Meal meal = mealList.get(position);

        // Set meal name
        holder.mealName.setText(meal.getName() != null ? meal.getName() : "Unknown Meal");

        // Truncate description
        String instr = meal.getInstructions() != null ? meal.getInstructions() : "No description available";
        holder.mealDescription.setText(instr.length() > 100 ? instr.substring(0, 100) + "…" : instr);

        // Placeholder for nutrients
        holder.calories.setText("N/A Calories");
        holder.protein.setText("N/A Protein");
        holder.carbs.setText("N/A Carbs");

        // Load image
        Glide.with(context)
                .load(meal.getMealThumb())
                .placeholder(R.drawable.app_logo)
                .error(R.drawable.app_logo)
                .into(holder.mealImage);

        holder.mealImage.setOnClickListener(v -> {
            if (mealClickListener != null) {
                mealClickListener.onMealClicked(meal);
            }
        });

        holder.heartIcon.setSelected(meal.isFavorite());
        holder.heartIcon.setOnClickListener(v -> {
            boolean newFav = !meal.isFavorite();
            meal.setFavorite(newFav);
            holder.heartIcon.setSelected(newFav);

            if (favoriteToggleListener != null) {
                favoriteToggleListener.onFavoriteToggled(meal);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mealList != null ? mealList.size() : 0;
    }

    static class MealViewHolder extends RecyclerView.ViewHolder {
        ImageView mealImage, heartIcon;
        TextView mealName, mealDescription, calories, protein, carbs;

        MealViewHolder(@NonNull View itemView) {
            super(itemView);
            mealImage       = itemView.findViewById(R.id.mealImage);
            mealName        = itemView.findViewById(R.id.mealName);
            mealDescription = itemView.findViewById(R.id.mealDescription);
            calories        = itemView.findViewById(R.id.calories);
            protein         = itemView.findViewById(R.id.protein);
            carbs           = itemView.findViewById(R.id.carbs);
            heartIcon       = itemView.findViewById(R.id.heartIcon);
        }
    }

    public interface OnMealClickListener {
        void onMealClicked(Meal meal);
    }

    public interface OnFavoriteToggleListener {
        void onFavoriteToggled(Meal meal);
    }
}
