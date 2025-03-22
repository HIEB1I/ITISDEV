package com.mobdeve.sustainabite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        foodList = new ArrayList<>();
        foodList.add(new FoodItem(R.drawable.fried_rice, "Roasted Chicken", "Kcal 200", "", ""));
        foodList.add(new FoodItem(R.drawable.spinach_omelette, "Chicken Alfredo", "Kcal 198", "", ""));
        foodList.add(new FoodItem(R.drawable.yellow_bg, "Pork Sisig", "Kcal 184", "", ""));
        foodList.add(new FoodItem(R.drawable.green_rounded_button, "Lechon Manok", "Kcal 243", "", ""));

        foodAdapter = new FoodAdapter(this, foodList);
        recyclerView.setAdapter(foodAdapter);
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
