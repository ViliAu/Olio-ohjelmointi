package com.example.bankapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bankapplication.databinding.FragmentCustomerTransactionHistoryBinding;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;

public class CustomerTransactionHistoryFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    // Recyclerview
    private RecyclerView recycler;
    private CustomerTransactionHistoryRecyclerAdapter recyclerAdapter;
    private RecyclerView.LayoutManager recyclerLayoutManager;


    private FragmentCustomerTransactionHistoryBinding binding;
    private SharedViewModelCustomer viewModel;
    private ArrayAdapter<Account> accountAdapter;

    private ArrayList<SpinnerAccount> accs;
    private ArrayList<PaymentTransaction> payments = new ArrayList<>();

    private int accPosition = 0;
    private TimeManager time;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCustomerTransactionHistoryBinding.inflate(inflater, container, false);
        initElements();
        time = TimeManager.getInstance();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null)
            viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModelCustomer.class);
        initSpinner();
    }

    private void initElements() {
        initRecyclerview();
    }

    private void initSpinner() {
        accountAdapter = new ArrayAdapter(getContext(), R.layout.spinner_item_account_customer, getAccounts());
        accountAdapter.setDropDownViewResource(R.layout.spinner_item_account_customer);
        binding.spinner.setAdapter(accountAdapter);
        binding.spinner.setOnItemSelectedListener(this);
        binding.spinner.setSelection(0);
    }

    // Get all the user's accounts that have buying enabled
    private ArrayList<SpinnerAccount> getAccounts() {
        accs = new ArrayList<>();
        for (Account a : viewModel.getAccounts()) {
            if (a.state == 2 || a.state == 4) {
                accs.add(new SpinnerAccount(a.getAccountNumber()));
            }
        }
        return accs;
    }

    private void initRecyclerview() {
        recycler = binding.recyclerView;
        recycler.setHasFixedSize(true);
        recyclerLayoutManager = new LinearLayoutManager(getContext());
        recyclerAdapter = new CustomerTransactionHistoryRecyclerAdapter(payments);
        recycler.setLayoutManager(recyclerLayoutManager);
        recycler.setAdapter(recyclerAdapter);
    }

    private void updateRecyclerView() {
        payments = getPayments(accs.get(accPosition).getAccountNumber());
        recyclerAdapter = new CustomerTransactionHistoryRecyclerAdapter(payments);
        recycler.setAdapter(recyclerAdapter);

        recyclerAdapter.setOnItemClickListener(new CustomerTransactionHistoryRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onInfoItemClick(int position) {
                showInfo(position);
            }
        });
    }

    private ArrayList<PaymentTransaction> getPayments(String accountNumber) {
        ArrayList<PaymentTransaction> transactions = new ArrayList<>();
        ResultSet rs;
        try {
            rs = DataBase.dataQuery("SELECT * FROM transaction_history WHERE account_from = '"+accountNumber+"' OR account_to = '"+accountNumber+"' ORDER BY date DESC");
            if (rs != null) {
                do {
                    transactions.add(new PaymentTransaction(
                            rs.getString("account_from"), rs.getString("account_to"), rs.getString("bic_from"),
                            rs.getString("bic_to"), rs.getString("message"),
                            rs.getFloat("amount"), rs.getTimestamp("date"),
                            rs.getString("action"), accountNumber
                    ));
                } while (rs.next());
            }
        }
        catch (SQLException e) {
            System.out.println("_LOG: "+e);
        }
        return transactions;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        accPosition = position;
        updateRecyclerView();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void showInfo(int position) {
        // Set data to viewmodel
        PaymentTransaction pay = payments.get(position);
        viewModel.setAccountFrom(pay.getAccountFrom());
        viewModel.setAccountTo(pay.getAccountTo());
        viewModel.setBicFrom(pay.getBicFrom());
        viewModel.setBicTo(pay.getBicTo());
        viewModel.setDate(time.getReadableDateTime(pay.getDate().getTime()));
        viewModel.setMessage(pay.getMessage());
        viewModel.setAmount(String.format(Locale.GERMANY, "%.2f€", pay.getAmount()));

        // Open dialog
        CustomerTransactionHistoryInfoDialog dialog = new CustomerTransactionHistoryInfoDialog();
        CustomerActivity activity = (CustomerActivity)getActivity();
        dialog.show(activity.getSupportFragmentManager(), "dialog");
    }

    //TODO: Remove this class and add the functionality to account class
    private class SpinnerAccount {
        String text;
        String number;
        private SpinnerAccount(String accountNumber) {
            this.number = accountNumber;
            this.text = String.format(Locale.GERMANY, "%s", accountNumber);
        }
        @Override
        public String toString() {
            return text;
        }
        private String getAccountNumber() {
            return number;
        }
    }
}
