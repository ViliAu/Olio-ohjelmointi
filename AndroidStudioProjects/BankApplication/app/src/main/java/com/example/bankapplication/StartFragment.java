package com.example.bankapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.bankapplication.databinding.FragmentStartBinding;

import java.sql.SQLException;

public class StartFragment extends Fragment {

    private SharedViewModelMain viewModel;
    private FragmentStartBinding binding;
    private Bank bank;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStartBinding.inflate(inflater, container, false);
        initButtons();
        bank = Bank.getInstance();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null)
            viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModelMain.class);
    }

    // Go to login view
    void initButtons() {
        binding.buttonSnorkkeli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBankInfo(1);
            }
        });
    }

    private void updateBankInfo(int id) {
        try {
            bank.setValues(1);
        }
        catch (Exception e) {
            Toast.makeText(getContext(), "Couldn't get bank information from database.", Toast.LENGTH_LONG).show();
            return;
        }
        MainActivity m = (MainActivity)getActivity();
        m.loadFragment(new LoginFragment());
    }
}
