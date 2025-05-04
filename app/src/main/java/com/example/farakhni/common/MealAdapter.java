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
import com.example.farakhni.model.Meal;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.ArrayList;
import java.util.List;
public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {
    private final Context context;
    private List<Meal> mealList;
    private List<Meal> favoriteMeals = new ArrayList<>();
    private OnMealClickListener mealClickListener;
    private OnFavoriteToggleListener favoriteToggleListener;
    private OnCalendarClickListener calendarClickListener;
    public void setFavoriteMeals(List<Meal> favMeals) {
        favoriteMeals.clear();
        if (favMeals != null) {
            favoriteMeals.addAll(favMeals);
        }
        notifyDataSetChanged();
    }

    public MealAdapter(Context context, List<Meal> mealList) {
        this.context = context;
        this.mealList = mealList;
    }

    public void setMealList(List<Meal> meals) {
        this.mealList = meals;
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
        View view = LayoutInflater.from(context)
                .inflate(R.layout.meal_item, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        Meal meal = mealList.get(position);

        holder.mealName.setText(meal.getName() != null ? meal.getName() : "Unknown Meal");

        String instr = meal.getInstructions() != null ? meal.getInstructions() : "No description available";
        holder.mealDescription.setText(instr.length() > 100 ? instr.substring(0, 100) + "…" : instr);

        Glide.with(context)
                .load(meal.getMealThumb())
                .placeholder(R.drawable.app_logo)
                .error(R.drawable.app_logo)
                .into(holder.mealImage);

        meal.setFavorite(false);
        for (Meal favMeal : favoriteMeals) {
            if (favMeal.getId().equals(meal.getId())) {
                meal.setFavorite(true);
                break;
            }
        }

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
        holder.calendarIcon.setSelected(meal.isScheduled());
        holder.calendarIcon.setOnClickListener(v -> showDatePicker(meal, holder.calendarIcon));
    }
    private void showDatePicker(Meal meal, ImageView calendarIcon) {
        boolean scheduled = !meal.isScheduled();
        meal.setScheduled(scheduled);
        calendarIcon.setSelected(scheduled);
        calendarIcon.startAnimation(AnimationUtils.loadAnimation(context, R.anim.calendar_bounce));

        // build and show picker
        MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Choose a date")
                .setTheme(R.style.ThemeOverlay_Farakhni_DatePicker)
                .build();
        picker.show(((FragmentActivity) context).getSupportFragmentManager(), "MEAL_DATE_PICKER");

        picker.addOnPositiveButtonClickListener(selection -> {
            if (calendarClickListener != null) {
                calendarClickListener.onCalendarIconClicked(meal, selection);
            }
        });
    }
    @Override
    public int getItemCount() {
        return mealList != null ? mealList.size() : 0;
    }

    static class MealViewHolder extends RecyclerView.ViewHolder {
        ImageView mealImage, heartIcon,calendarIcon;
        TextView mealName, mealDescription;

        MealViewHolder(@NonNull View itemView) {
            super(itemView);
            mealImage = itemView.findViewById(R.id.mealImage);
            mealName = itemView.findViewById(R.id.mealName);
            mealDescription = itemView.findViewById(R.id.mealDescription);
            heartIcon = itemView.findViewById(R.id.heartIcon);
            calendarIcon=itemView.findViewById(R.id.calendarIcon);
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