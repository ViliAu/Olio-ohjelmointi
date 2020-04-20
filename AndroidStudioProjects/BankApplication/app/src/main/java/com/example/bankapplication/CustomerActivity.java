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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerActivity extends AppCompatActivity {

    private ActivityCustomerBinding binding;
    private FragmentManager fm;
    private FragmentTransaction ft;
    SharedViewModelCustomer viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup viewmodel
        viewModel = ViewModelProviders.of(this).get(SharedViewModelCustomer.class);
        viewModel.setCustomerId(getIntent().getIntExtra("customerId", 0));
        viewModel.setBankId(getIntent().getIntExtra("bankId", 0));
        System.out.println("_LOG: "+viewModel.getCustomerId() + viewModel.getBankId());
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

    public ArrayList<Account> updateAccounts() {
        ArrayList<Account> accList = new ArrayList<>();
        ResultSet rs = DataBase.dataQuery("SELECT * FROM accounts WHERE owner_id = "+viewModel.getCustomerId()+" ORDER BY money_amount DESC");
        try {
            if (rs != null) {
                if (rs.getInt("state") == 2 || rs.getInt("state") == 4) {
                    do {
                        switch (rs.getInt("type")) {
                            case 1:
                                accList.add(new CurrentAccount(rs.getInt("id"),
                                        viewModel.getCustomerId(), viewModel.getBankId(), 1,
                                        rs.getInt("state"), rs.getString("name"),
                                        rs.getString("address"), rs.getFloat("money_amount")));
                                break;
                            case 2:
                                accList.add(new CreditAccount(rs.getInt("id"),
                                        viewModel.getCustomerId(), viewModel.getBankId(), 2,
                                        rs.getInt("state"), rs.getString("name"),
                                        rs.getString("address"), rs.getFloat("money_amount"), rs.getFloat("credit_limit")));
                                break;
                            case 3:
                                accList.add(new SavingsAccount(rs.getInt("id"),
                                        viewModel.getCustomerId(), viewModel.getBankId(), 3,
                                        rs.getInt("state"), rs.getString("name"),
                                        rs.getString("address"), rs.getFloat("money_amount"), 1));//TODO: Rework interest
                                break;
                            case 4:
                                accList.add(new FixedTermAccount(rs.getInt("id"),
                                        viewModel.getCustomerId(), viewModel.getBankId(), 4,
                                        rs.getInt("state"), rs.getString("name"),
                                        rs.getString("address"), rs.getFloat("money_amount")));
                                break;
                        }
                    } while (rs.next());
                }
            }
        }
        catch (SQLException e) {
            System.out.println("_LOG "+e);
        }
        return accList;
    }
}
