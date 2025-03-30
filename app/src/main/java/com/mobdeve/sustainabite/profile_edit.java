package com.mobdeve.sustainabite;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;

public class profile_edit extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private TextView userNameTextView, userEmailTextView;
    private ImageView profileImageView;
    private EditText editUsername, editEmail, editPassword, editConfirmPassword;
    private Button btnSaveChanges, btnChangeImage;
    private DBManager dbManager;
    private String userId;
    private String encodedImage = "";

    // Register ActivityResultLauncher for profile updates
    private final ActivityResultLauncher<Intent> profileLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getBooleanExtra("profile_updated", false)) {
                        fetchUserProfile(); // Refresh user details after editing
                    }
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        userNameTextView = findViewById(R.id.sampleUsername);
        userEmailTextView = findViewById(R.id.sampleEmail);
        profileImageView = findViewById(R.id.samplePhoto);

        editUsername = findViewById(R.id.editTextUsername);
        editEmail = findViewById(R.id.editTextEmail);
        editPassword = findViewById(R.id.editPassword);
        editConfirmPassword = findViewById(R.id.editConfirmPassword);
        btnSaveChanges = findViewById(R.id.roundedButton);
        btnChangeImage = findViewById(R.id.editPhoto);

        dbManager = new DBManager();

        // For fetching & displaying current user details
        fetchUserProfile();

        // For editing user details
        dbManager.getCurrentUserDetails(this, new DBManager.OnUserDetailsFetchedListener() {
            @Override
            public void onUserDetailsFetched(String id, String email, String name, String image) {
                userId = id;
                editUsername.setText(name);
                editEmail.setText(email);
                editPassword.setText("");
                editConfirmPassword.setText("");

                if (image != null && !image.isEmpty()) {
                    Bitmap bitmap = DBManager.decodeBase64ToBitmap(image);
                    if (bitmap != null) {
                        profileImageView.setImageBitmap(bitmap);
                    }
                }
            }

            @Override
            public void onError(String message) {
                Toast.makeText(profile_edit.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        btnSaveChanges.setOnClickListener(view -> saveProfileChanges());
        btnChangeImage.setOnClickListener(view -> selectImage());
    }

    // For displaying current user details
    private void fetchUserProfile() {
        dbManager.getCurrentUserDetails(this, new DBManager.OnUserDetailsFetchedListener() {
            @Override
            public void onUserDetailsFetched(String id, String email, String name, String image) {
                userNameTextView.setText(name);
                userEmailTextView.setText(email);

                if (image != null && !image.isEmpty()) {
                    Bitmap bitmap = DBManager.decodeBase64ToBitmap(image);
                    if (bitmap != null) {
                        profileImageView.setImageBitmap(bitmap);
                    }
                }
            }

            @Override
            public void onError(String message) {
                Toast.makeText(profile_edit.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                                getContentResolver(), result.getData().getData());
                        profileImageView.setImageBitmap(bitmap);
                        encodedImage = encodeImage(bitmap);
                    } catch (Exception e) {
                        Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                    }
                }
            });


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                profileImageView.setImageBitmap(bitmap);
                encodedImage = encodeImage(bitmap);
            } catch (Exception e) {
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] byteArray = baos.toByteArray();
        return android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT);
    }

    public void saveProfileChanges() {
        String updatedUsername = editUsername.getText().toString().trim();
        String updatedEmail = editEmail.getText().toString().trim();
        String updatedPassword = editPassword.getText().toString().trim();
        String confirmPassword = editConfirmPassword.getText().toString().trim();

        if (updatedUsername.isEmpty() || updatedEmail.isEmpty()) {
            Toast.makeText(this, "Username and Email cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!updatedPassword.isEmpty() && !updatedPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userId != null && !userId.isEmpty()) {
            dbManager.updateUserProfile(userId, updatedUsername, updatedEmail, updatedPassword, encodedImage, new DBManager.OnUserUpdateListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(profile_edit.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("profile_updated", true);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(profile_edit.this, "Failed to update profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
        }
    }



/* NAVIGATIONS */
    public void goHome(View view) {
        startActivity(new Intent(this, home.class));
    }

    public void goFood(View view) {
        startActivity(new Intent(this, foodManagement.class));
    }

    public void goCommunity(View view) {
        startActivity(new Intent(this, community.class));
    }

    public void goProfile(View view) {
        startActivity(new Intent(this, profile.class));
    }

    public void goLogout(View view) {
        logoutUser(this);
    }

    private void logoutUser(Context context) {
        dbManager.logoutUser(context);
        Intent intent = new Intent(context, logIn.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
