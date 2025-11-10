package dev.gyoaloba.gelde.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import dev.gyoaloba.gelde.R;
import dev.gyoaloba.gelde.databinding.ActivityMainBinding;
import dev.gyoaloba.gelde.firebase.DataStorage;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DataStorage.reload();
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    public static void launchMain(AppCompatActivity activity) {
        activity.startActivity(new Intent(activity, MainActivity.class));
        activity.finish();
    }

}