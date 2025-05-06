package com.example.farakhni.common;

import android.annotation.SuppressLint;
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
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.farakhni.R;
import com.example.farakhni.data.network.NetworkCallBack;
import com.example.farakhni.data.repositories.MealRepository;
import com.example.farakhni.data.repositories.MealRepositoryImpl;
import com.example.farakhni.model.FavoriteMeal;
import com.example.farakhni.model.Ingredient;
import com.example.farakhni.model.Meal;

import java.util.ArrayList;
import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {
    private final Context context;
    private final List<Ingredient> ingredientList;
    private final MealRepository mealRepository;
    private final List<FavoriteMeal> favoriteMeals;

    public IngredientAdapter(Context context, List<Ingredient> list) {
        this.context = context;
        this.ingredientList = list != null ? new ArrayList<>(list) : new ArrayList<>();
        this.mealRepository = MealRepositoryImpl.getInstance(context);
        this.favoriteMeals = new ArrayList<>();
    }

    public void setIngredientList(List<Ingredient> newList) {
        ingredientList.clear();
        if (newList != null) {
            ingredientList.addAll(newList);
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
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.card_item, parent, false);
        return new IngredientViewHolder(v);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient ingredient = ingredientList.get(position);
        String name = ingredient.getIngredient();
        holder.ingredientInfo.setText(name != null ? name : "Unknown Ingredient");

        String url = "https://www.themealdb.com/images/ingredients/" + name + "-Small.png";
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.app_logo)
                .error(R.drawable.app_logo)
                .into(holder.ingredientImage);

        holder.itemCard.setOnClickListener(v -> {
            mealRepository.filterByIngredient(name, new NetworkCallBack<List<Meal>>() {
                @Override
                public void onSuccessResult(List<Meal> meals) {
                    if (meals.isEmpty()) {
                        Toast.makeText(context, "No meals for " + name, Toast.LENGTH_SHORT).show();
                    } else {
                        //navigateToIngredientFragment(meals);
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
                    ingredient.getDescription() != null
                            ? ingredient.getDescription()
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
        List<Meal> fullMeals = new ArrayList<>();
        int total = simpleMeals.size();

        for (Meal m : simpleMeals) {
            mealRepository.getMealById(m.getId(), new NetworkCallBack<List<Meal>>() {
                @Override
                public void onSuccessResult(List<Meal> detailed) {
                    if (!detailed.isEmpty()) {
                        Meal detailedMeal = detailed.get(0);
                        for (FavoriteMeal favMeal : favoriteMeals) {
                            if (favMeal.getId().equals(detailedMeal.getId())) {
                                detailedMeal.setFavorite(true);
                                break;
                            }
                        }
                        synchronized (fullMeals) {
                            fullMeals.add(detailedMeal);
                            if (fullMeals.size() == total) {
                                navigateToIngredientFragment(fullMeals);
                            }
                        }
                    }
                }

                @Override
                public void onFailureResult(String message) {
                    synchronized (fullMeals) {
                        if (fullMeals.size() == total) {
                            navigateToIngredientFragment(fullMeals);
                        }
                    }
                }

                @Override
                public void onLoading() {}

                @Override
                public void onNetworkError(String errorMessage) {
                    synchronized (fullMeals) {
                        if (fullMeals.size() == total) {
                            navigateToIngredientFragment(fullMeals);
                        }
                    }
                }

                @Override
                public void onEmptyData() {
                    synchronized (fullMeals) {
                        if (fullMeals.size() == total) {
                            navigateToIngredientFragment(fullMeals);
                        }
                    }
                }

                @Override
                public void onProgress(int progress) {}
            });
        }
    }

    private void navigateToIngredientFragment(List<Meal> meals) {
        Bundle bundle = new Bundle();
        Meal[] mealsArray = meals.toArray(new Meal[0]);
        bundle.putSerializable("meals", mealsArray);
        NavController nav = Navigation.findNavController(
                (Activity) context,
                R.id.nav_host_fragment_content_app_screen
        );
        nav.navigate(R.id.nav_meals, bundle);
    }


    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    static class IngredientViewHolder extends RecyclerView.ViewHolder {
        ImageView ingredientImage;
        TextView ingredientInfo;
        CardView itemCard;
        IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientImage = itemView.findViewById(R.id.itemImage);
            ingredientInfo = itemView.findViewById(R.id.itemName);
            itemCard=itemView.findViewById(R.id.itemCard);
        }
    }
}