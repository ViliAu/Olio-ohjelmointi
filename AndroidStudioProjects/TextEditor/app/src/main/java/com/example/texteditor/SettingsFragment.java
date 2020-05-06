package com.example.texteditor;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.texteditor.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {
    private SharedViewModel viewModel;
    private FragmentSettingsBinding binding;

    // Primitives
    //private String textToCaps;
    private int[] colors;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        initElements();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

        colors = new int[3];
    }

    /* Return fragment's previous state */
    @Override
    public void onResume() {
        super.onResume();
        // Load colors
        if (viewModel.getColor().getValue() != null) {
            colors = viewModel.getColor().getValue();
            binding.barR.setProgress(colors[0]);
            binding.barG.setProgress(colors[1]);
            binding.barB.setProgress(colors[2]);
        }

        // Load text fields
        if (viewModel.getOverrideableText().getValue() != null)
                binding.etOverrideText.setText(viewModel.getOverrideableText().getValue());

        if (viewModel.getSize().getValue() != null)
                binding.etFontSize.setText(viewModel.getSize().getValue().toString());

        if (viewModel.getLineAmount().getValue() != null)
                binding.etLineAmount.setText(viewModel.getLineAmount().getValue().toString());

        // Set Switch
        if (viewModel.getEditAccess().getValue() != null)
            binding.switchEnableEdit.setChecked(viewModel.getEditAccess().getValue());
    }

    private void initElements() {
        initButtons();
        initEditors();
        initBars();
    }

    private void initButtons() {
        binding.buttonAllCaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.etOverrideText.setText(binding.etOverrideText.getText().toString().toUpperCase());
                viewModel.setOverrideableText(binding.etOverrideText.getText());
            }
        });

        binding.switchEnableEdit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                viewModel.setEditAccess(isChecked);
                viewModel.setEdit(isChecked);
            }
        });

        binding.buttonLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity m = (MainActivity)getActivity();
                m.gotoSetting();
            }
        });
    }

    private void initEditors() {
        // Override text field (bottom-most one)
        binding.etOverrideText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setOverrideableText(binding.etOverrideText.getText());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Text size edit field
        binding.etFontSize.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    viewModel.setSize(Integer.parseInt(binding.etFontSize.getText().toString()));
                }
                catch (Exception e) {
                    System.out.println("_LOG: string couldn't be parsed.");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Init line amount field
        binding.etLineAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    viewModel.setLineAmount(Integer.parseInt(binding.etLineAmount.getText().toString()));
                }
                catch (Exception e) {
                    System.out.println("_LOG: string couldn't be parsed.");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initBars() {
        binding.barR.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                colors[0] = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.barG.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                colors[1] = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                viewModel.setColor(colors);
            }
        });

        binding.barB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                colors[2] = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                viewModel.setColor(colors);
            }
        });
    }
}
