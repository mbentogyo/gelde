package dev.gyoaloba.gelde.firebase;

public interface Callback {
    void onSuccess();
    void onFailure(ExceptionEnum errorType);
}
