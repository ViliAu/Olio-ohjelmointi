package com.example.bankapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.bankapplication.databinding.ActivityCustomerBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CustomerActivity extends AppCompatActivity {

    private ActivityCustomerBinding binding;
    private FragmentManager fm;
    private FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup viewmodel
        SharedViewModelCustomer viewModel = ViewModelProviders.of(this).get(SharedViewModelCustomer.class);
        viewModel.setCustomerId(getIntent().getIntExtra("customerId", 0));
        System.out.println("_LOG: "+viewModel.getCustomerId());

        // Setup toolbar
        setupToolbar();

        // Init bottom nav bar
        initBottomBar();

        // Initialize view to start fragment
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
                //loadFragment(new StartFragment());
            }
        });
    }

    private void initBottomBar() {
        binding.bottomNavigationBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        loadFragment(new CustomerHomeFragment());
                        break;
                    case R.id.nav_accounts:
                        loadFragment(new CustomerAccountsFragment());
                        break;
                    case R.id.nav_transaction:
                        loadFragment(new CustomerTransactionFragment());
                        break;
                    case R.id.nav_transaction_history:
                        loadFragment(new CustomerTransactionHistoryFragment());
                        break;
                    case R.id.nav_settings:
                        loadFragment(new CustomerSettingsFragment());
                        break;
                }
                return true;
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
}
