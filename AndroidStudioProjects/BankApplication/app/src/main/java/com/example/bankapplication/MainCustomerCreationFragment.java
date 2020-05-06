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

import java.sql.ResultSet;

import com.example.bankapplication.databinding.FragmentMainCustomerCreationBinding;

public class MainCustomerCreationFragment extends Fragment {
    private SharedViewModelMain viewModel;
    private FragmentMainCustomerCreationBinding binding;
    private DataManager data;
    private Bank bank;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainCustomerCreationBinding.inflate(inflater, container, false);
        data = DataManager.getInstance();
        bank = Bank.getInstance();
        initElements();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null)
            viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModelMain.class);
    }

    private void initElements() {
        initButtons();
    }

    private void initButtons() {
        binding.buttonSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeAccountRequest();
            }
        });
    }

    // Check if account request can be sent here
    private void initializeAccountRequest() {
        if (passwordCheck() && fieldsCheck()) {
            createAccount(binding.etUsername.getText().toString(), binding.etName.getText().toString(),
                    binding.etPassword.getText().toString(), binding.etSocialId.getText().toString(),
                    binding.etAddress.getText().toString(), binding.etPhonenumber.getText().toString(),
                    binding.etZipcode.getText().toString());
        }
    }

    private void createAccount(String userName, String name, String password, String socialid, String address, String phoneNumber, String zipcode) {
        try {
            // Instance where there isn't an account called this in the database
            if (!data.customerAlreadyExists(viewModel.getBankId(), userName)) {
                // Create hashed password
                String salt = Hasher.getRandomSalt();
                String hashPass = Hasher.hashPassword(password, salt);

                // Add to database
                data.createCustomerRequest(userName, name, phoneNumber, hashPass, bank.getId(), address, zipcode, socialid, salt);
                Toast.makeText(getContext(), "Account request created.", Toast.LENGTH_LONG).show();
            }
            // Instance where there is
            else {
                Toast.makeText(getContext(), "Account called \"" + userName + "\" already exists in this bank.", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e) {
            System.err.println("_LOG: "+e);
            Toast.makeText(getContext(), "Error creating account request.", Toast.LENGTH_LONG).show();
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
        if (binding.etUsername.getText().toString().equals("") || binding.etUsername.getText().toString().equals("admin")) {
            isValid = false;
            binding.etUsername.setError("Enter a valid username");
        }
        return isValid;
    }
}
