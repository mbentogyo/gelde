package dev.gyoaloba.gelde.util;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;

public class StringValidation {
    private static DecimalFormat FORMATTER = new DecimalFormat("#,##0.00");


    public static boolean validateField(TextInputEditText input, TextInputLayout layout) {
        String text = input.getText() != null ? input.getText().toString().trim() : "";
        if (text.isEmpty()) {
            layout.setError("This field is required!");
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
        String error = "";

        //If empty
        if (text.isEmpty()) {
            layout.setError("This field is required!");
            return false;
        }

        if (text.length() < 8 || text.length() > 20) error += " • have 8–20 characters total\n";

        if (!text.matches(".*\\d.*")) error += " • contain at least 1 digit\n";

        if (!text.matches(".*[a-z].*")) error += " • contain at least 1 lowercase letter\n";

        if (!text.matches(".*[A-Z].*")) error += " • contain at least 1 uppercase letter\n";

        if (!text.matches(".*[@#$%^&+=!._*?-].*")) error += " • contain at least 1 special character (@#$%^&+=!._*?-)\n";

        layout.setError(error.isEmpty()? null : "Password must: \n" + error);
        return true;
    }

    public static String formatDouble(Object object) {
        try {
            return FORMATTER.format(object);
        } catch (IllegalArgumentException e) {
            return object.toString();
        }
    }
}
