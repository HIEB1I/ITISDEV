package com.mobdeve.sustainabite;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class profile extends AppCompatActivity {

    private TextView userNameTextView, userEmailTextView;
    private ImageView profileImageView;
    private DBManager dbManager;

    // Register ActivityResultLauncher to refresh profile after editing
    private final ActivityResultLauncher<Intent> profileLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getBooleanExtra("profile_updated", false)) {
                        fetchUserProfile(); // Refresh user profile
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userNameTextView = findViewById(R.id.sampleUsername);
        userEmailTextView = findViewById(R.id.sampleEmail);
        profileImageView = findViewById(R.id.samplePhoto);

        dbManager = new DBManager();

        fetchUserProfile();
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
                Toast.makeText(profile.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /* NAVIGATIONS */
    public void goEdit(View view) {
        Intent intent = new Intent(this, profile_edit.class);
        profileLauncher.launch(intent);
    }

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
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clears back stack
        startActivity(intent);
        finish();
    }
}
