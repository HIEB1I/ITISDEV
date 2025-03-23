package com.mobdeve.sustainabite;

import android.content.Context;
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
        holder.foodImage.setImageResource(food.getImage());
        holder.foodName.setText(food.getName());
        holder.foodKcal.setText(food.getKcal());

        // Button Click - Open Details Acti1vity
        holder.detailsButton.setOnClickListener(v -> {
//            Intent intent = new Intent(context, DetailsActivity.class);
//            intent.putExtra("foodItem", food);
//            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }
}
