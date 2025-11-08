package dev.gyoaloba.gelde.main.entry;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import dev.gyoaloba.gelde.databinding.FragmentEntryBinding;

public class EntryFragment extends Fragment {

    private FragmentEntryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EntryViewModel entryViewModel = new ViewModelProvider(this).get(EntryViewModel.class);

        binding = FragmentEntryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textEntry;
        //entryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}