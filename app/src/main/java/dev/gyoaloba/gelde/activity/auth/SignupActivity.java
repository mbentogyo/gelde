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

import dev.gyoaloba.gelde.GeldeMain;
import dev.gyoaloba.gelde.R;
import dev.gyoaloba.gelde.firebase.Authentication;
import dev.gyoaloba.gelde.firebase.Callback;
import dev.gyoaloba.gelde.firebase.ExceptionEnum;
import dev.gyoaloba.gelde.main.MainActivity;
import dev.gyoaloba.gelde.util.StringValidation;

public class SignupActivity extends AppCompatActivity {

    TextInputEditText email, password, confirmPassword;
    TextInputLayout emailLayout, passwordLayout, confirmPasswordLayout;
    TextView redirect;
    Button signupButton;

    /** @noinspection DataFlowIssue*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        //EditTexts
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);

        //Layouts
        emailLayout = findViewById(R.id.email_layout);
        passwordLayout = findViewById(R.id.password_layout);
        confirmPasswordLayout = findViewById(R.id.confirm_password_layout);

        //OnFocusChangeListeners
        email.setOnFocusChangeListener((v, onFocus) -> { if (!onFocus) StringValidation.validateEmailField(email, emailLayout); });
        password.setOnFocusChangeListener((v, onFocus) -> { if (!onFocus) StringValidation.validatePasswordField(password, passwordLayout); });
        confirmPassword.setOnFocusChangeListener((v, onFocus) -> { if (!onFocus) StringValidation.validateField(confirmPassword, confirmPasswordLayout); });

        //Sign up button
        signupButton = findViewById(R.id.signup_button);
        signupButton.setOnClickListener(v -> {
            boolean valid = true;

            valid &= StringValidation.validateEmailField(email, emailLayout);
            valid &= StringValidation.validatePasswordField(password, passwordLayout);
            valid &= StringValidation.validateField(confirmPassword, confirmPasswordLayout);

            if (!valid) return;

            if (!password.getText().toString().equals(confirmPassword.getText().toString())){
                confirmPasswordLayout.setError("Passwords do not match!");
                return;
            }

            Authentication.signUp(email.getText().toString(), password.getText().toString(), new Callback(){
                @Override
                public void onSuccess() {
                    GeldeMain.showToast("Sign up successful! Redirecting to home page...", CuteToast.LENGTH_SHORT, CuteToast.SUCCESS);

                    new Handler(Looper.getMainLooper()).postDelayed(() -> MainActivity.launchMain(SignupActivity.this), 2000);
                }

                @Override
                public void onFailure(ExceptionEnum errorType) {
                    if (errorType == ExceptionEnum.AUTH_USER_COLLISION) {
                        emailLayout.setError("User already exists!");
                    } else {
                        emailLayout.setError(" ");
                        passwordLayout.setError(" ");
                        confirmPasswordLayout.setError(" ");

                        GeldeMain.showToast(errorType.getMessage(), CuteToast.LENGTH_LONG, CuteToast.ERROR);
                    }
                }
            });
        });

        redirect = findViewById(R.id.redirect);
        redirect.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
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