package com.mobdeve.sustainabite;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailsActivity extends AppCompatActivity {

    private TextView foodName, ingContent, procContent, foodKcal;
    private ImageView foodImage;
    private Button editButton;

    private FoodItem foodItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        foodImage = findViewById(R.id.foodImage);
        foodName = findViewById(R.id.foodName);
        ingContent = findViewById(R.id.ing_content);
        procContent = findViewById(R.id.procedures_content);
        foodKcal = findViewById(R.id.foodKcal);
        editButton = findViewById(R.id.editButton);

        foodItem = (FoodItem) getIntent().getSerializableExtra("foodItem");

        if (foodItem != null) {
            foodName.setText(foodItem.getName());
            ingContent.setText(foodItem.getIngredients());
            procContent.setText(foodItem.getProcedures());

            if (foodItem.getKcal() != null && !foodItem.getKcal().isEmpty()) {
                foodKcal.setText(foodItem.getKcal() + " kcal");
            } else {
                foodKcal.setText("No kcal data available");
            }

            Bitmap bitmap = DBManager.decodeBase64ToBitmap(foodItem.getImageString());
            if (bitmap != null) {
                foodImage.setImageBitmap(bitmap);
            } else {
                foodImage.setImageResource(R.drawable.banana);
            }
        }


        editButton.setOnClickListener(v -> showEditDialog());
    }

    private void showEditDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.edit_recipe, null);

        EditText editFoodName = dialogView.findViewById(R.id.edit_foodName);
        EditText editIngContent = dialogView.findViewById(R.id.edit_ing_content);
        EditText editProcContent = dialogView.findViewById(R.id.edit_procedures_content);
        EditText editFoodKcal = dialogView.findViewById(R.id.edit_foodKcal);

        editFoodName.setText(foodItem.getName());
        editIngContent.setText(foodItem.getIngredients());
        editProcContent.setText(foodItem.getProcedures());
        editFoodKcal.setText(foodItem.getKcal());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Recipe")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    foodName.setText(editFoodName.getText().toString());
                    ingContent.setText(editIngContent.getText().toString());
                    procContent.setText(editProcContent.getText().toString());
                    foodKcal.setText(editFoodKcal.getText().toString());
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.yellow_rounded_box);
        dialog.show();
    }
}
