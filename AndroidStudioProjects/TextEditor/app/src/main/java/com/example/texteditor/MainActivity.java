package com.example.texteditor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.texteditor.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;
    private ActionBarDrawerToggle barToggle;
    private FragmentManager fm;
    private FragmentTransaction ft;

    // UI Elements
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Init activity
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.navigationView.setNavigationItemSelectedListener(this);

        // Setup toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Hamburger
        barToggle = new ActionBarDrawerToggle(this, binding.drawer, toolbar, R.string.open, R.string.close);
        binding.drawer.addDrawerListener(barToggle);
        barToggle.setDrawerIndicatorEnabled(true);
        barToggle.syncState();

        // Initialize view to main
        fm = getSupportFragmentManager();
        if (ft == null) {
            ft = fm.beginTransaction();
            ft.add(R.id.container_fragment, new MainFragment());
            ft.commit();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        System.out.println("_LOG: clicked item no. "+item.toString());
        binding.drawer.closeDrawer(GravityCompat.START);
        if (item.getItemId() == R.id.home) {
            loadMain();
        }

        else if (item.getItemId() == R.id.settings) {
            loadSettings();
        }
        return true;
    }

    private void loadMain() {
        // Load default fragment
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.container_fragment, new MainFragment());
        ft.commit();
    }

    private void loadSettings() {
        // Load default fragment
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.container_fragment, new SettingsFragment());
        ft.commit();
    }

    @Override
    public void recreate() {
        super.recreate();
        loadMain();
    }

    public void gotoSetting() {
        Intent i = new Intent(android.provider.Settings.ACTION_LOCALE_SETTINGS);
        startActivity(i);
        recreate();
    }
}
