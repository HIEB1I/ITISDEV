package com.mobdeve.sustainabite;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Blob;

public class signUp extends AppCompatActivity {

    private EditText password1;
    private EditText password2;
    private ImageView Button1, imageButton;
    private ImageView Button2;
    private boolean isPasswordVisible = false;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Bitmap selectedBitmap;

    private EditText username;
    private EditText email;
    private EditText image;

    DBManager dbManager = new DBManager();
    private Uri imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        password1 = findViewById(R.id.editPassword);
        password2 = findViewById(R.id.editConfirmPassword);
        Button1 = findViewById(R.id.NoViewPass);
        Button2 = findViewById(R.id.NoViewPass2);

        username = findViewById(R.id.editTextUsername);
        email = findViewById(R.id.editTextEmail);
        image = findViewById(R.id.editProfile);


        imageButton.setOnClickListener(v -> openGallery());

        Button1.setOnClickListener(v -> togglePasswordVisibility(password1, Button1));
        Button2.setOnClickListener(v -> togglePasswordVisibility(password2, Button2));
    }


    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                Log.e("AccountEditProfile", "Error loading image", e);
            }
        }
    }

    private byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public void goLogin(View view) {
        Intent intent = new Intent(this, logIn.class);
        startActivity(intent);
    }

    public void goSignUp(View view){
        String userName = username.getText().toString();
        String userEmail = email.getText().toString();
        String userPassword = password2.getText().toString();


        String imageString = selectedBitmap != null ? bitmapToBase64(selectedBitmap) : null;

        dbManager.putNewUser(userName,userPassword, userEmail, userImage);

        Toast.makeText(this, "Sign-up successful!", Toast.LENGTH_SHORT).show();
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
