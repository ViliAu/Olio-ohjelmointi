package com.example.bankapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.bankapplication.databinding.ActivityAdminBinding;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {

    // UI ELEMENTS
    private ActivityAdminBinding binding;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize view to start fragment
        fm = getSupportFragmentManager();
        if (fm != null) {
            ft = fm.beginTransaction();
            ft.add(R.id.fragment_container, new StartAdminFragment());
            ft.commit();
        }
        initElements();

        // Setup toolbar
        setupToolbar();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        binding.buttonBack.setActivated(false);
        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentFragment instanceof StartAdminFragment) {
                    loadMainActivity();
                }
                else
                    loadFragment(new StartAdminFragment());
            }
        });
    }

    void initElements() {

    }

    public void loadFragment(Fragment fragment) {
        currentFragment = fragment;
        if (fragment == null)
            return;
        // Change button to home icon when we're in the start screen
        if (fragment instanceof StartAdminFragment)
            binding.buttonBack.setActivated(false);
        else
            binding.buttonBack.setActivated(true);

        // Load fragment
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }

    private void loadMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        this.finish();
    }
}