package com.example.farakhni.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.farakhni.R;
import com.example.farakhni.data.network.NetworkCallBack;
import com.example.farakhni.data.repositories.MealRepository;
import com.example.farakhni.data.repositories.MealRepositoryImpl;
import com.example.farakhni.model.Category;
import com.example.farakhni.model.FavoriteMeal;
import com.example.farakhni.model.Meal;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private final Context context;
    private final List<Category> categoryList;
    private final MealRepository mealRepository;
    private final List<FavoriteMeal> favoriteMeals;

    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList != null ? new ArrayList<>(categoryList) : new ArrayList<>();
        this.mealRepository = MealRepositoryImpl.getInstance(context);
        this.favoriteMeals = new ArrayList<>();
    }

    public void updateCategories(List<Category> newCategories) {
        categoryList.clear();
        if (newCategories != null) {
            categoryList.addAll(newCategories);
        }
        notifyDataSetChanged();
    }

    public void setFavoriteMeals(List<FavoriteMeal> favMeals) {
        favoriteMeals.clear();
        if (favMeals != null) {
            favoriteMeals.addAll(favMeals);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        String categoryName = category.getCategory();
        holder.categoryName.setText(categoryName != null ? categoryName : "Unknown Category");

        Glide.with(context)
                .load(category.getCategoryThumb())
                .placeholder(R.drawable.app_logo)
                .error(R.drawable.app_logo)
                .into(holder.categoryImage);

        holder.itemCard.setOnClickListener(v -> {
            mealRepository.filterByCategory(categoryName, new NetworkCallBack<List<Meal>>() {
                @Override
                public void onSuccessResult(List<Meal> meals) {
                    if (meals.isEmpty()) {
                        Toast.makeText(context, "No meals for " + categoryName, Toast.LENGTH_SHORT).show();
                    } else {
                        fetchDetailsAndNavigate(meals);
                    }
                }

                @Override
                public void onFailureResult(String message) {
                    Toast.makeText(context, "Error: " + message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onLoading() {}

                @Override
                public void onNetworkError(String errorMessage) {
                    Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onEmptyData() {
                    Toast.makeText(context, "No meals found", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onProgress(int progress) {}
            });
        });

        holder.itemCard.setOnLongClickListener(v -> {
            View popupView = LayoutInflater.from(context)
                    .inflate(R.layout.popup_layout, null, false);
            TextView desc = popupView.findViewById(R.id.popupIngredientDescription);
            desc.setText(
                    category.getCategoryDescription() != null
                            ? category.getCategoryDescription()
                            : "No description available."
            );

            // measure & show
            popupView.measure(
                    View.MeasureSpec.UNSPECIFIED,
                    View.MeasureSpec.UNSPECIFIED
            );
            PopupWindow popup = new PopupWindow(
                    popupView,
                    popupView.getMeasuredWidth(),
                    popupView.getMeasuredHeight(),
                    true
            );
            popup.setOutsideTouchable(true);
            popup.showAsDropDown(v,
                    /* xOffset */ 0,
                    /* yOffset */ -v.getHeight() - popupView.getMeasuredHeight()
            );
            return true;  // consume the long-press
        });
    }

    private void fetchDetailsAndNavigate(List<Meal> simpleMeals) {
        List<FavoriteMeal> fullMeals = new ArrayList<>();
        int total = simpleMeals.size();

        for (Meal m : simpleMeals) {
            mealRepository.getMealById(m.getId(), new NetworkCallBack<List<Meal>>() {
                @Override
                public void onSuccessResult(List<Meal> detailed) {
                    if (!detailed.isEmpty()) {
                        FavoriteMeal detailedMeal = new FavoriteMeal(detailed.get(0));
                        for (FavoriteMeal favMeal : favoriteMeals) {
                            if (favMeal.getId().equals(detailedMeal.getId())) {
                                detailedMeal.setFavorite(true);
                                break;
                            }
                        }
                        synchronized (fullMeals) {
                            fullMeals.add(detailedMeal);
                            if (fullMeals.size() == total) {
                                navigateToCategoryFragment(fullMeals);
                            }
                        }
                    }
                }

                @Override
                public void onFailureResult(String message) {
                    synchronized (fullMeals) {
                        if (fullMeals.size() == total) {
                            navigateToCategoryFragment(fullMeals);
                        }
                    }
                }

                @Override
                public void onLoading() {}

                @Override
                public void onNetworkError(String errorMessage) {
                    synchronized (fullMeals) {
                        if (fullMeals.size() == total) {
                            navigateToCategoryFragment(fullMeals);
                        }
                    }
                }

                @Override
                public void onEmptyData() {
                    synchronized (fullMeals) {
                        if (fullMeals.size() == total) {
                            navigateToCategoryFragment(fullMeals);
                        }
                    }
                }

                @Override
                public void onProgress(int progress) {}
            });
        }
    }

    private void navigateToCategoryFragment(List<FavoriteMeal> meals) {
        if (!(context instanceof Activity)) return;
        try {
            Bundle bundle = new Bundle();
            bundle.putSerializable("meals", meals.toArray(new FavoriteMeal[0]));
            NavController navController = Navigation.findNavController((Activity) context,
                    R.id.nav_host_fragment_content_app_screen);
            navController.navigate(R.id.nav_meals, bundle);
        } catch (Exception e) {
            Toast.makeText(context, "Navigation error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryImage;
        TextView categoryName;
        CardView itemCard;


        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.itemImage);
            categoryName = itemView.findViewById(R.id.itemName);
            itemCard=itemView.findViewById(R.id.itemCard);
        }
    }
}