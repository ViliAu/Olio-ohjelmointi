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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bankapplication.databinding.FragmentAdminStartBinding;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StartAdminFragment extends Fragment implements AdapterView.OnItemSelectedListener{
    private SharedViewModelAdmin viewModel;
    private FragmentAdminStartBinding binding;

    private String column = "accountname";
    private String searchWord = "";

    // UI ELEMENTS
    private RecyclerView recycler;
    private AdminRecyclerAdapter recyclerAdapter;
    private RecyclerView.LayoutManager recyclerLayoutManager;

    // LISTS
    private ArrayList<Customer> customers = new ArrayList<>();
    private ArrayList<AdminCustomersCard> recyclerCardList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminStartBinding.inflate(inflater, container, false);
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
        initRecyclerview();
        getCustomers();
        setupRecycler();
        initSpinner();
        initEditor();
        initButtons();
    }

    private void initRecyclerview() {
        recycler = binding.recyclerView;
        recycler.setHasFixedSize(true);
        recyclerLayoutManager = new LinearLayoutManager(getContext());
        recyclerAdapter = new AdminRecyclerAdapter(recyclerCardList);

        recycler.setLayoutManager(recyclerLayoutManager);
        recycler.setAdapter(recyclerAdapter);

        recyclerAdapter.setOnItemClickListener(new AdminRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                int id = 0;
                try {
                    id = Integer.parseInt(recyclerCardList.get(position).getId());
                }
                catch (Exception e) {
                    System.out.println("_LOG: "+e);
                    return;
                }
                viewModel.setCustomerId(id);
                AdminActivity m = (AdminActivity)getActivity();
                //m.loadFragment();
            }
        });
    }

    void initSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.columns, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerColumn.setAdapter(adapter);
        binding.spinnerColumn.setOnItemSelectedListener(this);
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
        recyclerCardList = getCards(customers);

        if (recyclerCardList != null)
            recyclerAdapter = new AdminRecyclerAdapter(recyclerCardList);
        recycler.setAdapter(recyclerAdapter);
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

    private ArrayList<AdminCustomersCard> getCards(ArrayList<Customer> customers) {
        ArrayList<AdminCustomersCard> cards = new ArrayList<>();
        for (Customer c : customers) {
            cards.add(new AdminCustomersCard(R.drawable.ic_account_circle_black_24dp, c.getAccountName(),
                    "ID: "+String.valueOf(c.getId()), "Bank: "+c.getBankString(),
                    "Owner: "+c.getName(), "Type: "+c.getAccTypeString()));
        }
        return cards;
    }

    // Spinner methods
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        column = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        column = "id";
    }
}
