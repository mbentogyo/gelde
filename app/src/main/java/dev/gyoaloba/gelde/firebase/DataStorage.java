package dev.gyoaloba.gelde.firebase;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.FirebaseException;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.rejowan.cutetoast.CuteToast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import dev.gyoaloba.gelde.GeldeMain;

public class DataStorage {
    private static final String TAG = "FIREBASE_FIRESTORE_MANAGER";

    private static final MutableLiveData<List<Wallet>> walletsLive = new MutableLiveData<>();
    private static final MutableLiveData<List<Entry>> entriesLive = new MutableLiveData<>();

    private static final MutableLiveData<Double> totalBalance = new MutableLiveData<>(0d);

    public static LiveData<List<Wallet>> getWalletsLive() {
        if (walletsLive.getValue() == null) walletsLive.setValue(new ArrayList<>());
        return walletsLive;
    }

    public static LiveData<List<Entry>> getEntriesLive() {
        if (entriesLive.getValue() == null) entriesLive.setValue(new ArrayList<>());
        return entriesLive;
    }

    public static LiveData<Double> getTotalBalance() {
        return totalBalance;
    }

    private static FirebaseFirestore db(){
        return FirebaseFirestore.getInstance();
    }

    public static void reload() {
        // Wallets
        db()
                .collection("users")
                .document(Authentication.getUserUID())
                .collection("wallets")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Wallet> wallets = new ArrayList<>();
                    double balance = 0;
                    for (QueryDocumentSnapshot doc : querySnapshot) {

                        Wallet wallet = Wallet.fromQuery(doc);
                        wallets.add(wallet);

                        balance += wallet.getBalance();
                    }
                    walletsLive.setValue(wallets);
                    totalBalance.setValue(balance);
                })
                .addOnFailureListener(e -> {
                    GeldeMain.showToast("Loading wallets was not successful.", CuteToast.LENGTH_LONG, CuteToast.ERROR);
                    Log.e(TAG, "Fetching wallet data was unsuccessful.", e);
                });

        //Entries
        db()
                .collection("users")
                .document(Authentication.getUserUID())
                .collection("transactions")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Entry> entries = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : querySnapshot) entries.add(Entry.fromQuery(doc));
                    entriesLive.setValue(entries);
                }).addOnFailureListener(e -> {
                    GeldeMain.showToast("Loading entries was not successful.", CuteToast.LENGTH_LONG, CuteToast.ERROR);
                    Log.e(TAG, "Fetching entry data was unsuccessful.", e);
                });

    }

    public static Wallet getWallet(String name) {
        try {
            for (Wallet wallet : getWalletsLive().getValue()) {
                if (wallet.getName().equalsIgnoreCase(name)) {
                    return wallet;
                }
            }
        } catch (NullPointerException e){
            GeldeMain.showToast("Loading wallet was not successful.", CuteToast.LENGTH_LONG, CuteToast.ERROR);
            Log.e(TAG, "Fetching wallet data was unsuccessful.", e);
        }

        return null;
    }


    public static void createWallet(Wallet wallet, Callback callback) {
        if (getWallet(wallet.getName()) != null) {
            callback.onFailure(ExceptionEnum.WALLET_ALREADY_EXISTS);
            return;
        }

        db()
                .collection("users")
                .document(Authentication.getUserUID())
                .collection("wallets")
                .document(wallet.getName())
                .set(wallet.toMap())
                .addOnSuccessListener(r -> {
                    reload();
                    callback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Setting wallet data was unsuccessful.", e);
                    callback.onFailure(ExceptionEnum.getErrorType((FirebaseException) e));
                });
    }

    public static void deleteWallet(Wallet wallet, Callback callback){
        AtomicBoolean failed = new AtomicBoolean(false);

        db()
                .collection("users")
                .document(Authentication.getUserUID())
                .collection("wallets")
                .document(wallet.getName())
                .delete()
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Deleting wallet data was unsuccessful.", e);
                    callback.onFailure(ExceptionEnum.getErrorType((FirebaseException) e));
                    failed.set(true);
                });

        if (failed.get()) return;

        db()
                .collection("users")
                .document(Authentication.getUserUID())
                .collection("transactions")
                .whereEqualTo("wallet", wallet.getName())
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (querySnapshot.isEmpty()) {
                        reload();
                        callback.onSuccess();
                        return;
                    }

                    WriteBatch batch = db().batch();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        batch.delete(doc.getReference());
                    }

                    batch.commit()
                            .addOnSuccessListener(r -> {
                                reload();
                                callback.onSuccess();
                            })
                            .addOnFailureListener(e -> callback.onFailure(ExceptionEnum.getErrorType((FirebaseException) e)));
                })
                .addOnFailureListener(e -> callback.onFailure(ExceptionEnum.getErrorType((FirebaseException) e)));
    }

    public static void createEntry(Entry entry, Wallet wallet, Callback callback){
        if (!entry.isIncome() && wallet.getBalance() < entry.getAmount()) {
            callback.onFailure(ExceptionEnum.WALLET_BALANCE_NOT_ENOUGH);
            return;
        }

        wallet.changeBalance(entry.isIncome() ? entry.getAmount() : -entry.getAmount());

        AtomicBoolean failed = new AtomicBoolean(false);

        db()
                .collection("users")
                .document(Authentication.getUserUID())
                .collection("wallets")
                .document(wallet.getName())
                .update(wallet.toMap())
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Setting wallet data was unsuccessful.", e);
                    callback.onFailure(ExceptionEnum.getErrorType((FirebaseException) e));
                    failed.set(true);
                });

        if (failed.get()) return;

        db()
                .collection("users")
                .document(Authentication.getUserUID())
                .collection("transactions")
                .document(entry.getUid().toString())
                .set(entry.toMap())
                .addOnSuccessListener(r -> {
                    reload();
                    callback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Setting entry data was unsuccessful.", e);
                    callback.onFailure(ExceptionEnum.getErrorType((FirebaseException) e));
                });
    }
}
