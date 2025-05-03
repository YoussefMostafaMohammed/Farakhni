package com.example.farakhni.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.farakhni.R;
import com.example.farakhni.model.Ingredient;
import java.util.List;

public class IngredientDetailsAdapter extends RecyclerView.Adapter<IngredientDetailsAdapter.IngredientDetailsViewHolder> {
    private Context context;
    private List<Ingredient> ingredientList;
    public IngredientDetailsAdapter(Context context, List<Ingredient> ingredientList) {
        this.context = context;
        this.ingredientList = ingredientList;
    }

    @NonNull
    @Override
    public IngredientDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ingredient_details_item, parent, false);
        return new IngredientDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientDetailsViewHolder holder, int position) {
        Ingredient ingredient = ingredientList.get(position);
        String name = (ingredient.getIngredient() != null ? ingredient.getIngredient() : "Unknown");
        String measure = (ingredient.getIngredient() != null ? ingredient.getMeasure() : "Unknown");
        holder.ingredientName.setText(name);
        holder.ingredientMeasure.setText(measure);
        String imageUrl = "https://www.themealdb.com/images/ingredients/" + ingredient.getIngredient() + "-Small.png";
        Glide.with(context)
                .load(imageUrl)
                .into(holder.ingredientImage);
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
                        popup.setOutsideTouchable(false);
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

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public static class IngredientDetailsViewHolder extends RecyclerView.ViewHolder {
        ImageView ingredientImage;
        TextView ingredientName;
        TextView ingredientMeasure;
        public IngredientDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientImage = itemView.findViewById(R.id.ingredientImage);
            ingredientName = itemView.findViewById(R.id.ingredientName);
            ingredientMeasure = itemView.findViewById(R.id.ingredientMeasure);

        }
    }
}