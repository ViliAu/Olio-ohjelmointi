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

import com.example.bankapplication.databinding.FragmentCustomerCardSimulationsBinding;

import java.util.ArrayList;

public class CustomerCardSimulationsFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private FragmentCustomerCardSimulationsBinding binding;
    private SharedViewModelCustomer viewModel;
    private ArrayList<Card> cards = new ArrayList<>();
    private ArrayAdapter<Card> cardAdapter;
    private Account acc;
    private Bank bank;
    private DataManager data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCustomerCardSimulationsBinding.inflate(inflater, container, false);
        bank = Bank.getInstance();
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
        acc = viewModel.getAccountToEdit();
        cards = viewModel.getAccountCards();
        initSpinners();
        initButtons();
    }

    private void initSpinners() {
        ArrayAdapter<CharSequence> simulationAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.simulations, R.layout.spinner_item_account_type);
        simulationAdapter.setDropDownViewResource(R.layout.spinner_item_account_type);
        binding.spinnerSimulation.setAdapter(simulationAdapter);
        binding.spinnerSimulation.setOnItemSelectedListener(this);
        binding.spinnerSimulation.setSelection(0);

        ArrayAdapter<CharSequence> countryAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.countries, R.layout.spinner_item_account_type);
        countryAdapter.setDropDownViewResource(R.layout.spinner_item_account_type);
        binding.spinnerCountry.setAdapter(countryAdapter);
        binding.spinnerCountry.setSelection(0);

        cardAdapter = new ArrayAdapter(getContext(), R.layout.spinner_item_account_customer, cards);
        cardAdapter.setDropDownViewResource(R.layout.spinner_item_account_customer);
        binding.spinnerCard.setAdapter(cardAdapter);
        binding.spinnerCard.setSelection(0);
    }

    private void initButtons() {
        binding.buttonSimulate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSimulate();
            }
        });
        binding.buttonCardSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSettings();
            }
        });
    }

    private void gotoSettings() {
        CustomerActivity act = (CustomerActivity)getActivity();
        viewModel.setCardToEdit(cards.get(binding.spinnerCard.getSelectedItemPosition()));
        act.loadFragment(new CustomerCardSettingsFragment());
    }

    // Check if we can pay with given settings
    private void checkSimulate() {
        boolean canSimulate = true;
        float amount = 1;
        Card card = cards.get(binding.spinnerCard.getSelectedItemPosition());
        if (card.getState() == 4 && binding.spinnerSimulation.getSelectedItemPosition() == 2) {
            Toast.makeText(getContext(), "Card has paying disabled.", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            amount = Float.parseFloat(binding.etAmount.getText().toString().trim());
        }
        catch (Exception e) {
            binding.etAmount.setError("Enter a valid amount");
            canSimulate = false;
        }
        if (amount <= 0) {
            binding.etAmount.setError("Enter a positive amount.");
            canSimulate = false;
        }
        if (binding.spinnerCountry.getSelectedItemPosition()+1 > card.getCountryLimit()) {
            Toast.makeText(getContext(), "Region not supported by card's country limitation.", Toast.LENGTH_LONG).show();
            canSimulate = false;
        }
        // Paying with card
        if (binding.spinnerSimulation.getSelectedItemPosition() == 2) {
            if (binding.etTo.getText().toString().trim().equals("")) {
                binding.etTo.setError("Input a receiver name.");
                canSimulate = false;
            }
            if (card.getPaid() + amount > card.getPayLimit()) {
                Toast.makeText(getContext(), "Amount exceeds card pay limit.", Toast.LENGTH_LONG).show();
                canSimulate = false;
            }
            if (acc.getBalance() < amount) {
                canSimulate = false;
                Toast.makeText(getContext(), "Not enough money!", Toast.LENGTH_LONG).show();
            }
        }
        // Withdrawing with card
        else if (binding.spinnerSimulation.getSelectedItemPosition() == 1){
            if (card.getWithdrawn() + amount > card.getWithdrawLimit()) {
                Toast.makeText(getContext(), "Amount exceeds card withdraw limit.", Toast.LENGTH_LONG).show();
                canSimulate = false;
            }
            if (acc.getBalance() < amount) {
                canSimulate = false;
                Toast.makeText(getContext(), "Not enough money!", Toast.LENGTH_LONG).show();
            }
        }
        if (canSimulate) {
            runSimulation(amount, card);
        }
    }

    private void runSimulation(float amount, Card card) {
        try {
            if (binding.spinnerSimulation.getSelectedItemPosition() != 2) {
                bank.cardAction(binding.spinnerSimulation.getSelectedItemPosition(), acc.accountNumber, amount, card.getId());
            }
            else {
                bank.cardPayment(acc.accountNumber, amount, card.getId(), binding.etTo.getText().toString().trim());
            }
            Toast.makeText(getContext(), "Simulation successful.", Toast.LENGTH_LONG).show();
            CustomerActivity act = (CustomerActivity)getActivity();
            viewModel.setAccounts(act.updateAccounts());
            viewModel.setAccountCards(data.getAccountCards(viewModel.getAccountToEdit()));
            cards = viewModel.getAccountCards();
        }
        catch (Exception e) {
            Toast.makeText(getContext(), "Error trying to simulate.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                binding.twSimType.setText("Deposit");
                binding.etTo.setVisibility(View.GONE);
                binding.twTo.setVisibility(View.GONE);
                break;
            case 1:
                binding.twSimType.setText("Withdraw");
                binding.etTo.setVisibility(View.GONE);
                binding.twTo.setVisibility(View.GONE);
                break;
            case 2:
                binding.twSimType.setText("Paying with card");
                binding.etTo.setVisibility(View.VISIBLE);
                binding.twTo.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
