package com.example.bankapplication;

import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.ResultSet;

import com.example.bankapplication.databinding.FragmentAccountCreationBinding;

public class AccountCreationFragment extends Fragment {
    private SharedViewModelMain viewModel;
    private FragmentAccountCreationBinding binding;

    // Account vars
    private int id = 0, type = 0;
    private String userName = "", password = "", name = "", address = "", zipcode = "", phoneNumber = "", salt = "", socialid = "";
    private String bank;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAccountCreationBinding.inflate(inflater, container, false);
        initElements();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null)
            viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModelMain.class);
        if (viewModel.getBankName().getValue() != null)
            bank = viewModel.getBankName().getValue().toString();
    }

    private void initElements() {
        initButtons();
        initTextEditors();
    }

    private void initButtons() {
        binding.buttonSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeAccountRequest();
            }
        });
    }

    private void initTextEditors() {
        // Init password and legality check

    }

    // Check if account request can be sent here
    private void initializeAccountRequest() {
        boolean canCreate = true;
        if (!passwordCheck())
            //canCreate = false;
        if (!fieldsCheck())
            //canCreate = false;

        if (canCreate) {
            this.name = binding.etName.getText().toString();
            this.socialid = binding.etSocialId.getText().toString();
            this.address = binding.etAddress.getText().toString();
            this.zipcode = binding.etZipcode.getText().toString();
            this.phoneNumber = binding.etPhonenumber.getText().toString();
            this.userName = binding.etUsername.getText().toString();
            this.password = binding.etPassword.getText().toString();
        }

        // Query database to see if there's already an account called this.
        if (canCreate) {
            ResultSet rs = DataBase.dataQuery("SELECT * FROM henkilot WHERE bank_id = '" + viewModel.getBankId().getValue() + "' AND accountname = '" + userName + "' ");
            if (rs == null) {
                // Create hashed password
                String salt = Hasher.getRandomSalt();
                System.out.println("_LOG: "+salt);
                String hashPass = Hasher.hashPassword(password, salt);
                // Add to database
                DataBase.dataInsert("INSERT INTO henkilot VALUES (" + (DataBase.getTableLength("henkilot") + 1) + ", '" + userName + "', '" + name + "', '" + phoneNumber + "', '" + hashPass + "', " + viewModel.getBankId().getValue() + ", '" + address + "', '" + zipcode + "', '" + socialid + "', " + 1 + ", '"+salt+"')");
            }
                else {
                Toast toast = Toast.makeText(getContext(), "Account called \"" + userName + "\" already exists in this bank.", Toast.LENGTH_LONG);
                toast.show();
            }

        }
    }

    private boolean passwordCheck() {
        boolean isValid = true;
        String password = binding.etPassword.getText().toString();
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
