package dev.gyoaloba.gelde.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rejowan.cutetoast.CuteToast;

import dev.gyoaloba.gelde.R;
import dev.gyoaloba.gelde.firebase.FirebaseEnum;
import dev.gyoaloba.gelde.firebase.Authentication;
import dev.gyoaloba.gelde.main.MainActivity;
import dev.gyoaloba.gelde.util.StringValidation;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText email, password;
    TextInputLayout emailLayout, passwordLayout;
    TextView redirect, forgotPassword;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        //EditTexts
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        //Layouts
        emailLayout = findViewById(R.id.email_layout);
        passwordLayout = findViewById(R.id.password_layout);

        email.setOnFocusChangeListener((v, onFocus) -> { if (!onFocus) StringValidation.validateEmailField(email, emailLayout); });
        password.setOnFocusChangeListener((v, onFocus) -> { if (!onFocus) StringValidation.validateField(password, passwordLayout); });

        //Clickables
        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(v -> {
            boolean valid = true;

            valid &= StringValidation.validateEmailField(email, emailLayout);
            valid &= StringValidation.validateField(password, passwordLayout);

            if (!valid) return;

            Authentication.login(email.getText().toString(), password.getText().toString(), new Authentication.Callback() {
                @Override
                public void onSuccess() {
                    CuteToast.ct(LoginActivity.this, "Log in successful! Redirecting to home page...", CuteToast.LENGTH_SHORT, CuteToast.SUCCESS, true).show();

                    new Handler(Looper.getMainLooper()).postDelayed(() -> MainActivity.launchMain(LoginActivity.this), 2000);
                }

                @Override
                public void onFailure(FirebaseEnum errorType) {
                    if (errorType == FirebaseEnum.AUTH_INVALID_CREDENTIALS || errorType == FirebaseEnum.AUTH_INVALID_USER) {
                        emailLayout.setError("Invalid email or password!");
                        passwordLayout.setError("Invalid email or password!");
                    } else {
                        emailLayout.setError(" ");
                        passwordLayout.setError(" ");

                        CuteToast.ct(LoginActivity.this, errorType.getMessage(), CuteToast.LENGTH_LONG, CuteToast.ERROR, true).show();
                    }
                }
            });
        });

        forgotPassword = findViewById(R.id.forgot_password);
        forgotPassword.setOnClickListener(v -> {

        });

        redirect = findViewById(R.id.redirect);
        redirect.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignupActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}