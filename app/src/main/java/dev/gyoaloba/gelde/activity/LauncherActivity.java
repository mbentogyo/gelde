package dev.gyoaloba.gelde.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import dev.gyoaloba.gelde.R;
import dev.gyoaloba.gelde.activity.auth.LoginActivity;
import dev.gyoaloba.gelde.activity.auth.SignupActivity;
import dev.gyoaloba.gelde.firebase.Authentication;
import dev.gyoaloba.gelde.main.MainActivity;

public class LauncherActivity extends AppCompatActivity {
    Button loginButton, signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);

        if (Authentication.isSignedIn()) MainActivity.launchMain(this);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_launcher);

        loginButton = findViewById(R.id.login_button);
        signupButton = findViewById(R.id.signup_button);

        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });

        signupButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignupActivity.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public static void returnToLauncher(AppCompatActivity activity){
        activity.startActivity(new Intent(activity, LauncherActivity.class));
        activity.finish();
    }
}