package dev.gyoaloba.gelde.auth;

import com.google.firebase.auth.FirebaseAuthActionCodeException;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.FirebaseAuthMultiFactorException;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseAuthWebException;

public enum AuthErrorType {
    ACTION_CODE(FirebaseAuthActionCodeException.class),
    EMAIL(FirebaseAuthEmailException.class),
    INVALID_CREDENTIALS(FirebaseAuthInvalidCredentialsException.class),
    INVALID_USER(FirebaseAuthInvalidUserException.class),
    MISSING_ACTIVITY_FOR_RECAPTCHA(FirebaseAuthMissingActivityForRecaptchaException.class),
    MULTI_FACTOR(FirebaseAuthMultiFactorException.class),
    RECENT_LOGIN_REQUIRED(FirebaseAuthRecentLoginRequiredException.class),
    USER_COLLISION(FirebaseAuthUserCollisionException.class),
    WEB(FirebaseAuthWebException.class),
    WEAK_PASSWORD(FirebaseAuthWeakPasswordException.class)

    ;

    private final Class<? extends FirebaseAuthException> exception;

    AuthErrorType(Class<? extends FirebaseAuthException> exception){
        this.exception = exception;
    }

    public static AuthErrorType getErrorType(FirebaseAuthException authException){
        for (AuthErrorType type : AuthErrorType.values()){
            if (type.exception.isInstance(authException)) return type;
        }

        return null;
    }

}
