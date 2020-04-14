package com.example.bankapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.bankapplication.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {
    private SharedViewModelMain viewModel;
    private FragmentLoginBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        initElements();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null)
            viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModelMain.class);
    }

    private void initElements() {
        initButtons();
    }

    private void initButtons() {
        binding.buttonAccountRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAccRequestFragment();
            }
        });

        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // LOGIN FUNCTION(ALITY) HERE
            }
        });
    }

    private void loadAccRequestFragment() {
        MainActivity m = (MainActivity)getActivity();
        m.loadFragment(new AccountCreationFragment());
    }
}
