package com.example.bankapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.bankapplication.databinding.FragmentStartBinding;

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
                viewModel.setBankName("snorkkeli");
                viewModel.setBankId(1);
                bank.setId(1);
                MainActivity m = (MainActivity)getActivity();
                m.loadFragment(new LoginFragment());
            }
        });
    }
}
