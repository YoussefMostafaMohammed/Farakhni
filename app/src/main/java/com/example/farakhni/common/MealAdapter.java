package com.example.farakhni.common;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.farakhni.R;
import com.example.farakhni.data.repositories.MealRepositoryImpl;
import com.example.farakhni.model.FavoriteMeal;
import com.example.farakhni.model.Meal;
import com.example.farakhni.model.PlannedMeal;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.CompositeDateValidator;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {
    private final Context context;
    private List<Meal> mealList;
    private Set<String> favoriteMealIds;
    private final MealRepositoryImpl mealRepository;
    private OnMealClickListener mealClickListener;
    private OnFavoriteToggleListener favoriteToggleListener;
    private OnCalendarClickListener calendarClickListener;

    public MealAdapter(Context context, List<Meal> mealList) {
        this.context = context;
        this.mealList = mealList != null ? new ArrayList<>(mealList) : new ArrayList<>();
        this.favoriteMealIds = new HashSet<>();
        this.mealRepository = MealRepositoryImpl.getInstance(context);
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
        //Log.d("SlideshowFragment", "Fetched " + meal.getId());
        holder.mealName.setText(meal.getName() != null ? meal.getName() : "Unknown Meal");
        String instr = meal.getInstructions() != null ? meal.getInstructions() : "Tap to see full Instructions.";
        holder.mealDescription.setText(instr.length() > 100 ? instr.substring(0, 100) + "…" : instr);

        Glide.with(context)
                .load(meal.getMealThumb())
                .placeholder(R.drawable.app_logo)
                .error(R.drawable.app_logo)
                .into(holder.mealImage);

        boolean isFav = favoriteMealIds.contains(meal.getId());
        meal.setFavorite(isFav);
        holder.heartIcon.setSelected(isFav);

        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        boolean isScheduled = mealRepository.isMealPlanned(meal.getId(), today);
        meal.setScheduled(isScheduled);
        holder.calendarIcon.setSelected(false);

        holder.mealImage.setOnClickListener(v -> {
            if (mealClickListener != null) {
                mealClickListener.onMealClicked(meal);
            }
        });

        holder.heartIcon.setOnClickListener(v -> {
            boolean newFav = !meal.isFavorite();
            meal.setFavorite(newFav);
            holder.heartIcon.setSelected(newFav);
            // Apply heart scale animation
            Animator animator = AnimatorInflater.loadAnimator(context, R.animator.heart_scale);
            animator.setTarget(holder.heartIcon);
            animator.start();
            if (favoriteToggleListener != null) {
                favoriteToggleListener.onFavoriteToggled(meal);
            }
        });

        holder.calendarIcon.setOnClickListener(v -> showDatePicker(meal, holder.calendarIcon));

    }

    private void showDatePicker(Meal meal, ImageView calendarIcon) {
        calendarIcon.setSelected(true);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long today = cal.getTimeInMillis();

        cal.add(Calendar.DAY_OF_MONTH, 7);
        long nextWeek = cal.getTimeInMillis();

        CalendarConstraints constraints = new CalendarConstraints.Builder()
                .setStart(today)
                .setEnd(nextWeek)
                .setValidator(
                        CompositeDateValidator.allOf(Arrays.asList(
                                DateValidatorPointForward.from(today),
                                DateValidatorPointBackward.before(nextWeek)
                        ))
                )
                .build();

        MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Choose a date (within next 7 days)")
                .setTheme(R.style.ThemeOverlay_Farakhni_DatePicker)
                .setCalendarConstraints(constraints)
                .setSelection(today)
                .build();

        picker.show(((FragmentActivity) context).getSupportFragmentManager(), "MEAL_DATE_PICKER");

        picker.addOnPositiveButtonClickListener(selection -> {
            String selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(new Date(selection));
            String todayStr = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(new Date(today));

            // now your existing insert/delete logic…
            try {
                boolean isPlanned = mealRepository.isMealPlanned(meal.getId(), selectedDate);
                PlannedMeal plannedMeal = new PlannedMeal(meal);
                plannedMeal.setScheduledDate(selectedDate);

                if (isPlanned) {
                    mealRepository.deletePlannedMeal(plannedMeal);
                    Toast.makeText(context,
                            meal.getName() + " removed from " + selectedDate,
                            Toast.LENGTH_SHORT).show();
                } else {
                    mealRepository.insertPlannedMeal(plannedMeal);
                    Toast.makeText(context,
                            meal.getName() + " planned for " + selectedDate,
                            Toast.LENGTH_SHORT).show();
                }

                // re-query today’s flag to update UI
                boolean isScheduledToday = mealRepository.isMealPlanned(meal.getId(), todayStr);
                meal.setScheduled(isScheduledToday);
                calendarIcon.setSelected(isScheduledToday);
                calendarIcon.startAnimation(
                        AnimationUtils.loadAnimation(context, R.anim.calendar_bounce)
                );
            } catch (Exception e) {
                Log.e("MealAdapter", "Error scheduling: " + e.getMessage(), e);
                Toast.makeText(context, "Error scheduling date", Toast.LENGTH_SHORT).show();
            }

            if (calendarClickListener != null) {
                calendarClickListener.onCalendarIconClicked(meal, selection);
            }
            notifyDataSetChanged();
        });
        picker.addOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                calendarIcon.setSelected(false);
            }
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