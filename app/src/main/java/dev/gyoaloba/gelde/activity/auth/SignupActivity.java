package dev.gyoaloba.gelde.activity.auth;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import dev.gyoaloba.gelde.R;
import dev.gyoaloba.gelde.util.StringValidation;

public class SignupActivity extends AppCompatActivity {

    TextInputEditText fname, lname, email, password, confirmPassword;
    TextInputLayout fnameLayout, lnameLayout, emailLayout, passwordLayout, confirmPasswordLayout;
    Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        //EditTexts
        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);

        //Layouts
        fnameLayout = findViewById(R.id.fname_layout);
        lnameLayout = findViewById(R.id.lname_layout);
        emailLayout = findViewById(R.id.email_layout);
        passwordLayout = findViewById(R.id.password_layout);
        confirmPasswordLayout = findViewById(R.id.confirm_password_layout);

        fname.setOnFocusChangeListener((v, onFocus) -> { if (!onFocus) StringValidation.validateNameField(fname, fnameLayout); });
        lname.setOnFocusChangeListener((v, onFocus) -> { if (!onFocus) StringValidation.validateNameField(lname, lnameLayout); });
        email.setOnFocusChangeListener((v, onFocus) -> { if (!onFocus) StringValidation.validateEmailField(email, emailLayout); });
        password.setOnFocusChangeListener((v, onFocus) -> { if (!onFocus) StringValidation.validatePasswordField(password, passwordLayout); });
        confirmPassword.setOnFocusChangeListener((v, onFocus) -> { if (!onFocus) StringValidation.validateField(confirmPassword, confirmPasswordLayout); });

        //Sign up button
        signupButton = findViewById(R.id.signup_button);
        signupButton.setOnClickListener(v -> {
            boolean valid = true;

            valid &= StringValidation.validateNameField(fname, fnameLayout);
            valid &= StringValidation.validateNameField(lname, lnameLayout);
            valid &= StringValidation.validateEmailField(email, emailLayout);
            valid &= StringValidation.validatePasswordField(password, passwordLayout);
            valid &= StringValidation.validateField(confirmPassword, confirmPasswordLayout);

            if (!valid) return;

            if (!password.getText().toString().equals(confirmPassword.getText().toString())){
                confirmPasswordLayout.setError("Passwords do not match!");
                return;
            }

            //TODO: sign up logic
            System.out.println("yay");
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}