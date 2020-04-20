package com.example.bankapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bankapplication.databinding.FragmentCustomerAccountsBinding;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerAccountsFragment extends Fragment {

    private FragmentCustomerAccountsBinding binding;
    private SharedViewModelCustomer viewModel;

    // Recycler view
    private RecyclerView recycler;
    private CustomerAccountsRecyclerAdapter recyclerAdapter;
    private RecyclerView.LayoutManager recyclerLayoutManager;

    private ArrayList<Account> accounts = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCustomerAccountsBinding.inflate(inflater, container, false);
        initButtons();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null)
            viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModelCustomer.class);
        initRecyclerview();
    }

    private void initRecyclerview() {
        accounts = viewModel.getAccounts();

        recycler = binding.recyclerView;
        recycler.setHasFixedSize(true);
        recyclerLayoutManager = new LinearLayoutManager(getContext());
        recyclerAdapter = new CustomerAccountsRecyclerAdapter(accounts);

        recycler.setLayoutManager(recyclerLayoutManager);
        recycler.setAdapter(recyclerAdapter);

        recyclerAdapter.setOnItemClickListener(new CustomerAccountsRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //TODO: Goto account settings
            }
        });
    }

    private void initButtons() {
        binding.buttonAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerActivity m = (CustomerActivity)getActivity();
                m.loadFragment(new CustomerAccountCreationFragment());
            }
        });
    }

}
