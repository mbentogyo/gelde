package dev.gyoaloba.gelde.auth;

import android.util.Log;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseManager {
    private static final String TAG = "FIREBASE_MANAGER";
    public interface Callback {
        void onSuccess();
        void onFailure(FirebaseEnum errorType);
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
                        FirebaseException exception = (FirebaseException) task.getException();
                        FirebaseEnum errorType = FirebaseEnum.getErrorType(exception);

                        callback.onFailure(errorType);
                        logException(exception);
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
                        FirebaseException exception = (FirebaseException) task.getException();
                        FirebaseEnum errorType = FirebaseEnum.getErrorType(exception);

                        callback.onFailure(errorType);
                        logException(exception);
                    }
                });
    }

    private static void logException(FirebaseException exception){
        Log.e(TAG,
                "Exception occurred. Type: " + FirebaseEnum.getErrorType(exception) +
                "\nMessage: " + exception.getMessage() +
                "\nStacktrace: " + Log.getStackTraceString(exception)
        );
    }
}
