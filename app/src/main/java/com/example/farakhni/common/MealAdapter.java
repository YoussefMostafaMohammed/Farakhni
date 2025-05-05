package com.example.farakhni.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.farakhni.R;
import com.example.farakhni.model.FavoriteMeal;
import com.example.farakhni.model.Meal;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {
    private final Context context;
    private List<Meal> mealList;
    private Set<String> favoriteMealIds;
    private OnMealClickListener mealClickListener;
    private OnFavoriteToggleListener favoriteToggleListener;
    private OnCalendarClickListener calendarClickListener;

    public MealAdapter(Context context, List<Meal> mealList) {
        this.context = context;
        this.mealList = mealList != null ? new ArrayList<>(mealList) : new ArrayList<>();
        this.favoriteMealIds = new HashSet<>();
    }

    public void setMealList(List<Meal> meals) {
        this.mealList.clear();
        if (meals != null) {
            this.mealList.addAll(meals);
        }
        notifyDataSetChanged();
    }

    public void setFavoriteMeals(List<FavoriteMeal> favMeals) {
        favoriteMealIds.clear();
        if (favMeals != null) {
            for (FavoriteMeal fav : favMeals) {
                favoriteMealIds.add(fav.getId());
            }
        }
        notifyDataSetChanged();
    }

    public void setOnCalendarClickListener(OnCalendarClickListener listener) {
        this.calendarClickListener = listener;
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
        View view = LayoutInflater.from(context).inflate(R.layout.meal_item, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        Meal meal = mealList.get(position);

        holder.mealName.setText(meal.getName() != null ? meal.getName() : "Unknown Meal");
        String instr = meal.getInstructions() != null ? meal.getInstructions() : "Tap to see full description";
        holder.mealDescription.setText(instr.length() > 100 ? instr.substring(0, 100) + "…" : instr);

        Glide.with(context)
                .load(meal.getMealThumb())
                .placeholder(R.drawable.app_logo)
                .error(R.drawable.app_logo)
                .into(holder.mealImage);

        boolean isFav = favoriteMealIds.contains(meal.getId());
        meal.setFavorite(isFav);
        holder.heartIcon.setSelected(isFav);

        holder.mealImage.setOnClickListener(v -> {
            if (mealClickListener != null) {
                mealClickListener.onMealClicked(meal);
            }
        });

        holder.heartIcon.setOnClickListener(v -> {
            boolean newFav = !meal.isFavorite();
            meal.setFavorite(newFav);
            holder.heartIcon.setSelected(newFav);
            if (favoriteToggleListener != null) {
                favoriteToggleListener.onFavoriteToggled(meal);
            }
        });

        holder.calendarIcon.setSelected(meal.isScheduled());
        holder.calendarIcon.setOnClickListener(v -> showDatePicker(meal, holder.calendarIcon));
    }

    private void showDatePicker(Meal meal, ImageView calendarIcon) {
        boolean scheduled = !meal.isScheduled();
        meal.setScheduled(scheduled);
        calendarIcon.setSelected(scheduled);
        calendarIcon.startAnimation(AnimationUtils.loadAnimation(context, R.anim.calendar_bounce));

        MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Choose a date")
                .setTheme(R.style.ThemeOverlay_Farakhni_DatePicker)
                .build();
        picker.show(((FragmentActivity) context).getSupportFragmentManager(), "MEAL_DATE_PICKER");

        picker.addOnPositiveButtonClickListener(selection -> {
            if (calendarClickListener != null) {
                calendarClickListener.onCalendarIconClicked(meal, selection);
            }
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    static class MealViewHolder extends RecyclerView.ViewHolder {
        ImageView mealImage, heartIcon, calendarIcon;
        TextView mealName, mealDescription;

        MealViewHolder(@NonNull View itemView) {
            super(itemView);
            mealImage = itemView.findViewById(R.id.mealImage);
            mealName = itemView.findViewById(R.id.mealName);
            mealDescription = itemView.findViewById(R.id.mealDescription);
            heartIcon = itemView.findViewById(R.id.heartIcon);
            calendarIcon = itemView.findViewById(R.id.calendarIcon);
        }
    }

    public interface OnMealClickListener {
        void onMealClicked(Meal meal);
    }

    public interface OnFavoriteToggleListener {
        void onFavoriteToggled(Meal meal);
    }

    public interface OnCalendarClickListener {
        void onCalendarIconClicked(Meal meal, long selectedDateEpochMillis);
    }
}
