package dev.gyoaloba.gelde.firebase;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class DataStorage {

    private static final String TAG = "FIREBASE_FIRESTORE_MANAGER";

    private static FirebaseFirestore db(){
        return FirebaseFirestore.getInstance();
    }

    public static void get(){
        db()
                .collection("users")
                .document(Authentication.getUserUID())
                .collection("transactions")
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        Log.d("SortedTxn", doc.getData().toString());
                    }
                });
    }

    public static void addEntry(){

    }
}
