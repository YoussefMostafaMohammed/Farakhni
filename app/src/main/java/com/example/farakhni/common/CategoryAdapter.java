package com.example.farakhni.common;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.farakhni.R;
import com.example.farakhni.data.network.NetworkCallBack;
import com.example.farakhni.data.repositories.MealRepository;
import com.example.farakhni.data.repositories.MealRepositoryImpl;
import com.example.farakhni.model.Category;
import com.example.farakhni.model.Meal;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private final Context context;
    private final List<Category> categoryList;
    private final MealRepository mealRepository;
    private List<Meal> favoriteMeals = new ArrayList<>();


    public void setFavoriteMeals(List<Meal> favMeals) {
        favoriteMeals.clear();
        if (favMeals != null) {
            favoriteMeals.addAll(favMeals);
        }
    }
    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList != null ? categoryList : new ArrayList<>();
        this.mealRepository = MealRepositoryImpl.getInstance(context);
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        String categoryName = category.getCategory();

        holder.categoryName.setText(categoryName != null ? categoryName : "Unknown Category");

        Glide.with(context)
                .load(category.getCategoryThumb())
                .into(holder.categoryImage);

        holder.categoryImage.setOnClickListener(v -> {
            // Fetch meals by category
            mealRepository.filterByCategory(categoryName, new NetworkCallBack<List<Meal>>() {
                @Override public void onSuccessResult(List<Meal> meals) {
                    if (meals.isEmpty()) {
                        Toast.makeText(context, "No meals for " + categoryName, Toast.LENGTH_SHORT).show();
                    } else {
                        fetchDetailsAndNavigate(meals);
                    }
                }

                @Override public void onFailureResult(String message) {
                    Toast.makeText(context, "Error: " + message, Toast.LENGTH_SHORT).show();
                }

                @Override public void onLoading() {}

                @Override public void onNetworkError(String errorMessage) {}

                @Override public void onEmptyData() {}

                @Override public void onProgress(int progress) {}
            });
        });

        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            PopupWindow popup;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        View popupView = LayoutInflater.from(context)
                                .inflate(R.layout.popup_layout, null, false);
                        TextView desc = popupView.findViewById(R.id.popupIngredientDescription);
                        desc.setText(category.getCategoryDescription() != null
                                ? category.getCategoryDescription()
                                : "No description available.");

                        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

                        popup = new PopupWindow(
                                popupView,
                                popupView.getMeasuredWidth(),
                                popupView.getMeasuredHeight(),
                                true
                        );
                        popup.setOutsideTouchable(true);
                        popup.showAsDropDown(v, 0, -v.getHeight() - popupView.getMeasuredHeight());
                        return true;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (popup != null && popup.isShowing()) {
                            popup.dismiss();
                        }
                        return true;
                }
                return false;
            }
        });
    }

    private void fetchDetailsAndNavigate(List<Meal> simpleMeals) {
        List<Meal> fullMeals = new ArrayList<>();
        int total = simpleMeals.size();

        for (Meal m : simpleMeals) {
            mealRepository.getMealById(m.getId(), new NetworkCallBack<List<Meal>>() {
                @Override public void onSuccessResult(List<Meal> detailed) {
                    if (!detailed.isEmpty()) {
                        Meal detailedMeal = detailed.get(0);
                        for (Meal meal : favoriteMeals) {
                            if (meal.getId().equals(detailedMeal.getId())) {
                                detailedMeal.setFavorite(true);
                                break;
                            }
                        }
                        fullMeals.add(detailedMeal);                        }
                    if (fullMeals.size() == total) {
                        navigateToCategoryFragment(fullMeals);
                    }
                }

                @Override public void onFailureResult(String message) {
                    if (fullMeals.size() == total) {
                        navigateToCategoryFragment(fullMeals);
                    }
                }

                @Override public void onLoading() {}

                @Override public void onNetworkError(String errorMessage) {}

                @Override public void onEmptyData() {}

                @Override public void onProgress(int progress) {}
            });
        }
    }

    private void navigateToCategoryFragment(List<Meal> meals) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("meals", meals.toArray(new Meal[0]));

        NavController navController = Navigation.findNavController(
                (Activity) context,
                R.id.nav_host_fragment_content_app_screen
        );
        navController.navigate(R.id.nav_meals, bundle); // Replace with correct destination ID
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public void updateCategories(List<Category> newCategories) {
        categoryList.clear();
        categoryList.addAll(newCategories);
        notifyDataSetChanged();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryImage;
        TextView categoryName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.itemImage);
            categoryName = itemView.findViewById(R.id.itemName);
        }
    }
}
