package com.example.farakhni.ui.home;
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
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.farakhni.R;
import com.example.farakhni.model.Ingredient;
import com.example.farakhni.model.Meal;
import com.example.farakhni.data.network.NetworkCallBack;
import com.example.farakhni.data.network.meal.MealsRemoteDataSourceImpl;
import com.example.farakhni.IngredientFragment;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private final Context context;
    private final List<Ingredient> ingredientList;

    public IngredientAdapter(Context ctx, List<Ingredient> list) {
        this.context = ctx;
        this.ingredientList = list;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.ingredient_item, parent, false);
        return new IngredientViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int pos) {
        Ingredient ingredient = ingredientList.get(pos);
        String name = ingredient.getIngredient();
        holder.ingredientInfo.setText(name);
        String url = "https://www.themealdb.com/images/ingredients/" + name + "-Small.png";
        Glide.with(context).load(url).into(holder.ingredientImage);

        // Handle click to fetch meals
        holder.ingredientImage.setOnClickListener(v -> {
            MealsRemoteDataSourceImpl.getInstance().makeNetworkCallFilterByIngredient(name, new NetworkCallBack<List<Meal>>() {
                @Override
                public void onSuccessResult(List<Meal> simpleMeals) {
                    if (simpleMeals.isEmpty()) {
                        Toast.makeText(context, "No meals found for “" + name + "”", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    fetchDetailsAndNavigate(simpleMeals);
                }

                @Override
                public void onFailureResult(String error) {
                    Toast.makeText(context, "Error: " + error, Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Handle long press (popup showing ingredient description)
        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            PopupWindow popup;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        View popupView = LayoutInflater.from(context)
                                .inflate(R.layout.popup_layout, null, false);
                        TextView desc = popupView.findViewById(R.id.popupIngredientDescription);
                        desc.setText(ingredient.getDescription() != null ? ingredient.getDescription() : "No description available.");

                        popupView.measure(
                                View.MeasureSpec.UNSPECIFIED,
                                View.MeasureSpec.UNSPECIFIED
                        );

                        popup = new PopupWindow(
                                popupView,
                                popupView.getMeasuredWidth(),
                                popupView.getMeasuredHeight(),
                                true
                        );
                        popup.setOutsideTouchable(true);
                        popup.setClippingEnabled(true);
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
            MealsRemoteDataSourceImpl.getInstance().makeNetworkCallgetMealByID(m.getId(), new NetworkCallBack<List<Meal>>() {
                @Override
                public void onSuccessResult(List<Meal> detailed) {
                    if (!detailed.isEmpty()) {
                        fullMeals.add(detailed.get(0));
                    }
                    if (fullMeals.size() == total) {
                        navigateToIngredientFragment(fullMeals);
                    }
                }

                @Override
                public void onFailureResult(String error) {
                    if (fullMeals.size() == total) {
                        navigateToIngredientFragment(fullMeals);
                    }
                }
            });
        }
    }

    private void navigateToIngredientFragment(List<Meal> meals) {
        IngredientFragment fragment = new IngredientFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("meals", (Serializable) meals);
        fragment.setArguments(bundle);
        FragmentActivity activity = (FragmentActivity) context;
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment_content_app_screen, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    static class IngredientViewHolder extends RecyclerView.ViewHolder {
        ImageView ingredientImage;
        TextView ingredientInfo;

        IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientImage = itemView.findViewById(R.id.ingredientImage);
            ingredientInfo = itemView.findViewById(R.id.ingredientName);
        }
    }
}
