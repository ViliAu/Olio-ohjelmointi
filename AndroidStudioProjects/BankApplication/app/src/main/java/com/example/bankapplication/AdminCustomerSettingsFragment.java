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

import java.util.ArrayList;
import java.util.Locale;

public class AdminCustomerSettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private FragmentAdminCustomerSettingsBinding binding;
    private SharedViewModelAdmin viewModel;

    private int accountId = 0;
    private int accType = 0;
    private String accountNumber = "";
    private ArrayList<AccountListElement> accList = new ArrayList<>();
    private ArrayAdapter<AccountListElement> accountAdapter;
    private ArrayList<Card> cards = new ArrayList<>();
    private ArrayAdapter<Card> cardAdapter;
    private DataManager data;
    private Customer customer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminCustomerSettingsBinding.inflate(inflater, container, false);
        data = DataManager.getInstance();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null)
            viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModelAdmin.class);
        loadData();
        initElements();
    }

    private void initElements() {
        initButtons();
        initSpinner();
        initText();
    }

    private void loadData() {
        customer = viewModel.getCustomer();
        try {
            accList = data.loadAccounts(customer.getId());
        }
        catch (Exception e) {
            System.err.println("_LOG: "+e);
            Toast.makeText(getContext(), "Error loading customers.", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getContext(), "Account disabled", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.buttonAcceptCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCardState(2);
                Toast.makeText(getContext(), "Account cards enabled", Toast.LENGTH_SHORT).show();
            }
        });

        binding.buttonRejectCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCardState(3);
                Toast.makeText(getContext(), "Account cards disabled", Toast.LENGTH_SHORT).show();
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

    private void initCardSpinner() {
        cardAdapter = new ArrayAdapter(getContext(), R.layout.spinner_item_account_customer, cards);
        cardAdapter.setDropDownViewResource(R.layout.spinner_item_account_customer);
        binding.spinnerCard.setAdapter(cardAdapter);
        binding.spinnerCard.setSelection(0);
    }

    private void initText() {
        binding.twCustomerName.setText(customer.getName());
        if (accList.isEmpty()) {
            binding.twAccountName.setVisibility(View.GONE);
            binding.twAccountNumber.setVisibility(View.GONE);
            binding.twAccountId.setVisibility(View.GONE);
            binding.twAccountBalance.setVisibility(View.GONE);
            binding.twAccountState.setVisibility(View.GONE);
            binding.twAccountType.setVisibility(View.GONE);
            binding.buttonAcceptAccount.setVisibility(View.GONE);
            binding.buttonRejectAccount.setVisibility(View.GONE);
            binding.twAccount.setVisibility(View.GONE);
            binding.spinner.setVisibility(View.GONE);
        }
    }

    private void changeCustomerState(int state) {
        try {
            data.updateState("customers", state, customer.getId());
            if (state == 2)
                Toast.makeText(getContext(), "Customer enabled.", Toast.LENGTH_LONG).show();
            else if (state == 3)
                Toast.makeText(getContext(), "Customer disabled.", Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            System.err.println("_LOG: "+e);
            Toast.makeText(getContext(), "Couldn't update customer state.", Toast.LENGTH_LONG).show();
        }
    }

    private void changeAccountState(int state) {
        try {
            // Change state to payment disabled if the acc is savings or fixed term
            if (state == 2)
                state = ((accType == 3 || accType == 4)) ? 4 : 2;

            data.updateState("accounts", state, accountId);
            if (state == 2 || state == 4)
                Toast.makeText(getContext(), "Account enabled.", Toast.LENGTH_LONG).show();
            else if (state == 3)
                Toast.makeText(getContext(), "Account disabled.", Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            System.err.println("_LOG: "+e);
            Toast.makeText(getContext(), "Couldn't update account state.", Toast.LENGTH_LONG).show();
        }
    }

    private void changeCardState(int state) {
        try {
            data.updateState( "cards", state, cards.get(binding.spinnerCard.getSelectedItemPosition()).getId());
            if (state == 2)
                Toast.makeText(getContext(), "Card enabled.", Toast.LENGTH_LONG).show();
            else if (state == 3)
                Toast.makeText(getContext(), "Card disabled.", Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            System.err.println("_LOG: "+e);
            Toast.makeText(getContext(), "Couldn't update card state.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        accountId = accList.get(position).getId();
        accType = accList.get(position).getType();
        accountNumber = accList.get(position).getAccountNumber();
        try {
            binding.twAccountName.setText("Account name: " + accList.get(position).getName());
            binding.twAccountNumber.setText("Account number: " + accList.get(position).getAccountNumber());
            binding.twAccountId.setText(String.format(Locale.GERMANY, "Account id: %d", accList.get(position).getId()));
            binding.twAccountBalance.setText(String.format(Locale.GERMANY, "Account balance: %.2f", accList.get(position).getBalance()));
            binding.twAccountState.setText("Account state: " + accList.get(position).getStateString());
            binding.twAccountType.setText(String.format(Locale.GERMANY, "Account type: %d", accList.get(position).getType()));
            getCards(accountNumber);
            if (!cards.isEmpty()) {
                binding.buttonAcceptCards.setVisibility(View.VISIBLE);
                binding.buttonRejectCards.setVisibility(View.VISIBLE);
                binding.twCard.setVisibility(View.VISIBLE);
                binding.spinnerCard.setVisibility(View.VISIBLE);
                initCardSpinner();
            }
        }
        catch (IndexOutOfBoundsException ie) {
            System.err.println("_LOG: "+ie);
        }
        catch (Exception e) {
            System.err.println("_LOG: "+e);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void getCards(String accountNumber) throws Exception {
        cards = data.getAccountCardsAdmin(accountNumber);
    }
}
