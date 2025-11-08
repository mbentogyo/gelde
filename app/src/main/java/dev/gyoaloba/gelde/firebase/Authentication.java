package dev.gyoaloba.gelde.firebase;

import android.util.Log;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Authentication {
    public interface Callback {
        void onSuccess();
        void onFailure(FirebaseEnum errorType);
    }

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

    public static void signOut(){
        auth().signOut();
    }

    public static void signUp(String email, String password, Callback callback) {
        //TODO onSuccess and onFailure instead of onComplete
        auth().createUserWithEmailAndPassword(email, password)
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
        auth().signInWithEmailAndPassword(email, password)
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
