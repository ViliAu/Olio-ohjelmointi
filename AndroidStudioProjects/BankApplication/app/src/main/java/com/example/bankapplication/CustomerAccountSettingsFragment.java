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

import com.example.bankapplication.databinding.FragmentCustomerAccountSettingsBinding;

import java.util.Random;

public class CustomerAccountSettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private FragmentCustomerAccountSettingsBinding binding;
    private SharedViewModelCustomer viewModel;

    private Bank bank;
    private TimeManager time;
    private Account acc;
    private int type;
    private DataManager data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCustomerAccountSettingsBinding.inflate(inflater, container, false);
        bank = Bank.getInstance();
        time = TimeManager.getInstance();
        data = DataManager.getInstance();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null)
            viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModelCustomer.class);
        initElements();
    }

    private void initElements() {
        loadAccount();
        initSpinner();
        initTexts();
        initSwitch();
        initButtons();
    }

    private void loadAccount() {
        acc = viewModel.getAccountToEdit();
        type = acc.getType();
    }

    private void initTexts() {
        binding.etAccountName.setText(acc.getName());
        binding.etInfo.setText("0");
        if (!(acc instanceof CreditAccount)) {
            binding.twInfo.setVisibility(View.INVISIBLE);
            binding.etInfo.setVisibility(View.INVISIBLE);
        }
        else {
            binding.etInfo.setText(String.valueOf(((CreditAccount) acc).getCreditLimit()));
        }
    }

    private void initSwitch() {
        // Hide payment enabling if account is savings account
        if (acc instanceof SavingsAccount)
            binding.switchPaymentEnabled.setVisibility(View.INVISIBLE);
        else {
            boolean paymentState = (acc.getState() == 2);
            binding.switchPaymentEnabled.setChecked(paymentState);
        }
    }

    private void initButtons() {
        binding.buttonUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInfoUpdate();
            }
        });
        binding.buttonRequestCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCard();
            }
        });
    }

    private void checkInfoUpdate() {
        boolean canUpdate = true;
        if (acc.getBalance() < 0) {
            canUpdate = false;
            Toast.makeText(getContext(), "Account balance cannot be negative.", Toast.LENGTH_LONG).show();
        }
        if (binding.etAccountName.getText().toString().trim().equals("")) {
            canUpdate = false;
            binding.inputAccountName.setError("Account name cannot be empty.");
        }
        if (canUpdate) {
            updateInfo();
        }
    }

    private void requestCard() {
        try {
            if (data.hasPendingCard(acc.getAccountNumber())) {
                Toast.makeText(getContext(), "You have a pending card request.", Toast.LENGTH_LONG).show();
                return;
            }
        }
        catch (Exception e) {
            System.err.println("_LOG: "+e);
            Toast.makeText(getContext(), "Error creating card.", Toast.LENGTH_LONG).show();
        }
        String number = "";
        // Generate random number sequence
        for (int i = 0; i < 16; i++) {
            Random rand = new Random();
            number += String.valueOf(rand.nextInt(10));
        }
        // Check database for same card number
        try {
            if (data.exists("accounts", number))
                requestCard();
            data.createCardRequest(acc, number);
            Toast.makeText(getContext(), "Card requested.", Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            System.err.println("_LOG: "+e);
            Toast.makeText(getContext(), "Error creating card.", Toast.LENGTH_LONG).show();
        }
    }

    private void updateInfo() {
        float creditLimit = 0;
        if (type == 2) {
            try {
                creditLimit = Float.parseFloat(binding.etInfo.getText().toString());
                if (creditLimit < 0) {
                    Toast.makeText(getContext(), "Enter a valid credit amount.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            catch (Exception e) {
                Toast.makeText(getContext(), "Enter a valid credit amount.", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        int state = (binding.switchPaymentEnabled.isChecked()) ? 2 : 4;
        try {
            bank.updateAccount(acc.ID, binding.etAccountName.getText().toString(), type, creditLimit, acc.getAccountNumber(), state);
            updateAccounts();
            Toast.makeText(getContext(), "Account info updated.", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateAccounts() {
        CustomerActivity activity = (CustomerActivity)getActivity();
        viewModel.setAccounts(activity.updateAccounts());
    }

    private void initSpinner() {
        // Makes it so you can't change fixed term account to other types
        if (acc instanceof FixedTermAccount) {
            binding.twAccountType.setVisibility(View.INVISIBLE);
            binding.spinner.setVisibility(View.INVISIBLE);
            return;
        }
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.account_types_account_settings, R.layout.spinner_item_account_type);
        adapter.setDropDownViewResource(R.layout.spinner_item_account_type);
        binding.spinner.setAdapter(adapter);
        binding.spinner.setOnItemSelectedListener(this);
        binding.spinner.setSelection(type - 1);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        binding.switchPaymentEnabled.setEnabled(true);
        binding.switchPaymentEnabled.setChecked(true);
        binding.buttonUpdateInfo.setEnabled(true);
        binding.twInfo.setVisibility(View.INVISIBLE);
        binding.etInfo.setVisibility(View.INVISIBLE);
        if (position == 2) {
            binding.switchPaymentEnabled.setChecked(false);
            binding.switchPaymentEnabled.setEnabled(false);
        }
        else if (position == 3) {
            binding.twInfo.setVisibility(View.VISIBLE);
            binding.etInfo.setVisibility(View.VISIBLE);
        }
        type = position+1;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
