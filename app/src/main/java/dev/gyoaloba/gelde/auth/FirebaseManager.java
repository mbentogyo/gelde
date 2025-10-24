package dev.gyoaloba.gelde.auth;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseManager {
    private static final String TAG = "FIREBASE_MANAGER";
    public interface Callback {
        void onSuccess();
        void onFailure(String errorCode, String message);
    }

    private static FirebaseAuth firebaseAuth;

    public static void initialize() {
        firebaseAuth = FirebaseAuth.getInstance();
    }
    public static boolean isSignedIn() {
        return getCurrentUser() != null;
    }

    public static FirebaseUser getCurrentUser(){
        return firebaseAuth.getCurrentUser();
    }

    public static void signOut(){
        firebaseAuth.signOut();
    }

    public static void signUp(String email, String password, Callback callback) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) callback.onSuccess();
                    else {
                        FirebaseAuthException authException = (FirebaseAuthException) task.getException();

                        if (authException == null) {
                            callback.onFailure("UNKNOWN_ERROR", "Unknown error.");
                            Log.e(TAG, "Unknown error: I don't know how this could happen");
                        } else {
                            callback.onFailure(AuthErrorType.getErrorType(authException).toString(), authException.getMessage());
                            Log.e(TAG, AuthErrorType.getErrorType(authException).toString() + ": " + authException.getMessage());
                        }
                    }
                });
    }

    public static void login(String email, String password, Callback callback) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) callback.onSuccess();
                    else {
                        FirebaseAuthException authException = (FirebaseAuthException) task.getException();

                        if (authException == null) {
                            callback.onFailure("UNKNOWN_ERROR", "Unknown error.");
                            Log.e(TAG, "Unknown error: I don't know how this could happen");
                        } else {
                            callback.onFailure(AuthErrorType.getErrorType(authException).toString(), authException.getMessage());
                            Log.e(TAG, AuthErrorType.getErrorType(authException).toString() + ": " + authException.getMessage());
                        }
                    }
                });
    }
}
