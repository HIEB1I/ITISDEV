package com.mobdeve.sustainabite;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private List<FoodItem> foodList;
    private Context context;

    public FoodAdapter(Context context, List<FoodItem> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView foodName, foodKcal;
        Button detailsButton;

        public FoodViewHolder(View view) {
            super(view);
            foodImage = view.findViewById(R.id.foodImage);
            foodName = view.findViewById(R.id.foodName);
            foodKcal = view.findViewById(R.id.foodKcal);
            detailsButton = view.findViewById(R.id.detailsButton);
        }
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodItem food = foodList.get(position);

        if (food.getImageResId() != null) {
            holder.foodImage.setImageResource(food.getImageResId());
        } else if (food.getImageString() != null && !food.getImageString().isEmpty()) {
            // Convert Base64 string to Bitmap and set to ImageView
            Bitmap bitmap = DBManager.decodeBase64ToBitmap(food.getImageString());
            holder.foodImage.setImageBitmap(bitmap);
        } else {
            holder.foodImage.setImageResource(R.drawable.banana);
        }

        holder.foodName.setText(food.getName());
        holder.foodKcal.setText(food.getKcal());

        // Button Click - Open Details Activity
        holder.detailsButton.setOnClickListener(v -> {
            // Implement opening details
        });
    }


    @Override
    public int getItemCount() {
        return foodList.size();
    }
}
