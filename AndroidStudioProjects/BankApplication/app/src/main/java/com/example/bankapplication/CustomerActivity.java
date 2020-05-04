package com.example.bankapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.bankapplication.databinding.ActivityCustomerBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerActivity extends AppCompatActivity {

    private ActivityCustomerBinding binding;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Fragment fragment;
    private DataManager data;
    private SharedViewModelCustomer viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup viewmodel
        data = DataManager.getInstance();
        viewModel = ViewModelProviders.of(this).get(SharedViewModelCustomer.class);
        viewModel.setCustomerId(getIntent().getIntExtra("customerId", 0));
        viewModel.setBankId(getIntent().getIntExtra("bankId", 0));
        viewModel.setAccounts(updateAccounts());

        // Setup toolbar
        setupToolbar();

        // Init bottom nav bar
        initBottomBar();

        // Initialize view to customer fragment
        fm = getSupportFragmentManager();
        if (fm != null) {
            ft = fm.beginTransaction();
            ft.add(R.id.fragment_container, new CustomerHomeFragment());
            ft.commit();
        }
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
        if (fragment instanceof CustomerAccountCreationFragment ||
                fragment instanceof CustomerCardSimulationsFragment ||
                fragment instanceof CustomerAccountSettingsFragment) {
            loadFragment(new CustomerAccountsFragment());
        }
        else if (fragment instanceof CustomerCardSettingsFragment) {
            loadFragment(new CustomerCardSimulationsFragment());
        }
        else if (fragment instanceof CustomerHomeFragment) {
            loadMainActivity();
        }
    }

    private void initBottomBar() {
        binding.bottomNavigationBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        loadFragment(new CustomerHomeFragment());
                        binding.buttonBack.setVisibility(View.VISIBLE);
                        break;
                    case R.id.nav_accounts:
                        loadFragment(new CustomerAccountsFragment());
                        binding.buttonBack.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.nav_transaction:
                        loadFragment(new CustomerTransactionFragment());
                        binding.buttonBack.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.nav_transaction_history:
                        loadFragment(new CustomerTransactionHistoryFragment());
                        binding.buttonBack.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.nav_settings:
                        loadFragment(new CustomerSettingsFragment());
                        binding.buttonBack.setVisibility(View.INVISIBLE);
                        break;
                }
                return true;
            }
        });
    }

    public void loadFragment(Fragment fragment) {
        if (fragment == null)
            return;
        this.fragment = fragment;

        // Change back button to home icon if we're in the first screen
        if (fragment instanceof CustomerHomeFragment)
            binding.buttonBack.setActivated(false);
        else
            binding.buttonBack.setActivated(true);

        // Load fragment
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }

    public ArrayList<Account> updateAccounts() {
        try {
            return data.getCustomerAccounts(viewModel.getCustomerId(), viewModel.getBankId());
        }
        catch (Exception e) {
            System.err.println("_LOG: " + e);
            Toast.makeText(this, "Error loading accounts.", Toast.LENGTH_LONG).show();
            return new ArrayList<>();
        }
    }

    private void loadMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        this.finish();
    }
}