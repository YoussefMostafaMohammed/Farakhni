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
import com.example.farakhni.model.Area;
import com.example.farakhni.model.FavoriteMeal;
import com.example.farakhni.model.Meal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.AreaViewHolder> {
    private final Context context;
    private final List<Area> areaList;
    private final MealRepository mealRepository;
    private final List<FavoriteMeal> favoriteMeals;
    private final Map<String, String> areaToCountryCodeMap = new HashMap<String, String>() {{
        put("American", "US");
        put("British", "GB");
        put("Canadian", "CA");
        put("Chinese", "CN");
        put("Croatian", "HR");
        put("Dutch", "NL");
        put("Egyptian", "EG");
        put("French", "FR");
        put("Greek", "GR");
        put("Indian", "IN");
        put("Irish", "IE");
        put("Italian", "IT");
        put("Jamaican", "JM");
        put("Japanese", "JP");
        put("Kenyan", "KE");
        put("Malaysian", "MY");
        put("Mexican", "MX");
        put("Moroccan", "MA");
        put("Polish", "PL");
        put("Portuguese", "PT");
        put("Russian", "RU");
        put("Spanish", "ES");
        put("Thai", "TH");
        put("Tunisian", "TN");
        put("Turkish", "TR");
        put("Vietnamese", "VN");
    }};

    private String getFlagUrlForArea(String areaName) {
        String code = areaToCountryCodeMap.get(areaName);
        return code != null ? "https://flagcdn.com/w320/" + code.toLowerCase() + ".png" : null;
    }

    public AreaAdapter(Context context, List<Area> areaList) {
        this.context = context;
        this.areaList = areaList != null ? new ArrayList<>(areaList) : new ArrayList<>();
        this.mealRepository = MealRepositoryImpl.getInstance(context);
        this.favoriteMeals = new ArrayList<>();
    }

    public void updateAreas(List<Area> newAreas) {
        areaList.clear();
        if (newAreas != null) {
            areaList.addAll(newAreas);
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
    public AreaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_item, parent, false);
        return new AreaViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull AreaViewHolder holder, int position) {
        Area area = areaList.get(position);
        String areaName = area.getArea();
        holder.areaName.setText(areaName != null ? areaName : "Unknown Area");

        String flagUrl = getFlagUrlForArea(areaName);
        if (flagUrl != null) {
            Glide.with(context)
                    .load(flagUrl)
                    .placeholder(R.drawable.app_logo)
                    .error(R.drawable.app_logo)
                    .into(holder.areaImage);
        } else {
            holder.areaImage.setImageResource(R.drawable.app_logo);
        }

        holder.itemCard.setOnClickListener(v -> {
            mealRepository.filterByArea(areaName, new NetworkCallBack<List<Meal>>() {
                @Override
                public void onSuccessResult(List<Meal> meals) {
                    if (meals.isEmpty()) {
                        Toast.makeText(context, "No meals for " + areaName, Toast.LENGTH_SHORT).show();
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
            desc.setText("It is a Country.");

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
                                navigateToAreaFragment(fullMeals);
                            }
                        }
                    }
                }

                @Override
                public void onFailureResult(String message) {
                    synchronized (fullMeals) {
                        if (fullMeals.size() == total) {
                            navigateToAreaFragment(fullMeals);
                        }
                    }
                }

                @Override
                public void onLoading() {}

                @Override
                public void onNetworkError(String errorMessage) {
                    synchronized (fullMeals) {
                        if (fullMeals.size() == total) {
                            navigateToAreaFragment(fullMeals);
                        }
                    }
                }

                @Override
                public void onEmptyData() {
                    synchronized (fullMeals) {
                        if (fullMeals.size() == total) {
                            navigateToAreaFragment(fullMeals);
                        }
                    }
                }

                @Override
                public void onProgress(int progress) {}
            });
        }
    }

    private void navigateToAreaFragment(List<FavoriteMeal> meals) {
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
        return areaList.size();
    }

    public static class AreaViewHolder extends RecyclerView.ViewHolder {
        ImageView areaImage;
        TextView areaName;
        CardView itemCard;

        public AreaViewHolder(@NonNull View itemView) {
            super(itemView);
            areaImage = itemView.findViewById(R.id.itemImage);
            areaName = itemView.findViewById(R.id.itemName);
            itemCard=itemView.findViewById(R.id.itemCard);

        }
    }
}