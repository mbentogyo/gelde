package dev.gyoaloba.gelde.main.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import dev.gyoaloba.gelde.GeldeMain;
import dev.gyoaloba.gelde.R;
import dev.gyoaloba.gelde.databinding.FragmentHomeBinding;
import dev.gyoaloba.gelde.firebase.DataStorage;
import dev.gyoaloba.gelde.firebase.Entry;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView totalBalanceText = binding.totalBalanceText;

        DataStorage.getTotalBalance().observe(getViewLifecycleOwner(), total -> {
            totalBalanceText.setText(String.format("Your total balance: ₱ %.2f", total));
        });

        LinearLayout entriesContainer = binding.entriesContainer;
        Spinner walletFilter = binding.walletFilterSpinner;
        Button nextButton = binding.buttonNext;
        Button prevButton = binding.buttonPrev;

        homeViewModel.getEntriesLive().observe(getViewLifecycleOwner(), entries -> {
            entriesContainer.removeAllViews();
            LayoutInflater localInflater = LayoutInflater.from(requireContext());
            List<Entry> currentPageEntries = homeViewModel.getCurrentPageEntries();

            for (Entry entry : currentPageEntries) {
                View card = localInflater.inflate(dev.gyoaloba.gelde.R.layout.item_entry, entriesContainer, false);
                card.findViewById(R.id.layout).setBackgroundColor(getResources().getColor((entry.isIncome() ? R.color.green : R.color.red), GeldeMain.getAppContext().getTheme()));

                ((TextView) card.findViewById(dev.gyoaloba.gelde.R.id.entry_title)).setText(entry.getTitle());
                ((TextView) card.findViewById(dev.gyoaloba.gelde.R.id.entry_amount)).setText("₱ " + String.format("%.2f", entry.getAmount()));
                ((TextView) card.findViewById(dev.gyoaloba.gelde.R.id.entry_wallet)).setText(entry.getWallet());
                ((TextView) card.findViewById(dev.gyoaloba.gelde.R.id.entry_timestamp)).setText(entry.getTimestamp().toDate().toString());

                entriesContainer.addView(card);
            }
        });

        nextButton.setOnClickListener(v -> {
            homeViewModel.nextPage();
            homeViewModel.refresh();
        });

        prevButton.setOnClickListener(v -> {
            homeViewModel.prevPage();
            homeViewModel.refresh();
        });

        List<String> walletNames = new ArrayList<>();
        walletNames.add("All");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                walletNames
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        walletFilter.setAdapter(adapter);

        walletFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) parent.getItemAtPosition(position);
                homeViewModel.filterByWallet(selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        DataStorage.getEntriesLive().observe(getViewLifecycleOwner(), entries -> {
            homeViewModel.setEntries(entries);

            Set<String> wallets = entries.stream()
                    .map(e -> e.getWallet())
                    .collect(Collectors.toCollection(LinkedHashSet::new));

            List<String> newWalletNames = new ArrayList<>();
            newWalletNames.add("All");
            newWalletNames.addAll(wallets);

            ArrayAdapter<String> newAdapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    newWalletNames
            );
            newAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            walletFilter.setAdapter(newAdapter);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
