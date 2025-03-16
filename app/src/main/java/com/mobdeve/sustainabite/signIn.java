package com.mobdeve.sustainabite;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class signIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
    }

    public void goSignup(View view) {
        Intent intent = new Intent(this, signUp.class);
        startActivity(intent);
    }

/*
    public void goHome(View v) {
        navigateTo(Home.class);
    }

    public void goFood(View v) {
        navigateTo(FoodManagement.class);
    }

    public void goCommunity(View v) {
        navigateTo(Community.class);
    }

    public void goProfile(View v) {
        navigateTo(Profile.class);
    }

    private void navigateTo(Class<?> targetActivity) {
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent);
    }

 */
}
