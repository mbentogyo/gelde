package dev.gyoaloba.gelde.main.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dev.gyoaloba.gelde.firebase.Entry;
import lombok.Getter;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final MutableLiveData<List<Entry>> entriesLiveData = new MutableLiveData<>(new ArrayList<>());
    private List<Entry> allEntries = new ArrayList<>();

    @Getter
    private int currentPage = 0;
    private static final int PAGE_SIZE = 5;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<List<Entry>> getEntriesLive() {
        return entriesLiveData;
    }

    public void setEntries(List<Entry> newEntries) {
        if (newEntries == null) newEntries = new ArrayList<>();
        this.allEntries = new ArrayList<>(newEntries); // keep master copy
        List<Entry> sorted = newEntries.stream()
                .sorted((e1, e2) -> e2.getTimestamp().compareTo(e1.getTimestamp()))
                .collect(Collectors.toList());
        entriesLiveData.setValue(sorted);
    }


    public void refresh() {
        // ensure currentPage is in valid range
        List<Entry> current = entriesLiveData.getValue();
        if (current == null) return;

        int totalPages = (int) Math.ceil((double) current.size() / PAGE_SIZE);
        if (currentPage >= totalPages) currentPage = totalPages - 1;

        entriesLiveData.setValue(current);
    }


    public void filterByWallet(String walletName) {
        List<Entry> filtered;
        if (walletName.equals("All")) {
            filtered = allEntries.stream()
                    .sorted((e1, e2) -> e2.getTimestamp().compareTo(e1.getTimestamp()))
                    .collect(Collectors.toList());
        } else {
            filtered = allEntries.stream()
                    .filter(e -> e.getWallet().equals(walletName))
                    .sorted((e1, e2) -> e2.getTimestamp().compareTo(e1.getTimestamp()))
                    .collect(Collectors.toList());
        }

        currentPage = 0; // reset to first page on filter change
        entriesLiveData.setValue(filtered);
    }



    public List<Entry> getCurrentPageEntries() {
        List<Entry> all = entriesLiveData.getValue();
        if (all == null || all.isEmpty()) return new ArrayList<>();

        int start = currentPage * PAGE_SIZE;
        if (start >= all.size()) start = 0; // reset if out of bounds

        int end = Math.min(start + PAGE_SIZE, all.size());
        return all.subList(start, end);
    }


    public void nextPage() {
        List<Entry> all = entriesLiveData.getValue();
        if (all == null) return;
        if ((currentPage + 1) * PAGE_SIZE < all.size()) currentPage++;
    }

    public void prevPage() {
        if (currentPage > 0) currentPage--;
    }

}