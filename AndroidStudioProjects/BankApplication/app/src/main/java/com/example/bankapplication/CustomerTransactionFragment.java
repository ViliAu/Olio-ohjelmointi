package com.example.bankapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.bankapplication.databinding.FragmentCustomerTransactionBinding;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;

public class CustomerTransactionFragment extends Fragment {

    private FragmentCustomerTransactionBinding binding;
    private SharedViewModelCustomer viewModel;
    private ArrayAdapter<Account> accountAdapter;
    private ArrayList<SpinnerAccount> accs;
    private Bank bank;
    private Date currentDate;
    private TimeManager time;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCustomerTransactionBinding.inflate(inflater, container, false);
        initElements();
        bank = Bank.getInstance();
        time = TimeManager.getInstance();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null)
            viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModelCustomer.class);
        initSpinner();
    }

    private void initElements() {
        initButtons();
        initCalendarView();
    }

    private void initButtons() {
        binding.buttonMakeTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkTransaction();
            }
        });
    }

    private void initCalendarView() {
        binding.cwDueDate.setMinDate(binding.cwDueDate.getDate());
        currentDate = new Date(binding.cwDueDate.getDate());
        binding.cwDueDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                currentDate = new Date(time.parseDate
                        (String.format(Locale.GERMANY, "%d-%d-%d", year, month+1, dayOfMonth)));
            }
        });
    }

    private void initSpinner() {
        accountAdapter = new ArrayAdapter(getContext(), R.layout.spinner_item_account_customer, getPayableAccounts());
        accountAdapter.setDropDownViewResource(R.layout.spinner_item_account_customer);
        binding.spinner.setAdapter(accountAdapter);
        binding.spinner.setSelection(0);
    }

    // Get all the user's accounts that have buying enabled
    private ArrayList<SpinnerAccount> getPayableAccounts() {
        accs = new ArrayList<>();
        for (Account a : viewModel.getAccounts()) {
            if (a.state == 2) {
                accs.add(new SpinnerAccount(a.getAccountNumber(), a.getBalance()));
            }
        }
        return accs;
    }

    // Check if the amount is correct
    private void checkTransaction() { //TODO: Move to bank class
        boolean canTransfer = true;
        float amount = 0;
        try {
            amount = Float.parseFloat(binding.etAmount.getText().toString());
        }
        catch (Exception e) {
            canTransfer = false;
            binding.etAmount.setError("Input a valid amount.");
        }
        if (amount <= 0) {
            canTransfer = false;
            binding.etAmount.setError("Input a positive amount.");
        }
        // Check if the receiving account exists
        try {
            ResultSet rs = DataBase.dataQuery("SELECT * FROM accounts WHERE address = '" + binding.etAccountTo.getText().toString() + "' ");
            if (rs != null) {
                if (rs.getInt("state") == 1 || rs.getInt("state") == 3) {
                    System.out.println("_LOG: "+rs.getInt("state"));
                    binding.etAccountTo.setError("Account hasn't been approved or has been disabled by the administration.");
                    canTransfer = false;
                }
            }
            else {
                binding.etAccountTo.setError("Account doesn't exist");
                canTransfer = false;
            }
        }
        catch (SQLException e) {
            System.out.println("_LOG: "+e);
            Toast.makeText(getContext(), "Something went wrong. Please try again."+e, Toast.LENGTH_LONG).show();
        }
        // Check if money is being transferred to the same account
        if (binding.etAccountTo.equals(accs.get(binding.spinner.getSelectedItemPosition()).getAccountNumber())) {
            canTransfer = false;
            Toast.makeText(getContext(), "The Sending and receiving accounts cannot be same.", Toast.LENGTH_LONG).show();
        }
        if (canTransfer) {
            String toast = "";
            toast = bank.transferMoney(accs.get(binding.spinner.getSelectedItemPosition()).getAccountNumber(),
                    binding.etAccountTo.getText().toString(), amount,
                    currentDate,binding.switchReoccuring.isChecked(), binding.etMessage.getText().toString());
            Toast.makeText(getContext(), toast, Toast.LENGTH_LONG).show();
            CustomerActivity ca = (CustomerActivity)getActivity();
            viewModel.setAccounts(ca.updateAccounts());
            initSpinner();
        }
    }

    private class SpinnerAccount {
        String text;
        String number;
        private SpinnerAccount(String accountNumber, float balance) {
            this.number = accountNumber;
            this.text = String.format(Locale.GERMANY, "%s,  Balance: %.2f", accountNumber, balance);
        }
        @Override
        public String toString() {
            return text;
        }
        private String getAccountNumber() {
            return number;
        }
    }
}
