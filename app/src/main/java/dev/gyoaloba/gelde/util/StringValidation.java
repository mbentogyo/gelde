package dev.gyoaloba.gelde.util;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class StringValidation {

    public static boolean validateField(TextInputEditText input, TextInputLayout layout) {
        String text = input.getText() != null ? input.getText().toString().trim() : "";
        if (text.isEmpty()) {
            layout.setError("This field is required!");
            return false;
        }

        layout.setError(null);
        return true;
    }

    public static boolean validateNameField(TextInputEditText input, TextInputLayout layout) {
        String text = input.getText() != null ? input.getText().toString().trim() : "";
        if (text.isEmpty()) {
            layout.setError("This field is required!");
            return false;
        } else if (!text.matches("^[A-Z][a-z]+(?: [A-Z][a-z]+)*$")){
            layout.setError("Invalid name!");
            return false;
        } else if (text.charAt(0) > 95) {
            layout.setError("First letter must be capitalized!");
            return false;
        }

        layout.setError(null);
        return true;
    }

    public static boolean validateEmailField(TextInputEditText input, TextInputLayout layout) {
        String text = input.getText() != null ? input.getText().toString().trim() : "";
        if (text.isEmpty()) {
            layout.setError("This field is required!");
            return false;
        } else if (!text.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")){
            layout.setError("Invalid email!");
            return false;
        }

        layout.setError(null);
        return true;
    }

    public static boolean validatePasswordField(TextInputEditText input, TextInputLayout layout) {
        String text = input.getText() != null ? input.getText().toString() : "";
        if (text.isEmpty()) {
            layout.setError("This field is required!");
            return false;
        } else if (text.length() < 8 || text.length() > 20){
            layout.setError("Password must have 8–20 characters total");
            return false;
        } else if (!text.matches(".*\\d.*")){
            layout.setError("Password must contain at least 1 digit");
            return false;
        } else if (!text.matches(".*[a-z].*")) {
            layout.setError("Password must contain at least 1 lowercase letter");
            return false;
        } else if (!text.matches(".*[A-Z].*")){
            layout.setError("Password must contain at least 1 uppercase letter");
            return false;
        } else if (!text.matches(".*[@#$%^&+=!._*?-].*")){
            layout.setError("Password must contain at least 1 special character (@#$%^&+=!._*?-)");
            return false;
        }
        //TODO: remerge and just list these as the errors
        layout.setError(null);
        return true;
    }
}
