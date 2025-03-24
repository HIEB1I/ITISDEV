package com.mobdeve.sustainabite;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class community extends AppCompatActivity {

    private RecyclerView recyclerView;
    private IngProcAdapter ingProcAdapter;
    private List<FoodItem> foodList;
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        dbManager = new DBManager();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        foodList = new ArrayList<>();
        ingProcAdapter = new IngProcAdapter(this, foodList);
        recyclerView.setAdapter(ingProcAdapter);

        fetchRecipesFromFirestore(); // Fetch data from Firestore

        Button addRecipeButton = findViewById(R.id.addRecipeButton);
        addRecipeButton.setOnClickListener(v -> {
            Intent intent = new Intent(community.this, AddRecipeActivity.class);
            startActivity(intent);
        });
    }

    private void fetchRecipesFromFirestore() {
        dbManager.fetchRecipes(new DBManager.OnRecipesFetchedListener() {
            @Override
            public void onRecipesFetched(List<FoodItem> recipes) {
                foodList.clear();
                foodList.addAll(recipes);
                ingProcAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Exception e) {
                Log.e("Firestore", "Error fetching recipes", e);
            }
        });
    }

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
