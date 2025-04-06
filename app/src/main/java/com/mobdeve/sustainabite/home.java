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
    private ImageView searchIcon, xbutton, notif_icon;

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

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        foodList = new ArrayList<>();

        dbManager.getFoodHome(new DBManager.FoodDataCallback() {
            @Override
            public void onFoodDataRetrieved(ArrayList<FoodHome> foodList) {
                foodAdapter = new FoodAdapter(home.this, foodList);  // Pass context
                recyclerView.setAdapter(foodAdapter);
            }
        });


        searchBar = findViewById(R.id.search_bar);
        searchIcon = findViewById(R.id.search_icon);
        xbutton = findViewById(R.id.xbutton);
        notif_icon = findViewById(R.id.notif_icon);

        notif_icon.setOnClickListener(v -> goNotif(v));


        searchResultsRecyclerView = findViewById(R.id.searchResultsRecyclerView);
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchResults = new ArrayList<>();

                searchResultsRecyclerView.setAdapter(searchResultsAdapter);
                searchResultsRecyclerView.setVisibility(View.GONE);
                xbutton.setVisibility(View.GONE);

        searchIcon.setOnClickListener(v -> performSearch());
        xbutton.setOnClickListener(v -> clearSearchResults());
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
                    foundSearch.add("N/A - No Info");
                }

                for (String result : foundSearch) {
                    String[] parts = result.split(" - ", 2);
                    String name = parts[0];
                    String secondaryInfo = (parts.length > 1) ? parts[1] : "No Info";
                    searchResults.add(new SearchResult(name, secondaryInfo));
                }

                searchResultsAdapter.notifyDataSetChanged();
                searchResultsRecyclerView.setVisibility(View.VISIBLE);
                xbutton.setVisibility(View.VISIBLE);
            }
        });

        searchResultsAdapter = new SearchResultsAdapter(this, searchResults, searchResult -> {
            Log.d("UserPrefs", "SEARCH: " + searchResult);  // Fixed: Removed .getName()

            dbManager.getUserId(searchResult, new DBManager.FirestoreCallback() {
                @Override
                public void onUserIDRetrieved(String resultType) {
                    Intent intent;
                    if (resultType.equals("Food Exists")) {
                        intent = new Intent(home.this, ProductDetailsActivity2.class);
                        intent.putExtra("FOOD_NAME", searchResult);
                    } else {
                        intent = new Intent(home.this, RecipeDetailsActivity2.class);
                        intent.putExtra("RECIPE_ID", searchResult);
                    }
                    startActivity(intent);
                }
            });
        });

        searchResultsRecyclerView.setAdapter(searchResultsAdapter);
    }

    private void clearSearchResults() {
        searchResultsRecyclerView.setVisibility(View.GONE);
        xbutton.setVisibility(View.GONE);
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

    public void goNotif(View view){
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = prefs.getString("USER_ID", "No ID");

        Intent intent = new Intent(this, NotificationsActivity.class);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }

}
