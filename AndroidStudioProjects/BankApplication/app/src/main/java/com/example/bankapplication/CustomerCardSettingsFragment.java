package com.example.bankapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.bankapplication.databinding.FragmentCustomerCardSettingsBinding;

public class CustomerCardSettingsFragment extends Fragment {
    private FragmentCustomerCardSettingsBinding binding;
    private SharedViewModelCustomer viewModel;
    private Bank bank;
    private Card card;
    private DataManager data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCustomerCardSettingsBinding.inflate(inflater, container, false);
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
        card = viewModel.getCardToEdit();
        initEditors();
        initSpinner();
        initButtons();
    }

    private void initEditors() {
        binding.etCardName.setText(card.getName());
        binding.etPayLimit.setText(String.valueOf(card.getPayLimit()));
        binding.etWithdrawLimit.setText(String.valueOf(card.getWithdrawLimit()));
    }

    private void initSpinner() {
        ArrayAdapter<CharSequence> countryAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.countries, R.layout.spinner_item_account_type);
        countryAdapter.setDropDownViewResource(R.layout.spinner_item_account_type);
        binding.spinner.setAdapter(countryAdapter);
        binding.spinner.setSelection(card.getCountryLimit()-1);
    }

    private void initButtons() {
        binding.buttonSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSettings();
            }
        });
    }

    private void checkSettings() {
        boolean canUpdate = true;
        float withdrawAmount = 0;
        float payAmount = 0;
        try {
            withdrawAmount = Float.parseFloat(binding.etWithdrawLimit.getText().toString().trim());
            payAmount = Float.parseFloat(binding.etPayLimit.getText().toString().trim());
        }
        catch (Exception e) {
            System.out.println("_LOG: "+e);
            canUpdate = false;
        }
        if (withdrawAmount == 0) {
            binding.etWithdrawLimit.setError("Enter a valid amount.");
            canUpdate = false;
        }
        if (payAmount == 0) {
            binding.etWithdrawLimit.setError("Enter a valid amount.");
            canUpdate = false;
        }
        if (binding.etCardName.getText().toString().trim().equals("")) {
            binding.etCardName.setError("Enter a card name.");
            canUpdate = false;
        }
        if (canUpdate) {
            updateCardSettings(payAmount, withdrawAmount);
        }
    }

    private void updateCardSettings(float payAmount, float withdrawAmount) {
        try {
            data.updateCardSettings(binding.etCardName.getText().toString(), payAmount, withdrawAmount, binding.spinner.getSelectedItemPosition() + 1, card.getId());
            Toast.makeText(getContext(), "Card updated", Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            System.err.println("_LOG: "+e);
            Toast.makeText(getContext(), "Error updating card settings.", Toast.LENGTH_LONG).show();
        }
    }
}
