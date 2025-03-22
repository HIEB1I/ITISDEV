package com.mobdeve.sustainabite;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Handler to delay the transition to the sign-in screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, signIn.class);
                startActivity(intent);
                finish(); // Close the splash screen to prevent going back to it
            }
        }, 3000); // 3000 ms = 3 seconds
    }

}
