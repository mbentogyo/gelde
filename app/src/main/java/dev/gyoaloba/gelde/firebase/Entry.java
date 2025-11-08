package dev.gyoaloba.gelde.firebase;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

import lombok.Getter;

public class Entry {
    @Getter private String uid;
    @Getter private String title;
    @Getter private double amount;
    @Getter private boolean isIncome;
    @Getter private String wallet;
    @Getter private Timestamp timestamp;

    public Entry(@NotNull String title, double amount, boolean isIncome, @NotNull String wallet, @Nullable Timestamp timestamp){
        this.uid = UUID.randomUUID().toString();
        this.title = title;
        this.amount = amount;
        this.isIncome = isIncome;
        this.wallet = wallet;
        this.timestamp = timestamp == null? Timestamp.now() : timestamp;
    }

    public Entry(@NotNull String uid, @NotNull String title, double amount, boolean isIncome, @NotNull String wallet, @Nullable Timestamp timestamp){
        this.uid = uid;
        this.title = title;
        this.amount = amount;
        this.isIncome = isIncome;
        this.wallet = wallet;
        this.timestamp = timestamp == null? Timestamp.now() : timestamp;
    }

    public Map<String, Object> toMap(){
        return Map.of(
                "uid", uid,
                "title", title,
                "amount", amount,
                "isIncome", isIncome,
                "wallet", wallet,
                "timestamp", timestamp
        );
    }

    public static Entry fromQuery(QueryDocumentSnapshot doc) {
        try {
            return new Entry(
                    doc.getString("uid"),
                    doc.getString("title"),
                    doc.getDouble("amount"),
                    doc.getBoolean("isIncome"),
                    doc.getString("wallet"),
                    doc.getTimestamp("timestamp")
            );
        } catch (NullPointerException e){
            throw new IllegalArgumentException("wah"); //TODO
        }
    }
}
