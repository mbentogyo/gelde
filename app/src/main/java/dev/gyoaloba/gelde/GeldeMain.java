package dev.gyoaloba.gelde;

import android.app.Application;
import android.content.Context;

import com.rejowan.cutetoast.CuteToast;

public class GeldeMain extends Application {
    private static GeldeMain instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Context getAppContext() {
        return instance.getApplicationContext();
    }

    public static void showToast(String message, int duration, int type) {
        CuteToast.ct(getAppContext(), message, duration, type, true).show();
    }
}
