package com.example.farakhni.utils;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
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
import com.google.firebase.auth.FirebaseAuth;

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
    private final String userId;
    private final boolean showDeleteIcon;

    public MealAdapter(Context context,
                       List<Meal> mealList,
                       boolean showDeleteIcon) {
        this.context = context;
        this.mealList = mealList != null ? new ArrayList<>(mealList) : new ArrayList<>();
        this.favoriteMealIds = new HashSet<>();
        this.mealRepository = MealRepositoryImpl.getInstance(context);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        this.userId = auth.getCurrentUser() != null
                ? auth.getCurrentUser().getUid()
                : null;
        this.showDeleteIcon = showDeleteIcon;
    }

    public void setMealList(List<Meal> meals) {
        this.mealList.clear();
        if (meals != null) this.mealList.addAll(meals);
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
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                             int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.meal_item, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder,
                                 int position) {
        Meal meal = mealList.get(position);
        holder.mealName.setText(
                meal.getName() != null ? meal.getName() : "Unknown Meal"
        );
        String instr = meal.getInstructions() != null
                ? meal.getInstructions()
                : "Tap to see full Instructions.";
        holder.mealDescription.setText(
                instr.length() > 100
                        ? instr.substring(0, 100) + "…"
                        : instr
        );

        Glide.with(context)
                .load(meal.getMealThumb())
                .placeholder(R.drawable.app_logo)
                .error(R.drawable.app_logo)
                .into(holder.mealImage);

        // Favorite
        boolean isFav = favoriteMealIds.contains(meal.getId());
        meal.setFavorite(isFav);
        holder.heartIcon.setSelected(isFav);

        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(new Date());
        boolean isScheduled = mealRepository.isMealPlanned(meal.getId(), today);
        meal.setScheduled(isScheduled);

        holder.calendarIconDelete.setVisibility(
                showDeleteIcon ? View.VISIBLE : View.GONE
        );
        holder.calendarIconDelete.setSelected(true);

        holder.mealImage.setOnClickListener(v -> {
            if (mealClickListener != null) {
                mealClickListener.onMealClicked(meal);
            }
        });

        holder.heartIcon.setOnClickListener(v -> {
            if (userId == null) {
                Toast.makeText(context,
                        "Please log in to favorite meals",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            // Toggle favorite
            boolean newFav = !meal.isFavorite();
            meal.setFavorite(newFav);
            favoriteMealIds.remove(meal.getId());
            if (newFav) {
                favoriteMealIds.add(meal.getId());
                mealRepository.insertFavoriteMeal(new FavoriteMeal(meal));
            } else {
                mealRepository.deleteFavoriteMeal(new FavoriteMeal(meal));
            }
            if (favoriteToggleListener != null) {
                favoriteToggleListener.onFavoriteToggled(meal);
            }
            // Re-bind this item so calendar icon keeps correct tint
            notifyItemChanged(holder.getAdapterPosition());
        });

        holder.calendarIconAdd.setOnClickListener(v -> {
            if (userId == null) {
                Toast.makeText(context,
                        "Please log in to plan meals",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            showDatePicker(meal, holder.calendarIconAdd);
        });


        if (showDeleteIcon) {
            holder.calendarIconDelete.setOnClickListener(v -> {
                holder.calendarIconDelete.setSelected(false);
                String todayStr = new SimpleDateFormat("yyyy-MM-dd",
                        Locale.getDefault()).format(new Date());
                Toast.makeText(context,
                        meal.getName() + " removed from plan",
                        Toast.LENGTH_SHORT).show();
                if (calendarClickListener != null) {
                    calendarClickListener.onCalendarIconDeleteClicked(meal);
                }
                notifyItemChanged(holder.getAdapterPosition());
            });
        }
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    private void showDatePicker(Meal meal, ImageView calendarIconAdd) {
        calendarIconAdd.setSelected(true);

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
                .setValidator(CompositeDateValidator.allOf(Arrays.asList(
                        DateValidatorPointForward.from(today),
                        DateValidatorPointBackward.before(nextWeek)
                )))
                .build();

        MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Choose a date (next 7 days)")
                .setTheme(R.style.ThemeOverlay_Farakhni_DatePicker)  // ← restored!
                .setSelection(today)
                .setCalendarConstraints(constraints)
                .build();

        picker.show(
                ((FragmentActivity) context)
                        .getSupportFragmentManager(),
                "MEAL_DATE_PICKER"
        );

        picker.addOnPositiveButtonClickListener(selection -> {
            String selDate = new SimpleDateFormat("yyyy-MM-dd",
                    Locale.getDefault()).format(new Date(selection));
            PlannedMeal plannedMeal = new PlannedMeal(meal, selDate);

            if (mealRepository.isMealPlanned(meal.getId(), selDate)) {
                Toast.makeText(context,
                        meal.getName() + " Already Planned for " + selDate,
                        Toast.LENGTH_SHORT).show();
            } else {
                mealRepository.insertPlannedMeal(plannedMeal);
                Toast.makeText(context,
                        meal.getName() + " planned for " + selDate,
                        Toast.LENGTH_SHORT).show();
            }

            calendarIconAdd.startAnimation(
                    AnimationUtils.loadAnimation(context,
                            R.anim.calendar_bounce)
            );

            if (calendarClickListener != null) {
                calendarClickListener
                        .onCalendarIconAddClicked(meal, selection);
            }

            notifyItemChanged(
                    ((FragmentActivity) context)
                            .getSupportFragmentManager()
                            .getFragments()
                            .indexOf(this)
            );
        });

        picker.addOnDismissListener(dialog ->
                calendarIconAdd.setSelected(false)
        );
    }

    static class MealViewHolder extends RecyclerView.ViewHolder {
        ImageView mealImage, heartIcon,
                calendarIconAdd, calendarIconDelete;
        TextView mealName, mealDescription;

        MealViewHolder(@NonNull View itemView) {
            super(itemView);
            mealImage         = itemView.findViewById(R.id.mealImage);
            mealName          = itemView.findViewById(R.id.mealName);
            mealDescription   = itemView.findViewById(R.id.mealDescription);
            heartIcon         = itemView.findViewById(R.id.heartIcon);
            calendarIconAdd   = itemView.findViewById(R.id.calendarIconAdd);
            calendarIconDelete= itemView.findViewById(R.id.calendarIconDelete);
        }
    }

    public interface OnMealClickListener {
        void onMealClicked(Meal meal);
    }

    public interface OnFavoriteToggleListener {
        void onFavoriteToggled(Meal meal);
    }

    public interface OnCalendarClickListener {
        void onCalendarIconAddClicked(Meal meal, long dateEpochMillis);
        void onCalendarIconDeleteClicked(Meal meal);
    }
}
