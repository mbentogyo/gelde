package dev.gyoaloba.gelde.main.entry;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EntryViewModel extends ViewModel {
    private final MutableLiveData<Boolean> wasSuccessful;

    public EntryViewModel() {
        wasSuccessful = new MutableLiveData<>(false);
    }

    public LiveData<Boolean> getWasSuccessful() {
        return wasSuccessful;
    }

    public void setWasSuccessful(boolean value) {
        wasSuccessful.setValue(value);
    }
}
