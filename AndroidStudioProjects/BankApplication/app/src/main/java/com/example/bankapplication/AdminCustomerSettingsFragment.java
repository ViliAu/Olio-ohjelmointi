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

import com.example.bankapplication.databinding.FragmentAdminCustomerSettingsBinding;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;

public class AdminCustomerSettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private FragmentAdminCustomerSettingsBinding binding;
    private SharedViewModelAdmin viewModel;
    private ResultSet rs;

    private int id = 0;
    private int accountId = 0;
    private ArrayList<AccountListElement> accList = new ArrayList<>();
    private ArrayAdapter<AccountListElement> accountAdapter;

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
        loadAccounts();
        initSpinner();
    }

    private void initElements() {
        initButtons();
        initSpinner();
    }

    private void loadAccounts() {
        try {
            ResultSet rs = DataBase.dataQuery("SELECT * FROM accounts WHERE owner_id = "+id+" ORDER BY 'due_date' DESC");
            if (rs != null) {
                do {
                    accList.add(new AccountListElement(rs.getString("name"),
                            rs.getString("address"), rs.getInt("id"),
                            rs.getInt("type"), rs.getInt("state"),
                            rs.getFloat("money_amount")));
                } while (rs.next());
            }
        }
        catch (SQLException e) {
            System.out.println("_LOG: "+e);
            Toast.makeText(getContext(), "Couldn't load customer accounts", Toast.LENGTH_SHORT).show();
        }
    }

    // Buttons for changing account state. 1 = pending, 2 = Normal 3 = rejected
    private void initButtons() {
        binding.buttonAcceptCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCustomerState(2);
                Toast.makeText(getContext(), "Customer enabled", Toast.LENGTH_SHORT).show();
            }
        });

        binding.buttonRejectCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCustomerState(3);
                Toast.makeText(getContext(), "Customer disabled", Toast.LENGTH_SHORT).show();
            }
        });

        binding.buttonAcceptAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accountId != 0) {
                    changeAccountState(2);
                    Toast.makeText(getContext(), "Account enabled", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.buttonRejectAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accountId != 0) {
                    changeAccountState(3);
                    Toast.makeText(getContext(), "Account enabled", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initSpinner() {
        accountAdapter = new ArrayAdapter(getContext(), R.layout.spinner_item_account_type, accList);
        accountAdapter.setDropDownViewResource(R.layout.spinner_item_account_type);
        binding.spinner.setAdapter(accountAdapter);
        binding.spinner.setOnItemSelectedListener(this);
        binding.spinner.setSelection(0);
    }

    private void getCurrentAccount() {
        try {
            rs = DataBase.dataQuery("SELECT * FROM Henkilot WHERE id = "+id);
            if (rs != null) {
                binding.twCustomerName.setText(rs.getString("accountname"));
            }
        }
        catch (SQLException e) {
            binding.twCustomerName.setText("ERROR!");
            System.out.println("_LOG: " + e );
        }
    }

    private void changeCustomerState(int state) {
        DataBase.dataUpdate("UPDATE Henkilot SET type = "+state+" WHERE id = "+id);
    }

    private void changeAccountState(int state) {
        DataBase.dataUpdate("UPDATE accounts SET state = "+state+" WHERE id = "+accountId);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        try {
            binding.twAccountName.setText("Account name: " + accList.get(position).name);
            binding.twAccountNumber.setText("Account number: " + accList.get(position).accountNumber);
            binding.twAccountId.setText(String.format(Locale.GERMANY, "Account id: %d", accList.get(position).id));
            binding.twAccountBalance.setText(String.format(Locale.GERMANY, "Account balance: %.2f", accList.get(position).balance));
            binding.twAccountState.setText("Account state: " + accList.get(position).getStateString(accList.get(position).state));
            binding.twAccountType.setText(String.format(Locale.GERMANY, "Account type: %d", accList.get(position).type));
        }
        catch (IndexOutOfBoundsException ie) {
            System.out.println("_LOG: "+ie);
        }
        catch (Exception e) {
            System.out.println("_LOG: "+e);
        }
        accountId = accList.get(position).id;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class AccountListElement {
        String name, accountNumber;
        int id;
        int type;
        int state;
        float balance;
        private AccountListElement(String name, String accountNumber, int id, int type,
                                   int state, float balance) {
            this.name = name;
            this.accountNumber = accountNumber;
            this.id = id;
            this.type = type;
            this.state = state;
            this.balance = balance;
        }


        @Override
        public String toString() {
            return accountNumber + "\n State: "+getStateString(state);
        }

        private String getStateString(int s) {
            switch(s) {
                case 1:
                    return "Pending";
                case 2:
                    return "Normal";
                case 3:
                    return "Disabled";
                case 4:
                    return "Payment disabled";
                default:
                    return "???";
            }
        }
    }
}
