package dev.gyoaloba.gelde.firebase;

public interface DataCallback<T> {
    void onSuccess(T t);
    void onFailure(ExceptionEnum errorType);
}