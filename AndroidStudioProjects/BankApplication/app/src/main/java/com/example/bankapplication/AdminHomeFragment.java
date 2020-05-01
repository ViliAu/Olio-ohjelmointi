package com.example.bankapplication;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.example.bankapplication.databinding.FragmentAdminHomeBinding;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdminHomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private SharedViewModelAdmin viewModel;
    private FragmentAdminHomeBinding binding;

    private String column = "accountname";
    private String searchWord = "";

    // Recycler view
    private RecyclerView recycler;
    private AdminCustomerRecyclerAdapter recyclerAdapter;
    private RecyclerView.LayoutManager recyclerLayoutManager;

    // LISTS
    private ArrayList<Customer> customers = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminHomeBinding.inflate(inflater, container, false);
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
        initEditor();
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
                viewModel.setCustomerId(customers.get(position).getId());
                loadUserSettingsFragment();
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

    void initEditor() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchWord = binding.etSearch.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
                viewModel.setCustomerId(customers.get(position).getId());
                loadUserSettingsFragment();
            }
        });
    }


    private ArrayList<Customer> getCustomers() {
        ArrayList <Customer> searchedCustomers = new ArrayList<>();
        try {
            ResultSet rs;
            if (column.equals("id") || column.equals("type") || column.equals("bank_id"))
                rs = DataBase.dataQuery("SELECT * FROM Henkilot WHERE " + column + " = " + searchWord + " AND NOT id = 1 ORDER BY id ASC");
            else
                rs = DataBase.dataQuery("SELECT * FROM Henkilot WHERE " + column + " LIKE '%" + searchWord + "%' AND NOT id = 1 ORDER BY id ASC");
            if (rs != null) {
                do {
                    System.out.println("_LOG: Adding to customer list: "+rs.getString("accountname"));
                    searchedCustomers.add(new Customer(rs.getInt("id"), rs.getInt("type"),
                            rs.getString("accountname"), rs.getString("name"), rs.getString("address"),
                            rs.getString("zipcode"), rs.getString("phonenumber"), rs.getString("socialid"),
                            rs.getInt("bank_id")));

                } while (rs.next());
            }
        }
        catch (SQLException e) {
            System.out.println("_LOG: "+e);
        }
        return searchedCustomers;
    }

    // Spinner methods
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        column = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        column = "accountname";
    }

    private void loadUserSettingsFragment() {
        AdminActivity m = (AdminActivity)getActivity();
        m.loadFragment(new AdminCustomerSettingsFragment());
    }

}
