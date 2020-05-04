package com.example.bankapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bankapplication.databinding.FragmentCustomerHomeBinding;

import java.util.ArrayList;
import java.util.Locale;

public class CustomerHomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    // Recyclerview
    private RecyclerView recycler;
    private CustomerHomeRecyclerAdapter recyclerAdapter;
    private RecyclerView.LayoutManager recyclerLayoutManager;

    private FragmentCustomerHomeBinding binding;
    private SharedViewModelCustomer viewModel;
    private ArrayAdapter<Account> accountAdapter;

    private ArrayList<SpinnerAccount> accs;
    private ArrayList<PendingPayment> payments = new ArrayList<>();
    private DataManager data;

    private int accPosition = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCustomerHomeBinding.inflate(inflater, container, false);
        data = DataManager.getInstance();
        initElements();
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
        accountAdapter = new ArrayAdapter(getContext(), R.layout.spinner_item_account_customer, getPayableAccounts());
        accountAdapter.setDropDownViewResource(R.layout.spinner_item_account_customer);
        binding.spinner.setAdapter(accountAdapter);
        binding.spinner.setOnItemSelectedListener(this);
        binding.spinner.setSelection(0);
    }

    // Get all the user's accounts that have buying enabled
    private ArrayList<SpinnerAccount> getPayableAccounts() {
        accs = new ArrayList<>();
        for (Account a : viewModel.getAccounts()) {
            if (a.state == 2) {
                accs.add(new SpinnerAccount(a.getAccountNumber()));
            }
        }
        if (accs.isEmpty()) {
            binding.spinner.setVisibility(View.INVISIBLE);
            binding.twSelectAccount.setVisibility(View.INVISIBLE);
            Toast.makeText(getContext(), "You don't have any accounts. Make new ones in the \"Accounts\" tab.", Toast.LENGTH_LONG).show();
        }
        return accs;
    }

    private void initRecyclerview() {
        recycler = binding.recyclerView;
        recycler.setHasFixedSize(true);
        recyclerLayoutManager = new LinearLayoutManager(getContext());
        recyclerAdapter = new CustomerHomeRecyclerAdapter(payments);
        recycler.setLayoutManager(recyclerLayoutManager);
        recycler.setAdapter(recyclerAdapter);
    }

    private void updateRecyclerView() {
        payments = getPayments(accs.get(accPosition).getAccountNumber());
        recyclerAdapter = new CustomerHomeRecyclerAdapter(payments);
        recycler.setAdapter(recyclerAdapter);
        recyclerAdapter.setOnItemClickListener(new CustomerHomeRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onDeleteItemClick(int position) {
                deletePayment(payments.get(position).getId());
            }
        });
    }

    private ArrayList<PendingPayment> getPayments(String accountNumber) {
        try {
            return data.getAccountPendingPayments(accountNumber);
        }
        catch (Exception e) {
            System.err.println("_LOG: "+e);
            Toast.makeText(getContext(), "Error fetching payments.", Toast.LENGTH_LONG).show();
            return null;
        }
    }

    private void deletePayment(int id) {
        try {
            data.deletePayment(id);
            updateRecyclerView();
            Toast.makeText(getContext(), "Payment cancelled.", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            System.err.println("_LOG: "+e);
            Toast.makeText(getContext(), "Error deleting payment.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        accPosition = position;
        updateRecyclerView();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

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
