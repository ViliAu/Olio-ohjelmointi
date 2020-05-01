package com.example.bankapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.bankapplication.databinding.FragmentCustomerAccountCreationBinding;
import java.sql.Date;
import java.util.Locale;

public class CustomerAccountCreationFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private FragmentCustomerAccountCreationBinding binding;
    private SharedViewModelCustomer viewModel;

    private int accountType = 0;
    private Bank bank;
    private TimeManager time;
    private Date currentDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCustomerAccountCreationBinding.inflate(inflater, container, false);
        bank = Bank.getInstance();
        time = TimeManager.getInstance();
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

    private void initSpinner() {
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
        currentDate = new Date(time.today());
        binding.cwDueDate.setMinDate(time.today());
        binding.cwDueDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                currentDate = new Date(time.parseDate
                        (String.format(Locale.GERMANY, "%d-%d-%d", year, month+1, dayOfMonth)));
            }
        });
    }

    private void checkAccountCreation() {
        if (checkFields()) {
            try {
                bank.createAccountRequest(accountType, viewModel.getCustomerId(),
                        binding.etAccountName.getText().toString(),
                        Float.parseFloat(binding.etCreditLimit.getText().toString()), currentDate);
                Toast.makeText(getContext(), "Account created.", Toast.LENGTH_LONG).show();
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
        else if (binding.etCreditLimit.getText().toString().trim().isEmpty()) {
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
