package com.example.texteditor;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.texteditor.databinding.FragmentMainBinding;

public class MainFragment extends Fragment {
    private SharedViewModel viewModel;
    private FragmentMainBinding binding;
    private CharSequence stringSaver;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        initEditor();
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (stringSaver != null)
            binding.etUserText.setText(stringSaver);
    }

    // Save User editor text data
    void initEditor() {
        binding.etUserText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setString(binding.etUserText.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    // Get data from shared ViewModel
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

        // Get text
        viewModel.getOverrideableText().observe(getViewLifecycleOwner(), new Observer<CharSequence>() {
            @Override
            public void onChanged(CharSequence charSequence) {
                binding.twEditorText.setText(charSequence);
            }
        });

        // Get color
        viewModel.getColor().observe(getViewLifecycleOwner(), new Observer<int[]>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onChanged(int[] ints) {
                binding.twEditorText.setTextColor(Color.rgb(ints[0], ints[1], ints[2]));
                if (!binding.etUserText.isEnabled())
                    binding.etUserText.setTextColor(Color.rgb(ints[0], ints[1], ints[2]));
                else
                  binding.etUserText.setTextColor(Color.rgb(0,0,0));
            }
        });

        // Get Size
        viewModel.getSize().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.twEditorText.setTextSize(integer);
                if (!binding.etUserText.isEnabled())
                    binding.etUserText.setTextSize(integer);
                else
                  binding.etUserText.setTextSize(20);
            }
        });

        // Get Line amount
        viewModel.getLineAmount().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.twEditorText.setLines(integer);
                if (!binding.etUserText.isEnabled())
                    binding.etUserText.setLines(integer);
                else
                  binding.etUserText.setLines(5);
            }
        });

        // Get Text field accessibility
        viewModel.getEditAccess().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.etUserText.setEnabled(aBoolean);
            }
        });

        // Get String from storage
        viewModel.getString().observe(getViewLifecycleOwner(), new Observer<CharSequence>() {
            @Override
            public void onChanged(CharSequence charSequence) {
                stringSaver = charSequence;
            }
        });
    }
}
