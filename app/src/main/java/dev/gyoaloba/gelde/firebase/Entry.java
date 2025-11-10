package dev.gyoaloba.gelde.firebase;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

import lombok.Getter;

public class Entry {

    @Getter private final UUID uid;
    @Getter private final String title;
    @Getter private final double amount;
    @Getter private final boolean isIncome;
    @Getter private final String wallet;
    @Getter private final Timestamp timestamp;

    public Entry(@NotNull String title, double amount, boolean isIncome, @NotNull String wallet){
        this.uid = UUID.randomUUID();
        this.title = title;
        this.amount = amount;
        this.isIncome = isIncome;
        this.wallet = wallet;
        this.timestamp = Timestamp.now();
    }

    private Entry(@NotNull UUID uid, @NotNull String title, double amount, boolean isIncome, @NotNull String wallet, @NotNull Timestamp timestamp){
        this.uid = uid;
        this.title = title;
        this.amount = amount;
        this.isIncome = isIncome;
        this.wallet = wallet;
        this.timestamp = timestamp;
    }

    public Map<String, Object> toMap(){
        return Map.of(
                "uid", this.uid.toString(),
                "title", this.title,
                "amount", this.amount,
                "isIncome", this.isIncome,
                "wallet", this.wallet,
                "timestamp", this.timestamp
        );
    }

    /** @noinspection DataFlowIssue*/
    public static Entry fromQuery(QueryDocumentSnapshot doc) {
        return new Entry(
                    UUID.fromString(doc.getString("uid")),
                    doc.getString("title"),
                    doc.getDouble("amount"),
                    doc.getBoolean("isIncome"),
                    doc.getString("wallet"),
                    doc.getTimestamp("timestamp")
            );
    }
}
