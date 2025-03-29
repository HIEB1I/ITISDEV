package com.mobdeve.sustainabite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class home extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FoodAdapter foodAdapter;
    private List<FoodItem> foodList;

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
      boolean isLoggedIn = prefs.getBoolean("IS_LOGGED_IN", false);
        Log.d("UserPrefs", "User ID: " + userId);
        Log.d("UserPrefs", "Email: " + email);
        Log.d("UserPrefs", "Name: " + name);
        Log.d("UserPrefs", "Image URL: " + image);
        Log.d("UserPrefs", "Is Logged In: " + isLoggedIn);*/

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        foodList = new ArrayList<>();
        foodList.add(new FoodItem(R.drawable.fried_rice, "Roasted Chicken", "Kcal 200", "", ""));
        foodList.add(new FoodItem(R.drawable.spinach_omelette, "Chicken Alfredo", "Kcal 198", "", ""));
        foodList.add(new FoodItem(R.drawable.yellow_bg, "Pork Sisig", "Kcal 184", "", ""));
        foodList.add(new FoodItem(R.drawable.green_rounded_button, "Lechon Manok", "Kcal 243", "", ""));

        foodAdapter = new FoodAdapter(this, foodList);
        recyclerView.setAdapter(foodAdapter);

        ImageView notifIcon = findViewById(R.id.notif_icon);
        notifIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, NotificationsActivity.class);
                startActivity(intent);
            }
        });
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
