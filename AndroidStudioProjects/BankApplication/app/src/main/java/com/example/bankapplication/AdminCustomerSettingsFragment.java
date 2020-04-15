package com.example.bankapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.bankapplication.databinding.FragmentAdminCustomerSettingsBinding;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminCustomerSettingsFragment extends Fragment {

    private FragmentAdminCustomerSettingsBinding binding;
    private SharedViewModelAdmin viewModel;
    private ResultSet rs;

    private int id = 0;
    private String userName = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminCustomerSettingsBinding.inflate(inflater, container, false);
        initElements();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null)
            viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModelAdmin.class);
        if (viewModel.getCustomerId() != 0) {
            id = viewModel.getCustomerId();
        }
        getCurrentAccount();
    }

    private void initElements() {
        initButtons();
    }

    // Buttons for changing account state. 1 = pending, 2 = Normal 3 = rejected
    private void initButtons() {
        binding.buttonAcceptCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeAccountState(2);
                Toast.makeText(getContext(), "Account accepted", Toast.LENGTH_SHORT).show();
            }
        });

        binding.buttonRejectCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeAccountState(3);
                Toast.makeText(getContext(), "Account disabled", Toast.LENGTH_SHORT).show();
            }
        });

        binding.buttonAcceptAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: CREATE ACCOUNT CLASS AND ACCEPT ACCOUNTS HERE
            }
        });

        binding.buttonRejectAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: CREATE ACCOUNT CLASS AND REJECT ACCOUNTS HERE
            }
        });
    }

    private void getCurrentAccount() {
        try {
            rs = DataBase.dataQuery("SELECT * FROM Henkilot WHERE id = "+id);
            if (rs != null) {
                binding.twAccountName.setText(rs.getString("accountname"));
            }
        }
        catch (SQLException e) {
            binding.twAccountName.setText("ERROR!");
            System.out.println("_LOG: " + e );
        }
    }

    private void changeAccountState(int state) {
        try {
            DataBase.dataUpdate("UPDATE Henkilot SET type = "+state+" WHERE id = "+id);
            if (rs != null) {
                binding.twAccountName.setText(rs.getString("accountname"));
            }
        }
        catch (SQLException e) {
            binding.twAccountName.setText("ERROR!");
            System.out.println("_LOG: " + e );
        }
    }
}
