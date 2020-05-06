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

import com.example.bankapplication.databinding.FragmentCustomerSettingsBinding;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerSettingsFragment extends Fragment {

    private FragmentCustomerSettingsBinding binding;
    private SharedViewModelCustomer viewModel;
    private String salt;
    private DataManager data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCustomerSettingsBinding.inflate(inflater, container, false);
        initElements();
        data = DataManager.getInstance();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null)
            viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModelCustomer.class);
        getAccountData();
    }

    private void initElements() {
        initButtons();
    }

    private void getAccountData() {
        Customer cust = null;
        try {
            cust = data.getSingleCustomer(viewModel.getCustomerId());
        }
        catch (Exception e) {
            System.err.println("_LOG: "+e);
            Toast.makeText(getContext(), "Error fetching account data.", Toast.LENGTH_SHORT).show();
        }
        if (cust != null) {
            binding.etName.setText(cust.getName());
            binding.etZipcode.setText(cust.getZipcode());
            binding.etSocialId.setText(cust.getSocialid());
            binding.etAddress.setText(cust.getAddress());
            binding.etPhonenumber.setText(cust.getPhoneNumber());
            salt = cust.getSalt();
        }
    }

    private void initButtons() {
        binding.buttonUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAccountUpdateInfo();
            }
        });
    }

    private void checkAccountUpdateInfo() {
        if (passwordCheck() && fieldsCheck()) {
            updateAccountInfo(binding.etName.getText().toString(),
                    binding.etPassword.getText().toString(), binding.etSocialId.getText().toString(),
                    binding.etAddress.getText().toString(), binding.etPhonenumber.getText().toString(),
                    binding.etZipcode.getText().toString());
        }
    }

    private void updateAccountInfo(String name, String password, String socialid, String address, String phoneNumber, String zipcode) {
        try {
            data.updateCustomerInfo(name, phoneNumber, Hasher.hashPassword(password, this.salt), address, zipcode, socialid, viewModel.getCustomerId());
            Toast.makeText(getContext(), "Customer information updated", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            System.err.println("_LOG: "+e);
            Toast.makeText(getContext(), "Couldn't update account info.", Toast.LENGTH_LONG).show();
        }
    }

    private boolean passwordCheck() {
        boolean isValid = true;
        String password = binding.etPassword.getText().toString().trim();
        if (password.length() == 0) {
            isValid = false;
            binding.etPassword.setError("Password cannot be empty.");
        }
        else {
            boolean number = false, upper = false, lower = false, special = false;
            for (char c : password.toCharArray()) {
                if (Character.isDigit(c))
                    number = true;
                else if (Character.isLetter(c))
                    if (Character.isUpperCase(c))
                        upper = true;
                    else if (Character.isLowerCase(c))
                        lower = true;
                    else
                        special = true;
            }
            if (!number || !upper || !lower || !special && password.length() < 12) {
                isValid = false;
                binding.inputPassword.setError("Password must contain at least 12 characters and have at least one" +
                        " lowercase, uppercase, digit and special character.");
            }
        }
        return isValid;
    }

    // Checks rest of the fields
    private boolean fieldsCheck() {
        boolean isValid = true;
        if (binding.etName.getText().toString().equals("")) {
            isValid = false;
            binding.etName.setError("Name cannot be empty.");
        }
        if (binding.etSocialId.getText().toString().equals("")) {
            isValid = false;
            binding.etSocialId.setError("Social ID cannot be empty.");
        }
        if (binding.etAddress.getText().toString().equals("")) {
            isValid = false;
            binding.etAddress.setError("Enter a valid address.");
        }
        if (binding.etZipcode.getText().toString().equals("")) {
            isValid = false;
            binding.etZipcode.setError("Enter a valid zip code.");
        }
        if (binding.etPhonenumber.getText().toString().equals("")) {
            isValid = false;
            binding.etPhonenumber.setError("Enter a valid phone number.");
        }
        return isValid;
    }
}
