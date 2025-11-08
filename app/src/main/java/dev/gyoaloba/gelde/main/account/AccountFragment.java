package dev.gyoaloba.gelde.main.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import dev.gyoaloba.gelde.activity.LauncherActivity;
import dev.gyoaloba.gelde.databinding.FragmentAccountBinding;
import dev.gyoaloba.gelde.firebase.Authentication;

public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AccountViewModel accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);

        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        requireContext();

        binding.logoutButton.setOnClickListener(v -> {
            Authentication.signOut();
            LauncherActivity.returnToLauncher((AppCompatActivity) requireContext());
        });

        //final TextView textView = binding.textAccount;
        //accountViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}