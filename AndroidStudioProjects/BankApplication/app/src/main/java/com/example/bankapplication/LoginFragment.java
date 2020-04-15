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

import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;

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
                loginBank(binding.etUsername.getText().toString(), binding.etPassword.getText().toString());
            }
        });
    }

    private void loadAccRequestFragment() {
        MainActivity m = (MainActivity) getActivity();
        m.loadFragment(new AccountCreationFragment());
    }

    private void loginBank(String name, String pass) {
        ResultSet rs;
        System.out.println("_LOG: "+Hasher.hashPassword("admin", "admin"));
        try {
            rs = DataBase.dataQuery("SELECT * FROM henkilot WHERE accountname = '" + name + "' AND (bank_id =" + viewModel.getBankId().getValue() + " OR bank_id=0)");
            if (rs == null) {
                binding.inputUsername.setError("Username not found");
                return;
            }
            String salt = rs.getString("salt");
            if (!Hasher.testPassword(pass, rs.getString("password"), salt)) {
                binding.inputPassword.setError("Password incorrect.");
                return;
            }
            int accountType = rs.getInt("type");
            if (accountType == 1) {
                binding.inputUsername.setError("Username hasn't been approved by administration yet.");
                Toast.makeText(getContext(), "Username hasn't been approved by administration yet.", Toast.LENGTH_SHORT).show();
                return;
            }
            else if (accountType == 2) {

            }
            else if (accountType == 3) {

            }
            else if (accountType == 0) {
                MainActivity m = (MainActivity)getActivity();
                m.loadAdminActivity();
            }


        } catch (SQLException e) {
            System.out.println("_LOG: " + e);
        }
    }
}
