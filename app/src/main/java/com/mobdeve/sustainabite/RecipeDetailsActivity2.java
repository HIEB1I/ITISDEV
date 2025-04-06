package com.mobdeve.sustainabite;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailsActivity2 extends AppCompatActivity {


    private TextView foodName, ingContent, procContent, foodKcal, recipeDetailsOwner;
    private ImageView foodImage;
    private Button editButton, deleteButton;
    private String recipeId;
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details2);

        dbManager = new DBManager();

        foodImage = findViewById(R.id.foodImage);
        foodName = findViewById(R.id.foodName);
        ingContent = findViewById(R.id.ing_content);
        procContent = findViewById(R.id.procedures_content);
        foodKcal = findViewById(R.id.foodKcal);
        //editButton = findViewById(R.id.editButton);
        //deleteButton = findViewById(R.id.deleteButton);
        recipeDetailsOwner = findViewById(R.id.recipeDetailsOwner);

        // Retrieve recipe ID from intent
        recipeId = getIntent().getStringExtra("RECIPE_ID");

        if (recipeId != null) {
            loadRecipeData(recipeId);
        } else {
            Toast.makeText(this, "Error: Recipe not found", Toast.LENGTH_SHORT).show();
            finish();
        }

        //editButton.setOnClickListener(v -> showEditDialog());
        //deleteButton.setOnClickListener(v -> confirmDeleteRecipe());
    }

    private void loadRecipeData(String recipeId) {
        dbManager.getRecipeById(recipeId, new DBManager.OnRecipeLoadedListener() {
            @Override
            public void onSuccess(String name, String ingredients, String procedures, String imageBase64, String kcal, String ownerName) {
                foodName.setText(name);
                ingContent.setText(ingredients);
                procContent.setText(procedures);
                foodKcal.setText(kcal != null && !kcal.isEmpty() ? kcal + " kcal" : "No kcal data available");
                recipeDetailsOwner.setText(ownerName != null ? "@" + ownerName : "Unknown Owner");

                Bitmap bitmap = DBManager.decodeBase64ToBitmap(imageBase64);
                if (bitmap != null) {
                    foodImage.setImageBitmap(bitmap);
                } else {
                    foodImage.setImageResource(R.drawable.banana);
                }
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(RecipeDetailsActivity2.this, "Error loading recipe: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

/*
    private void showEditDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.edit_recipe, null);

        EditText editFoodName = dialogView.findViewById(R.id.edit_foodName);
        EditText editIngContent = dialogView.findViewById(R.id.edit_ing_content);
        EditText editProcContent = dialogView.findViewById(R.id.edit_procedures_content);
        EditText editFoodKcal = dialogView.findViewById(R.id.edit_foodKcal);

        if (foodItem != null) {
            editFoodName.setText(foodItem.getName());
            editIngContent.setText(foodItem.getIngredients());
            editProcContent.setText(foodItem.getProcedures());
            editFoodKcal.setText(foodItem.getKcal());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Recipe")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    String updatedName = editFoodName.getText().toString();
                    String updatedKcal = editFoodKcal.getText().toString();
                    String updatedIngredients = editIngContent.getText().toString();
                    String updatedProcedures = editProcContent.getText().toString();

                    foodName.setText(updatedName);
                    ingContent.setText(updatedIngredients);
                    procContent.setText(updatedProcedures);
                    foodKcal.setText(updatedKcal);

                    DBManager dbManager = new DBManager();
                    dbManager.updateRecipeInFirestore(foodItem.getId(), updatedName, updatedKcal, updatedIngredients, updatedProcedures, new DBManager.OnRecipeListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(RecipeDetailsActivity2.this, "Recipe updated successfully!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(RecipeDetailsActivity2.this, "Failed to update recipe: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.yellow_rounded_box);
        dialog.show();
    }

    private void confirmDeleteRecipe() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Recipe")
                .setMessage("Are you sure you want to delete this recipe?")
                .setPositiveButton("Delete", (dialog, which) -> deleteRecipe())
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void deleteRecipe() {
        if (foodItem == null || foodItem.getId() == null || foodItem.getId().isEmpty()) {
            Toast.makeText(this, "Error: Recipe ID not found", Toast.LENGTH_SHORT).show();
            return;
        }

        DBManager dbManager = new DBManager();
        dbManager.deleteRecipeFromFirestore(foodItem.getId(), new DBManager.OnRecipeListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(RecipeDetailsActivity2.this, "Recipe deleted successfully!", Toast.LENGTH_SHORT).show();

                Intent resultIntent = new Intent();
                resultIntent.putExtra("recipe_deleted", true);
                setResult(RESULT_OK, resultIntent);

                finish();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(RecipeDetailsActivity2.this, "Failed to delete recipe: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /* NAVIGATIONS */
    /* NAVIGATIONS */
    public void goHome(View view) {
        startActivity(new Intent(this, home.class));
    }

    public void goFood(View view) {
        startActivity(new Intent(this, foodManagement.class));
    }

    public void goCommunity(View view) {
        startActivity(new Intent(this, community.class));
    }

    public void goProfile(View view) {
        startActivity(new Intent(this, profile.class));
    }
}
