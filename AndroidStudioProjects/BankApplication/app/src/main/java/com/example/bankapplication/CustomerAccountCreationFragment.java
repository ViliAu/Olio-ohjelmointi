package com.example.bankapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.bankapplication.databinding.FragmentCustomerAccountCreationBinding;

import java.sql.Time;
import java.util.Date;

public class CustomerAccountCreationFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private FragmentCustomerAccountCreationBinding binding;
    private SharedViewModelCustomer viewModel;

    private int accountType = 0;
    private Bank bank = Bank.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCustomerAccountCreationBinding.inflate(inflater, container, false);
        initElements();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null)
            viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModelCustomer.class);
    }

    private void initElements() {
        initSpinner();
        initButtons();
        initCalendar();
    }

    void initSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.account_types, R.layout.spinner_item_account_type);
        adapter.setDropDownViewResource(R.layout.spinner_item_account_type);
        binding.spinner.setAdapter(adapter);
        binding.spinner.setOnItemSelectedListener(this);
        binding.spinner.setSelection(0);
    }

    private void initButtons() {
        binding.buttonAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAccountCreation();
            }
        });
    }

    private void initCalendar() {
        Date date = new Date();
        binding.cwDueDate.setMinDate(date.getTime());
    }

    private void checkAccountCreation() {
        if (checkFields()) {
            try {
                java.sql.Date date = new java.sql.Date(binding.cwDueDate.getDate());
                bank.createAccountRequest(accountType, viewModel.getBankId(), viewModel.getCustomerId(),
                        binding.etAccountName.getText().toString(),
                        Float.parseFloat(binding.etCreditLimit.getText().toString()), date);
            }
            catch (Exception e) {
                System.out.println("_LOG: "+e);
                Toast.makeText(getContext(), "Error creating account!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkFields() {
        boolean canCreate = true;
        if (binding.etAccountName.getText().toString().trim().isEmpty()) {
            canCreate = false;
            binding.etAccountName.setError("Account name cannot be empty.");
        }
        if (accountType != 2) {
            binding.etCreditLimit.setText("0");
        }
        else if (accountType == 2 && binding.etCreditLimit.getText().toString().trim().isEmpty()) {
            canCreate = false;
            binding.etAccountName.setError("Credit limit cannot be empty");
        }
        return canCreate;
    }

    // Show info of selected account type only
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        binding.etCreditLimit.setVisibility(View.INVISIBLE);
        binding.twBankInterest.setVisibility(View.INVISIBLE);
        binding.containerFixedterm.setVisibility(View.INVISIBLE);
        int accType = position + 1;
        switch (accType) {
            // Credit account
            case 2:
                binding.etCreditLimit.setVisibility(View.VISIBLE);
                break;
            // Savings account
            case 3:
                binding.twBankInterest.setVisibility(View.VISIBLE);
                break;
            // Fixed term account
            case 4:
                binding.containerFixedterm.setVisibility(View.VISIBLE);
                break;

        }

        this.accountType = accType;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
