package com.example.bankapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bankapplication.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Iterator;

// Main activity consist of bank selection + login + new account creation.
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FragmentManager fm;
    private FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup toolbar
        setupToolbar();

        // Initialize view to start fragment
        fm = getSupportFragmentManager();
        if (fm != null) {
            ft = fm.beginTransaction();
            ft.add(R.id.fragment_container, new StartFragment());
            ft.commit();
        }

        // Check and establish a database connection
        if (!DataBase.getConnection()) {
            Toast.makeText(this, "Couldn't connect to database.", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Successfully connected to database.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        //DataBase.closeConnection();
        super.onDestroy();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new StartFragment());
            }
        });
    }

    public void loadFragment(Fragment fragment) {
        if (fragment == null)
            return;
        // Hide back button if we're in the first view
        if (fragment instanceof StartFragment)
            binding.buttonBack.setVisibility(View.INVISIBLE);
        else
            binding.buttonBack.setVisibility(View.VISIBLE);

        // Load fragment
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }

    public void loadAdminActivity() {
        Intent i = new Intent(this, AdminActivity.class);
        startActivity(i);
        this.finish();
    }

    public void loadCustomerActivity(int id) {
        Intent i = new Intent(this, CustomerActivity.class);
        i.putExtra("customerId", id);
        startActivity(i);
        this.finish();
    }


}
