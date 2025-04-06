package com.mobdeve.sustainabite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class welcomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
    }

    public void goHome(View view) {
        Intent intent = new Intent(this, welcomePage.class);
        startActivity(intent);
    }

    //added this in order to have goLogin on welcomePage.java
    public void goLogin(View view) {
        // Start your login activity
        Intent intent = new Intent(this, logIn.class); // Assuming logIn is the login activity
        startActivity(intent);
    }

}
