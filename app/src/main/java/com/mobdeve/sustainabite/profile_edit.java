package com.mobdeve.sustainabite;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class profile_edit extends AppCompatActivity {

    private TextView userNameTextView, userEmailTextView;
    private ImageView profileImageView;
    private EditText editUsername, editEmail, editPassword, editConfirmPassword;
    private Button btnSaveChanges;
    private DBManager dbManager;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        // Initialize UI elements
        userNameTextView = findViewById(R.id.sampleUsername);
        userEmailTextView = findViewById(R.id.sampleEmail);
        profileImageView = findViewById(R.id.samplePhoto);

        editUsername = findViewById(R.id.editTextUsername);
        editEmail = findViewById(R.id.editTextEmail);
        editPassword = findViewById(R.id.editPassword);
        editConfirmPassword = findViewById(R.id.editConfirmPassword);
        btnSaveChanges = findViewById(R.id.roundedButton);

        dbManager = new DBManager();

        fetchUserProfile();

        // Fetch current user details and prefill the fields
        dbManager.getCurrentUserDetails(this, new DBManager.OnUserDetailsFetchedListener() {
            @Override
            public void onUserDetailsFetched(String id, String email, String name, String password) {
                userId = id;
                editUsername.setText(name);
                editEmail.setText(email);
                editPassword.setText("");
                editConfirmPassword.setText("");
            }

            @Override
            public void onError(String message) {
                Toast.makeText(profile_edit.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        // Handle save changes button click
        btnSaveChanges.setOnClickListener(view -> saveProfileChanges());
    }

    private void fetchUserProfile() {
        dbManager.getCurrentUserDetails(this, new DBManager.OnUserDetailsFetchedListener() {
            @Override
            public void onUserDetailsFetched(String userId, String email, String name, String image) {
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

    public void saveProfileChanges() {
        String updatedUsername = editUsername.getText().toString().trim();
        String updatedEmail = editEmail.getText().toString().trim();
        String updatedPassword = editPassword.getText().toString().trim();
        String confirmPassword = editConfirmPassword.getText().toString().trim();

        // Ensure all fields are filled
        if (updatedUsername.isEmpty() || updatedEmail.isEmpty()) {
            Toast.makeText(this, "Username and Email cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ensure passwords are provided and match
        if (updatedPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Password fields cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!updatedPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userId != null && !userId.isEmpty()) {
            dbManager.updateUserProfile(userId, updatedUsername, updatedEmail, updatedPassword, new DBManager.OnUserUpdateListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(profile_edit.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();

                    // Send result back to profile page
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
        startActivity(new Intent(this, logIn.class));
    }

    private void logoutUser(Context context) {
        dbManager.logoutUser(context);
        Intent intent = new Intent(context, logIn.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clears back stack
        startActivity(intent);
        finish();
    }
}
