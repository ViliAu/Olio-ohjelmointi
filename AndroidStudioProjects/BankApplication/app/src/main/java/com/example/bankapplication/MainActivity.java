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
public class MainActivity extends AppCompatActivity implements LoginPinCodeDialog.PinDialogListener{

    private ActivityMainBinding binding;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Fragment fragment;
    private Bank bank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        LoadingScreen.progressBar = binding.progressbar;
        // Ugly workaround for loading progress bar
        LoadingScreen l = new LoadingScreen();
        l.execute();
        l.cancel(true);
        //l.cancel(true);

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

        // Create bank instance so we can load pending payments
        bank = Bank.getInstance();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBackButtonAction();
            }
        });
    }

    private void handleBackButtonAction() {
        if (fragment instanceof MainCustomerCreationFragment) {
            loadFragment(new LoginFragment());
        }
        else {
            loadFragment(new StartFragment());
        }
    }

    public void loadFragment(Fragment fragment) {
        if (fragment == null)
            return;
        this.fragment = fragment;
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
        i.putExtra("bankId", bank.getId());

        startActivity(i);
        this.finish();
    }


    @Override
    public void changeToCustomerActivity(int id) {
        if (id != 0)
            loadCustomerActivity(id);
        else {
            Toast.makeText(this, "Error while trying to log in", Toast.LENGTH_LONG).show();
        }
    }
}
