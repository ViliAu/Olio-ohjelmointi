package com.example.bankapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.bankapplication.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {
    private SharedViewModelMain viewModel;
    private FragmentLoginBinding binding;
    private Bank bank;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        bank = Bank.getInstance();
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
                loginBank(binding.etUsername.getText().toString(), binding.etPassword.getText().toString());
            }
        });
    }

    private void loadAccRequestFragment() {
        MainActivity m = (MainActivity) getActivity();
        m.loadFragment(new MainCustomerCreationFragment());
    }

    private void loginBank(String name, String pass) {
        LoginManager log = new LoginManager();
        int[] result;
        result = log.login(name, pass, bank.getId());
        switch (result[0]) {
            case 1:
                binding.inputUsername.setError("Username not found");
                break;
            case 2:
                binding.inputPassword.setError("Password incorrect.");
                break;
            case 3:
                Toast.makeText(getContext(), "Customer hasn't been approved by administration yet.", Toast.LENGTH_LONG).show();
                break;
            case 4:
                MainActivity m = (MainActivity)getActivity();
                m.loadCustomerActivity(result[1]);
                break;
            case 5:
                Toast.makeText(getContext(), "Customer has been disabled by the administration.", Toast.LENGTH_LONG).show();
                break;
            case 6:
                MainActivity m2 = (MainActivity)getActivity();
                m2.loadAdminActivity();
                break;
            default:
                break;
        }
    }
}
