package com.mobdeve.sustainabite;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class logIn extends AppCompatActivity {

    private EditText Email, Password;
    private Button logInButton;
    private boolean isPasswordVisible = false;
    private ImageView Button1;

    DBManager dbManager = new DBManager();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Email = findViewById(R.id.editTextEmail);
        Password = findViewById(R.id.editPassword);
        logInButton = findViewById(R.id.roundedButton);
        Button1 = findViewById(R.id.NoViewPass_1 );
        Button1.setOnClickListener(v -> togglePasswordVisibility(Password, Button1));

        logInButton.setOnClickListener(v -> LogIn());
    }

    public void LogIn() {
        String UEmail = Email.getText().toString();
        String UPass = Password.getText().toString();

        dbManager.checkUser(this, UEmail, UPass, new DBManager.OnCheckUserListener() {
            @Override
            public void onResult(boolean exists) {
                if (exists) {
                    Intent intent = new Intent(logIn.this, home.class);
                    SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                    startActivity(intent);

                } else {
                    Toast.makeText(logIn.this, "Invalid email or password.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void togglePasswordVisibility(EditText passwordField, ImageView button) {
        if (isPasswordVisible) {
            passwordField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            button.setImageResource(R.drawable.no_view_pass);
        } else {
            passwordField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            button.setImageResource(R.drawable.view_pass);
        }

        isPasswordVisible = !isPasswordVisible;
        passwordField.setSelection(passwordField.getText().length());
    }


    public void goSignup(View view) {
        Intent intent = new Intent(this, signUp.class);
        startActivity(intent);
    }

}
