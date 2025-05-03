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
import com.example.farakhni.model.Ingredient;
import com.example.farakhni.model.Meal;

import java.util.ArrayList;
import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {
    private final Context context;
    private  List<Ingredient> ingredientList;
    private final MealRepository mealRepository;
    private List<Meal> favoriteMeals = new ArrayList<>();


    public void setFavoriteMeals(List<Meal> favMeals) {
        favoriteMeals.clear();
        if (favMeals != null) {
            favoriteMeals.addAll(favMeals);
        }
    }
    public IngredientAdapter(Context context, List<Ingredient> list) {
        this.context = context;
        this.ingredientList = list;
        this.mealRepository = MealRepositoryImpl.getInstance(context);
    }
    public void setIngredientList(List<Ingredient> newList) {
        this.ingredientList.clear();
        this.ingredientList.addAll(newList);
        notifyDataSetChanged();
    }
    @NonNull @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.card_item, parent, false);
        return new IngredientViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient ingredient = ingredientList.get(position);
        String name = ingredient.getIngredient();
        holder.ingredientInfo.setText(name);

        String url = "https://www.themealdb.com/images/ingredients/" + name + "-Small.png";
        Glide.with(context).load(url).into(holder.ingredientImage);

        holder.ingredientImage.setOnClickListener(v -> {
            // fetch by ingredient
            mealRepository.filterByIngredient(name, new NetworkCallBack<List<Meal>>() {
                @Override public void onSuccessResult(List<Meal> meals) {
                    if (meals.isEmpty()) {
                        Toast.makeText(context, "No meals for " + name, Toast.LENGTH_SHORT).show();
                    } else {
                        fetchDetailsAndNavigate(meals);
                    }
                }
                @Override public void onFailureResult(String message) {
                    Toast.makeText(context, "Error: " + message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onLoading() {

                }

                @Override
                public void onNetworkError(String errorMessage) {

                }

                @Override
                public void onEmptyData() {

                }

                @Override
                public void onProgress(int progress) {

                }
            });
        });

        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            PopupWindow popup;
            @Override public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    View popupView = LayoutInflater.from(context)
                            .inflate(R.layout.popup_layout, null, false);
                    TextView desc = popupView.findViewById(R.id.popupIngredientDescription);
                    desc.setText(ingredient.getDescription() != null
                            ? ingredient.getDescription()
                            : "No description available.");
                    popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                    popup = new PopupWindow(popupView,
                            popupView.getMeasuredWidth(),
                            popupView.getMeasuredHeight(),
                            true);
                    popup.setOutsideTouchable(true);
                    popup.showAsDropDown(v, 0,
                            -v.getHeight() - popupView.getMeasuredHeight());
                    return true;
                } else if (popup != null && popup.isShowing()) {
                    popup.dismiss();
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
                        fullMeals.add(detailedMeal);
                    }
                    if (fullMeals.size() == total) {
                        navigateToIngredientFragment(fullMeals);
                    }
                }
                @Override public void onFailureResult(String message) {
                    if (fullMeals.size() == total) {
                        navigateToIngredientFragment(fullMeals);
                    }
                }
                @Override
                public void onLoading() {

                }

                @Override
                public void onNetworkError(String errorMessage) {

                }
                @Override
                public void onEmptyData() {

                }
                @Override
                public void onProgress(int progress) {

                }
            });
        }
    }

    private void navigateToIngredientFragment(List<Meal> meals) {
        Bundle bundle = new Bundle();
        // nav-graph arg “meals” expects a Meal[]
        bundle.putSerializable("meals", meals.toArray(new Meal[0]));

        NavController navController = Navigation.findNavController(
                (Activity) context,
                R.id.nav_host_fragment_content_app_screen
        );
        navController.navigate(R.id.nav_meals, bundle);
    }

    @Override public int getItemCount() {
        return ingredientList.size();
    }

    static class IngredientViewHolder extends RecyclerView.ViewHolder {
        ImageView ingredientImage;
        TextView  ingredientInfo;
        IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientImage = itemView.findViewById(R.id.itemImage);
            ingredientInfo  = itemView.findViewById(R.id.itemName);
        }
    }
}
