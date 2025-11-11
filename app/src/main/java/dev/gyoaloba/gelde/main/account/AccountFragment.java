package dev.gyoaloba.gelde.main.account;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rejowan.cutetoast.CuteToast;

import dev.gyoaloba.gelde.GeldeMain;
import dev.gyoaloba.gelde.R;
import dev.gyoaloba.gelde.activity.LauncherActivity;
import dev.gyoaloba.gelde.databinding.FragmentAccountBinding;
import dev.gyoaloba.gelde.firebase.Authentication;
import dev.gyoaloba.gelde.firebase.Callback;
import dev.gyoaloba.gelde.firebase.DataStorage;
import dev.gyoaloba.gelde.firebase.ExceptionEnum;
import dev.gyoaloba.gelde.firebase.Wallet;
import yuku.ambilwarna.AmbilWarnaDialog;

public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.logoutButton.setOnClickListener(v -> {
            Authentication.signOut();
            LauncherActivity.returnToLauncher((AppCompatActivity) requireContext());
        });

        binding.email.setText(Authentication.getEmail());

        RecyclerView recyclerView = binding.walletList;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        WalletAdapter adapter = new WalletAdapter(DataStorage.getWalletsLive().getValue());
        recyclerView.setAdapter(adapter);

        DataStorage.getWalletsLive().observe(getViewLifecycleOwner(), adapter::updateWallets);

        binding.addWallet.setOnClickListener(v -> {
            Dialog dialog = new Dialog(requireContext());
            dialog.setContentView(R.layout.dialog_add_wallet);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            EditText nameInput = dialog.findViewById(R.id.edit_wallet_name);
            EditText colorInput = dialog.findViewById(R.id.edit_wallet_color);
            Button cancelBtn = dialog.findViewById(R.id.button_cancel);
            Button createBtn = dialog.findViewById(R.id.button_create);
            ImageView colorPicker = dialog.findViewById(R.id.color_picker);
            Drawable background = colorPicker.getBackground();

            colorInput.setText("#FFFFFF");
            colorInput.setOnFocusChangeListener((v2, isFocused) -> {
                String color = colorInput.getText().toString();
                if (color.isEmpty() || !color.matches("^#([A-Fa-f0-9]{6})$")) colorInput.setText("#FFFFFF");
                else {
                    DrawableCompat.setTint(background, Color.parseColor(colorInput.getText().toString()));
                    colorPicker.setBackground(background);
                    colorInput.setText(colorInput.getText().toString().toUpperCase());
                }
            });

            colorPicker.setOnClickListener(v3 -> new AmbilWarnaDialog(dialog.getContext(), Color.WHITE, false, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                @Override
                public void onCancel(AmbilWarnaDialog dialog) {}

                @Override
                public void onOk(AmbilWarnaDialog dialog, int color) {
                    DrawableCompat.setTint(background, color);
                    colorPicker.setBackground(background);
                    colorInput.setText(String.format("#%06X", (0xFFFFFF & color)));
                }
            }).show());

            cancelBtn.setOnClickListener(c -> dialog.dismiss());

            createBtn.setOnClickListener(c -> {
                colorInput.clearFocus();
                if (nameInput.getText().toString().isEmpty() || colorInput.getText().toString().isEmpty()) {
                    GeldeMain.showToast("Please fill in all fields.", CuteToast.LENGTH_LONG, CuteToast.ERROR);
                    return;
                }

                String name = nameInput.getText().toString().trim();
                String color = colorInput.getText().toString().trim();

                Wallet newWallet = new Wallet(name, color);
                dialog.dismiss();
                binding.addWallet.setEnabled(false);
                DrawableCompat.setTint(background, Color.WHITE);
                colorPicker.setBackground(background);

                DataStorage.createWallet(newWallet, new Callback() {
                    @Override
                    public void onSuccess() {
                        GeldeMain.showToast("Successfully added new wallet \"" + newWallet.getName() + "\"", CuteToast.LENGTH_LONG, CuteToast.SUCCESS);
                        binding.addWallet.setEnabled(true);
                    }

                    @Override
                    public void onFailure(ExceptionEnum errorType) {
                        GeldeMain.showToast(errorType.getMessage(), CuteToast.LENGTH_LONG, CuteToast.ERROR);
                        binding.addWallet.setEnabled(true);
                    }
                });
            });

            dialog.show();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}