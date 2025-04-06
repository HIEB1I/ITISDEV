package com.mobdeve.sustainabite;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class signUp extends AppCompatActivity {

    private EditText password1, password2, username, email, editProfile;
    private ImageView Button1, Button2;
    private ImageView image;  // ✅ Added missing initialization
    private boolean isPasswordVisible = false;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Bitmap selectedBitmap;

    DBManager dbManager = new DBManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        password1 = findViewById(R.id.editPassword);
        editProfile = findViewById(R.id.editProfile);
        password2 = findViewById(R.id.editConfirmPassword);
        username = findViewById(R.id.editTextUsername);
        email = findViewById(R.id.editTextEmail);
        Button1 = findViewById(R.id.NoViewPass_1);
        Button2 = findViewById(R.id.NoViewPass2);
        image= findViewById(R.id.imageButton);

        image.setOnClickListener(v -> openGallery());
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
                String fileName = getFileName(imageUri);
                editProfile.setText(fileName);
            } catch (IOException e) {
                Log.e("SignUpActivity", "Error loading image", e);
            }
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (index != -1) {
                        result = cursor.getString(index);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }


    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private Bitmap base64ToBitmap(String base64String) {
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public void goSignUp(View view) {
        String userName = username.getText().toString();
        String userEmail = email.getText().toString();
        String userPassword1 = password1.getText().toString();
        String userPassword2 = password2.getText().toString();

        String imageString = selectedBitmap != null ? bitmapToBase64(selectedBitmap) : "";

        Log.d("SignUpDebug", "Username: " + userName);
        Log.d("SignUpDebug", "Email: " + userEmail);
        Log.d("SignUpDebug", "Password: " + userPassword2);
        Log.d("SignUpDebug", "Image String Length: " + imageString.length());

        if (userPassword1.equals(userPassword2)) {
            dbManager.putNewUser(userName, userPassword2, userEmail, imageString);

            Toast.makeText(this, "Sign-up successful!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, welcomePage.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Passwords do not match. Please try again.", Toast.LENGTH_SHORT).show();
        }
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

    public void goLogin(View view) {
        Intent intent = new Intent(this, logIn.class);
        startActivity(intent);
    }
}
