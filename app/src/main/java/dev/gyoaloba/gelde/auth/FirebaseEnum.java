package dev.gyoaloba.gelde.auth;

import androidx.annotation.Nullable;

import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthActionCodeException;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import org.jetbrains.annotations.NotNull;

import lombok.Getter;

public enum FirebaseEnum {
    API_NOT_AVAILABLE(FirebaseApiNotAvailableException.class, "This feature is not available on your device."),
    AUTH(FirebaseAuthException.class, "Authentication failed. Please try again."),
    NETWORK(FirebaseNetworkException.class, "Network error. Check your internet connection."),
    TOO_MANY_REQUESTS(FirebaseTooManyRequestsException.class, "Too many requests. Please wait and try again."),

    AUTH_ACTION_CODE(FirebaseAuthActionCodeException.class, "Invalid or expired action code."),
    AUTH_EMAIL(FirebaseAuthEmailException.class, "There was a problem with your email address."),
    AUTH_INVALID_CREDENTIALS(FirebaseAuthInvalidCredentialsException.class, "Invalid credentials. Please verify your details."),
    AUTH_INVALID_USER(FirebaseAuthInvalidUserException.class, "User account not found or has been disabled."),
    AUTH_RECENT_LOGIN_REQUIRED(FirebaseAuthRecentLoginRequiredException.class, "Please log in again to continue."),
    AUTH_USER_COLLISION(FirebaseAuthUserCollisionException.class, "An account already exists with this email."),
    AUTH_WEAK_PASSWORD(FirebaseAuthWeakPasswordException.class, "Password is too weak. Please choose a stronger one."),
    UNKNOWN_ERROR(null, "An unexpected error occurred. Please try again later.");

    ;
    private final Class<? extends FirebaseException> exception;
    @Getter private final String message;

    FirebaseEnum(Class<? extends FirebaseException> exception, String message){
        this.exception = exception;
        this.message = message;
    }

    @NotNull
    public static FirebaseEnum getErrorType(@Nullable FirebaseException exception) {
        if (exception == null) return UNKNOWN_ERROR;

        FirebaseEnum matched = UNKNOWN_ERROR;

        for (FirebaseEnum type : FirebaseEnum.values()) {
            if (type == UNKNOWN_ERROR) continue;

            if (type.exception.isInstance(exception)) {
                if (matched == UNKNOWN_ERROR || matched == AUTH) matched = type;
            }
        }

        return matched;
    }



}
