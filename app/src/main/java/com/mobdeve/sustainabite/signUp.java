package com.mobdeve.sustainabite;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class signUp extends AppCompatActivity {

    private EditText password1;
    private EditText password2;
    private ImageView Button1;
    private ImageView Button2;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        password1 = findViewById(R.id.editPassword);
        password2 = findViewById(R.id.editConfirmPassword);
        Button1 = findViewById(R.id.NoViewPass);
        Button2 = findViewById(R.id.NoViewPass2);

        Button1.setOnClickListener(v -> togglePasswordVisibility(password1, Button1));
        Button2.setOnClickListener(v -> togglePasswordVisibility(password2, Button2));
    }

    public void goLogin(View view) {
        Intent intent = new Intent(this, logIn.class);
        startActivity(intent);
    }

    private void togglePasswordVisibility(EditText passwordField, ImageView button) {
        if (isPasswordVisible) {
            passwordField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            button.setImageResource(R.drawable.no_view_pass);
        } else {
            passwordField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            button.setImageResource(R.drawable.view_pass);
        }

        isPasswordVisible = !isPasswordVisible; // Toggle the flag
        passwordField.setSelection(passwordField.getText().length()); // Keep cursor at the end
    }
}
