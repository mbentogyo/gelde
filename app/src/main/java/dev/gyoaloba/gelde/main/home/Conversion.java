package dev.gyoaloba.gelde.main.home;

import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dev.gyoaloba.gelde.firebase.DataCallback;
import dev.gyoaloba.gelde.firebase.ExceptionEnum;
import dev.gyoaloba.gelde.util.StringValidation;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Conversion {

    private static final String API_KEY = "2211e31b7eaf6642b94daf26";
    private static final String TAG = "CurrencyConverter";
    private static final Map<String, Double> rates = new HashMap<>();

    public static void getConversion(double amount, String currency, DataCallback<String> callback) {
        if (rates.containsKey(currency)) {
            double rate = rates.get(currency);
            if (rate == 0) {
                callback.onSuccess("Invalid Currency");
                return;
            }

            String formatted = StringValidation.formatDouble(amount * rate) + " " + currency;
            callback.onSuccess(formatted);
            return;
        }

        new Thread(() -> {
            JSONObject json = fetchCurrencyData(currency);
            if (json == null) {
                callback.onFailure(ExceptionEnum.NETWORK);
                return;
            }

            try {
                String result = json.optString("result", "error");
                if (!result.equals("success")) {
                    rates.put(currency, 0d);
                    callback.onSuccess("Invalid Currency");
                    return;
                }

                double rate = json.getDouble("conversion_rate");
                rates.put(currency, rate);
                String formatted = StringValidation.formatDouble(amount * rate) + " " + currency;
                callback.onSuccess(formatted);

            } catch (Exception e) {
                Log.e(TAG, "Error parsing conversion rate", e);
                callback.onFailure(ExceptionEnum.UNKNOWN_ERROR); // shouldnt happen
            }
        }).start();
    }

    private static String getURL(String currency) {
        return "https://v6.exchangerate-api.com/v6/" + API_KEY + "/pair/PHP/" + currency + "/";
    }

    private static JSONObject fetchCurrencyData(String currency) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(getURL(currency))
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            return new JSONObject(responseBody);
        } catch (Exception e) {
            Log.e(TAG, "Error fetching currency data", e);
            return null;
        }
    }
}