package com.example.bankapplication;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.example.bankapplication.databinding.FragmentAdminHomeBinding;

import java.util.ArrayList;

public class AdminHomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private SharedViewModelAdmin viewModel;
    private FragmentAdminHomeBinding binding;

    private String column = "username";

    // Recycler view
    private RecyclerView recycler;
    private AdminCustomerRecyclerAdapter recyclerAdapter;
    private RecyclerView.LayoutManager recyclerLayoutManager;

    // Lists
    private ArrayList<Customer> customers = new ArrayList<>();

    DataManager data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminHomeBinding.inflate(inflater, container, false);
        data = DataManager.getInstance();
        initElements();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null)
            viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModelAdmin.class);
    }

    private void initElements() {
        initSpinner();
        initRecyclerview();
        initButtons();
    }

    private void initRecyclerview() {
        customers = getCustomers();

        recycler = binding.recyclerView;
        recycler.setHasFixedSize(true);
        recyclerLayoutManager = new LinearLayoutManager(getContext());
        recyclerAdapter = new AdminCustomerRecyclerAdapter(customers);

        recycler.setLayoutManager(recyclerLayoutManager);
        recycler.setAdapter(recyclerAdapter);

        recyclerAdapter.setOnItemClickListener(new AdminCustomerRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                loadUserSettingsFragment(customers.get(position));
            }
        });
    }

    void initSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.columns, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerColumn.setAdapter(adapter);
        binding.spinnerColumn.setOnItemSelectedListener(this);
        binding.spinnerColumn.setSelection(0);
    }

    void initButtons() {
        binding.buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupRecycler();
            }
        });
    }

    void setupRecycler() {
        customers = getCustomers();

        recyclerAdapter = new AdminCustomerRecyclerAdapter(customers);
        recycler.setAdapter(recyclerAdapter);
        recyclerAdapter.setOnItemClickListener(new AdminCustomerRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                loadUserSettingsFragment(customers.get(position));
            }
        });
    }


    private ArrayList<Customer> getCustomers() {
        try {
            return data.getCustomersForAdminView(column, binding.etSearch.getText().toString());
        }
        catch (Exception e) {
            System.err.println("_LOG: "+e);
            Toast.makeText(getContext(), "Couldn't load customers.", Toast.LENGTH_SHORT).show();
            return new ArrayList<>();
        }
    }

    // Spinner methods
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        column = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        column = "username";
    }

    private void loadUserSettingsFragment(Customer customer) {
        viewModel.setCustomer(customer);
        AdminActivity act = (AdminActivity)getActivity();
        act.loadFragment(new AdminCustomerSettingsFragment());
    }

}
