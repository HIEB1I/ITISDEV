package com.mobdeve.sustainabite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class logIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void goWelcome(View view) {
        Intent intent = new Intent(this, welcomePage.class);
        startActivity(intent);
    }

}
