package dev.gyoaloba.gelde.firebase;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

import lombok.Getter;

public class Wallet {

    @Getter private final String name;
    @Getter private final String color;
    @Getter private double balance;

    public Wallet(@NotNull String name, @NotNull String color){
        this.name = name.trim();
        this.color = color.trim().matches("^#([A-Fa-f0-9]{6})$")? color: "#000000";
        this.balance = 0;
    }

    public Wallet setBalance(double balance){
        this.balance = balance;
        return this;
    }

    public Wallet changeBalance(double amount){
        this.balance += amount;
        return this;
    }

    public Map<String, Object> toMap(){
        return Map.of(
                "name", this.name,
                "color", this.color,
                "balance", this.balance
        );
    }

    public static Wallet fromQuery(QueryDocumentSnapshot doc) {
        return new Wallet(
                doc.getString("name"),
                doc.getString("color")
        ).setBalance(doc.getDouble("balance"));
    }
}
