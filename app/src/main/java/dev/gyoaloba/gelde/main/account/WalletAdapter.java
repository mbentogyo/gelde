package dev.gyoaloba.gelde.main.account;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rejowan.cutetoast.CuteToast;

import java.util.ArrayList;
import java.util.List;

import dev.gyoaloba.gelde.GeldeMain;
import dev.gyoaloba.gelde.R;
import dev.gyoaloba.gelde.firebase.DataStorage;
import dev.gyoaloba.gelde.firebase.ExceptionEnum;
import dev.gyoaloba.gelde.firebase.Wallet;
import dev.gyoaloba.gelde.firebase.Callback;
import dev.gyoaloba.gelde.util.StringValidation;

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.ViewHolder> {
    private final List<Wallet> wallets = new ArrayList<>();

    public WalletAdapter(List<Wallet> wallets) {
        this.wallets.addAll(wallets);
    }

    /** @noinspection ClassEscapesDefinedScope*/
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wallet, parent, false);
        return new ViewHolder(view);
    }

    /** @noinspection ClassEscapesDefinedScope*/
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Wallet wallet = wallets.get(position);
        holder.name.setText(wallet.getName());
        holder.balance.setText("₱ " + StringValidation.formatDouble(wallet.getBalance()));

        GradientDrawable bg = (GradientDrawable) holder.colorCircle.getBackground();
        bg.setColor(Color.parseColor(wallet.getColor()));

        holder.menuButton.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(v.getContext(), v);
            popup.inflate(R.menu.wallet_menu);
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_delete) {
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("Delete Wallet")
                            .setMessage("Are you sure you want to delete this wallet and all of its transactions? This action cannot be undone.")
                            .setPositiveButton("Delete", (dialog, which) -> DataStorage.deleteWallet(wallet, new Callback() {
                                @Override
                                public void onSuccess() {
                                    GeldeMain.showToast("Successfully deleted wallet", CuteToast.LENGTH_LONG, CuteToast.SUCCESS);
                                }

                                @Override
                                public void onFailure(ExceptionEnum errorType) {
                                    GeldeMain.showToast(errorType.getMessage(), CuteToast.LENGTH_LONG, CuteToast.ERROR);
                                }
                            }))
                            .setNegativeButton("Cancel", null)
                            .show();
                    return true;
                }
                return false;
            });
            popup.show();
        });
    }

    @Override
    public int getItemCount() {
        return wallets.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final View colorCircle;
        final TextView name;
        final TextView balance;
        final ImageView menuButton;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            colorCircle = itemView.findViewById(R.id.color_circle);
            name = itemView.findViewById(R.id.text_wallet_name);
            balance = itemView.findViewById(R.id.text_wallet_balance);
            menuButton = itemView.findViewById(R.id.menu_button);
        }
    }

    public void updateWallets(List<Wallet> newWallets) {
        this.wallets.clear();
        this.wallets.addAll(newWallets);
        notifyDataSetChanged();
    }

}

