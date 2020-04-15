package com.example.bankapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bankapplication.databinding.FragmentAdminCustomerSettingsBinding;

public class AdminCustomerSettingsFragment extends Fragment {

    private FragmentAdminCustomerSettingsBinding binding;
    private SharedViewModelAdmin viewModel;

    private int id;

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
        if (viewModel.getCustomerId() != null)
        id = viewModel.getCustomerId().getValue();
    }

    private void initElements() {

    }
}
