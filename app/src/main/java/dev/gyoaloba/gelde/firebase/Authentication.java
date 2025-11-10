package dev.gyoaloba.gelde.firebase;

import android.util.Log;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Authentication {

    private static final String TAG = "FIREBASE_AUTH_MANAGER";

    private static FirebaseAuth auth(){
        return FirebaseAuth.getInstance();
    }
    public static boolean isSignedIn() {
        return getCurrentUser() != null;
    }

    private static FirebaseUser getCurrentUser(){
        return auth().getCurrentUser();
    }

    public static String getUserUID(){
        return getCurrentUser().getUid();
    }

    public static String getEmail() {
        return getCurrentUser().getEmail();
    }

    public static void signOut(){
        auth().signOut();
    }

    public static void signUp(String email, String password, Callback callback) {
        auth().createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(r -> {
                    Log.i(TAG, "Successful User Sign Up");
                    callback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    FirebaseException exception = (FirebaseException) e;
                    ExceptionEnum errorType = ExceptionEnum.getErrorType(exception);

                    callback.onFailure(errorType);
                    logException(exception);
                });
    }

    public static void login(String email, String password, Callback callback) {
        auth().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(r -> {
                    Log.i(TAG, "Successful User Log In");
                    callback.onSuccess();
                }).addOnFailureListener(e -> {
                    FirebaseException exception = (FirebaseException) e;
                    ExceptionEnum errorType = ExceptionEnum.getErrorType(exception);

                    callback.onFailure(errorType);
                    logException(exception);
                });
    }

    private static void logException(FirebaseException exception){
        Log.e(TAG,
                "Exception occurred. Type: " + ExceptionEnum.getErrorType(exception) +
                "\nMessage: " + exception.getMessage() +
                "\nStacktrace: " + Log.getStackTraceString(exception)
        );
    }
}
