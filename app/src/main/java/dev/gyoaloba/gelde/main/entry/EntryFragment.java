package dev.gyoaloba.gelde.main.entry;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.rejowan.cutetoast.CuteToast;

import java.util.List;
import java.util.stream.Collectors;

import dev.gyoaloba.gelde.GeldeMain;
import dev.gyoaloba.gelde.databinding.FragmentEntryBinding;
import dev.gyoaloba.gelde.firebase.DataStorage;
import dev.gyoaloba.gelde.firebase.Entry;
import dev.gyoaloba.gelde.firebase.ExceptionEnum;
import dev.gyoaloba.gelde.firebase.Wallet;
import dev.gyoaloba.gelde.firebase.Callback;
import dev.gyoaloba.gelde.util.StringValidation;

public class EntryFragment extends Fragment {

    private FragmentEntryBinding binding;

    /** @noinspection DataFlowIssue*/
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EntryViewModel entryViewModel = new ViewModelProvider(this).get(EntryViewModel.class);

        binding = FragmentEntryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        DataStorage.getWalletsLive().observe(getViewLifecycleOwner(), this::setDropdown);
        entryViewModel.getWasSuccessful().observe(getViewLifecycleOwner(), success -> {
            if (success != null && success) {
                binding.entryTitle.setText("");
                binding.entryAmount.setText("");
                binding.walletsDropdown.setText("");
                binding.entryButton.setEnabled(true);
                entryViewModel.setWasSuccessful(false);
            }
        });

        binding.entryTitle.setOnFocusChangeListener((v, onFocus) -> { if (!onFocus) StringValidation.validateField(binding.entryTitle, binding.entryTitleLayout); });
        binding.entryAmount.setOnFocusChangeListener((v, onFocus) -> {
            if (!onFocus) {
                if (StringValidation.validateField(binding.entryAmount, binding.entryAmountLayout)) {
                    binding.entryAmount.setText(String.format("%.2f", Double.parseDouble(binding.entryAmount.getText().toString())));
                }
            }
        });

        binding.entryButton.setOnClickListener(v -> {
            boolean valid = true;

            valid &= StringValidation.validateField(binding.entryTitle, binding.entryTitleLayout);
            valid &= StringValidation.validateField(binding.entryAmount, binding.entryAmountLayout);

            String dropdownSelect = binding.walletsDropdown.getText().toString();

            if (dropdownSelect.isEmpty()) {
                binding.walletsDropdownLayout.setError("Please select a wallet.");
                return;
            }

            if (!valid) return;

            Wallet wallet = DataStorage.getWallet(dropdownSelect);
            if (wallet == null) {
                binding.walletsDropdownLayout.setError("Please try a different wallet."); // This is a major error.
                return;
            }

            binding.entryButton.setEnabled(false);
            binding.walletsDropdownLayout.setError("");

            Entry entry = new Entry(
                    binding.entryTitle.getText().toString(),
                    Double.parseDouble(binding.entryAmount.getText().toString()),
                    binding.entryToggle.isChecked(),
                    wallet.getName()
            );

            DataStorage.createEntry(entry, wallet, new Callback() {
                @Override
                public void onSuccess() {
                    if (getContext() != null){
                        binding.entryTitle.setText("");
                        binding.entryAmount.setText("");
                        binding.walletsDropdown.setText("");
                        binding.entryButton.setEnabled(true);
                    }

                    entryViewModel.setWasSuccessful(true);
                    GeldeMain.showToast("Entry added successfully!", CuteToast.LENGTH_LONG, CuteToast.SUCCESS);
                }

                @Override
                public void onFailure(ExceptionEnum errorType) {
                    GeldeMain.showToast(errorType.getMessage(), CuteToast.LENGTH_LONG, CuteToast.ERROR);
                }
            });

        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setDropdown(List<Wallet> wallets) {
        if (wallets.isEmpty()) {
            binding.walletsDropdownLayout.setEnabled(false);
            binding.entryButton.setEnabled(false);
            binding.entryButton.setText("Please create a wallet first.");
            return;
        }

        List<String> walletNames = wallets.stream().map(Wallet::getName).collect(Collectors.toList());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, walletNames);
        binding.walletsDropdown.setAdapter(adapter);
        binding.entryButton.setEnabled(true);
        binding.walletsDropdownLayout.setEnabled(true);
        binding.entryButton.setText("Add Entry");
    }
}