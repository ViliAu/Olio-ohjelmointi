package com.example.bankapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Random;

public class LoginPinCodeDialog extends AppCompatDialogFragment {

    private TextView code;
    private EditText textEditor;
    private TextInputLayout input;
    private PinDialogListener listener;
    private SharedViewModelMain viewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (getActivity() != null)
            viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModelMain.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_login_pin, null);
        builder.setView(view).setTitle("PIN CODE").setNegativeButton("Cancel", null)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        checkCode();
                    }
                });
        initElements(view);
        return builder.create();
    }

    // Set texts to represent the right info
    private void initElements(View view) {
        // Init elements
        code = view.findViewById(R.id.tw_code);
        code.setText(generateCode());
        textEditor = view.findViewById(R.id.et_code);
        input = view.findViewById(R.id.input_code);
    }

    private String generateCode() {
        Random rand = new Random();
        String pin = "";
        for (int i = 0; i < 6; i++) {
            pin += rand.nextInt(10);
        }
        return pin;
    }

    private void checkCode() {
        if (code.getText().toString().equals(textEditor.getText().toString())) {
            if (listener != null)
                listener.changeToCustomerActivity(viewModel.getCustomerId());
        }
        else {
            Toast.makeText(getContext(), "Codes did not match!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (PinDialogListener) context;
        }
        catch (ClassCastException e) {
            System.err.println("_LOG: "+e);
        }
    }

    public interface PinDialogListener {
        void changeToCustomerActivity(int id);
    }
}
