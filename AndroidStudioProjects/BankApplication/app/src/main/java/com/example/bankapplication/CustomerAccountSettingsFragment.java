package com.example.bankapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.bankapplication.databinding.FragmentCustomerAccountSettingsBinding;

public class CustomerAccountSettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private FragmentCustomerAccountSettingsBinding binding;
    private SharedViewModelCustomer viewModel;

    private Bank bank;
    private TimeManager time;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCustomerAccountSettingsBinding.inflate(inflater, container, false);
        bank = Bank.getInstance();
        time = TimeManager.getInstance();
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
        initSpinner();
    }

    private void initSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.account_types_account_settings, R.layout.spinner_item_account_type);
        adapter.setDropDownViewResource(R.layout.spinner_item_account_type);
        binding.spinner.setAdapter(adapter);
        binding.spinner.setOnItemSelectedListener(this);
        binding.spinner.setSelection(0);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
