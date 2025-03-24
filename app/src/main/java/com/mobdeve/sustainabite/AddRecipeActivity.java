package com.mobdeve.sustainabite;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class AddRecipeActivity extends AppCompatActivity {

    private EditText recipeNameInput, procedureInput, pictureInput;
    private Button addRecipeButton, addIngredientButton;
    private LinearLayout ingredientLayout;
    private List<EditText> ingredientInputs = new ArrayList<>();
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        dbManager = new DBManager();

        recipeNameInput = findViewById(R.id.recipeNameInput);
        procedureInput = findViewById(R.id.procedureInput);
        pictureInput = findViewById(R.id.pictureInput);
        addRecipeButton = findViewById(R.id.addRecipeButton);
        addIngredientButton = findViewById(R.id.addIngredientButton);
        ingredientLayout = findViewById(R.id.ingredientLayout); // Container for ingredient inputs

        addIngredientButton.setOnClickListener(v -> addIngredientField());

        addRecipeButton.setOnClickListener(v -> addRecipe());

        // Add an initial ingredient field
        addIngredientField();
    }

    private void addIngredientField() {
        EditText newIngredientInput = new EditText(this);
        newIngredientInput.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT // Adjusts height dynamically
        ));
        newIngredientInput.setHint("Enter an ingredient");
        newIngredientInput.setPadding(10, 10, 10, 10);
        newIngredientInput.setBackgroundResource(android.R.drawable.editbox_background);
        newIngredientInput.setTextSize(16); // Match other fields
        newIngredientInput.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        newIngredientInput.setInputType(InputType.TYPE_CLASS_TEXT);

        ingredientLayout.addView(newIngredientInput);
        ingredientInputs.add(newIngredientInput);
    }


    private void addRecipe() {
        String rName = recipeNameInput.getText().toString().trim();
        String rProcedure = procedureInput.getText().toString().trim();
        String rImage = pictureInput.getText().toString().trim();

        if (rName.isEmpty() || rProcedure.isEmpty() || ingredientInputs.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Collect ingredients and format them with commas
        StringBuilder ingredientsBuilder = new StringBuilder();
        for (EditText ingredientInput : ingredientInputs) {
            String ingredient = ingredientInput.getText().toString().trim();
            if (!ingredient.isEmpty()) {
                if (ingredientsBuilder.length() > 0) {
                    ingredientsBuilder.append(", ");
                }
                ingredientsBuilder.append(ingredient);
            }
        }
        String rIngredients = ingredientsBuilder.toString();

        // Save recipe to Firestore
        dbManager.addRecipeToFirestore(this, rImage, rIngredients, rName, rProcedure, new DBManager.OnRecipeAddedListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(AddRecipeActivity.this, "Recipe Added Successfully!", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK, new Intent().putExtra("recipe_added", true));
                finish();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(AddRecipeActivity.this, "Failed to add recipe: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
