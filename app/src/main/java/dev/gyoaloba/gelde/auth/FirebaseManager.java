package dev.gyoaloba.gelde.auth;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseManager {
    private static final String TAG = "FIREBASE_MANAGER";
    public interface Callback {
        void onSuccess();
        void onFailure(AuthErrorType errorType, String message);
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
                    if (task.isSuccessful()) {
                        Log.i(TAG, "Successful User Sign Up");
                        callback.onSuccess();
                    }
                    else {
                        FirebaseAuthException authException = (FirebaseAuthException) task.getException();
                        AuthErrorType errorType = AuthErrorType.getErrorType(authException);
                        String message = errorType != AuthErrorType.UNKNOWN_ERROR ? authException.getMessage() : "MAJOR ERROR!";
                        callback.onFailure(errorType, message);
                        Log.e(TAG, errorType + ": " + message);
                    }
                });
    }

    public static void login(String email, String password, Callback callback) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.i(TAG, "Successful User Log In");
                        callback.onSuccess();
                    }
                    else {
                        //TODO more robust error management
                        FirebaseAuthException authException = (FirebaseAuthException) task.getException();
                        AuthErrorType errorType = AuthErrorType.getErrorType(authException);
                        String message = errorType != AuthErrorType.UNKNOWN_ERROR ? authException.getMessage() : "MAJOR ERROR!";
                        callback.onFailure(errorType, message);
                        Log.e(TAG, errorType + ": " + message);
                    }
                });
    }
}
