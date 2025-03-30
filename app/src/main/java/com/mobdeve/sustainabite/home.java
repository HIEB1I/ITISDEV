package com.mobdeve.sustainabite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class home extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FoodAdapter foodAdapter;
    private List<FoodItem> foodList;
    DBManager dbManager = new DBManager();


    /*NEWWW*/
    private RecyclerView searchResultsRecyclerView;
    private SearchResultsAdapter searchResultsAdapter;
    private List<SearchResult> searchResults;
    private EditText searchBar;
    private ImageView searchIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        String userId = prefs.getString("USER_ID", "No ID");
        String email = prefs.getString("USER_EMAIL", "No Email");
        String name = prefs.getString("USER_NAME", "No Name");
        String image = prefs.getString("USER_IMAGE", "No Image");

        Log.d("UserPrefs", "Name: " + name);
        Log.d("UserPrefs", "User ID: " + userId);

      /*
        Log.d("UserPrefs", "User ID: " + userId);
        Log.d("UserPrefs", "Email: " + email);
        Log.d("UserPrefs", "Name: " + name);
        Log.d("UserPrefs", "Image URL: " + image);
        Log.d("UserPrefs", "Is Logged In: " + isLoggedIn);*/



        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        foodList = new ArrayList<>();

        dbManager.getFoodHome(new DBManager.FoodDataCallback() {
            @Override
            public void onFoodDataRetrieved(ArrayList<FoodItem> foodList) {
                foodAdapter = new FoodAdapter(home.this, foodList);
                recyclerView.setAdapter(foodAdapter);
            }
        });

        /*NEWWW*/
        searchBar = findViewById(R.id.search_bar);
        searchIcon = findViewById(R.id.search_icon);

        searchResultsRecyclerView = findViewById(R.id.searchResultsRecyclerView);
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchResults = new ArrayList<>();

        searchResultsAdapter = new SearchResultsAdapter(this, searchResults, item -> {
            Toast.makeText(home.this, "Clicked: " + item, Toast.LENGTH_SHORT).show();
            // You can navigate to another activity or show more details here
        });
                searchResultsRecyclerView.setAdapter(searchResultsAdapter);
                searchResultsRecyclerView.setVisibility(View.GONE);

        searchIcon.setOnClickListener(v -> performSearch());
    }


    private void performSearch() {
        String query = searchBar.getText().toString().trim();

        if (query.isEmpty()) {
            Toast.makeText(this, "Enter a search term", Toast.LENGTH_SHORT).show();
            return;
        }

        searchResults.clear();

        dbManager.finder(query, new DBManager.RecipeCallback() {
            @Override
            public void onRecipeRetrieved(List<String> foundSearch) {
                if (foundSearch.isEmpty()) {
                    foundSearch.add("N/A");
                }

                searchResults.add(new SearchResult("Search Result", foundSearch));

                searchResultsAdapter.notifyDataSetChanged();
                searchResultsRecyclerView.setVisibility(View.VISIBLE);
            }
        });

        // Handle individual search result clicks
        searchResultsAdapter = new SearchResultsAdapter(this, searchResults, item -> {
            Toast.makeText(home.this, "Clicked: " + item, Toast.LENGTH_SHORT).show();
            // Add action (e.g., navigate to details page)
        });

        searchResultsRecyclerView.setAdapter(searchResultsAdapter);
    }



    /*NAVIGATIONS*/
    public void goHome(View view) {
        Intent intent = new Intent(this, home.class);
        startActivity(intent);
    }

    public void goFood(View view) {
        Intent intent = new Intent(this, foodManagement.class);
        startActivity(intent);
    }

    public void goCommunity(View view) {
        Intent intent = new Intent(this, community.class);
        startActivity(intent);
    }

    public void goProfile(View view) {
        Intent intent = new Intent(this, profile.class);
        startActivity(intent);
    }

}
