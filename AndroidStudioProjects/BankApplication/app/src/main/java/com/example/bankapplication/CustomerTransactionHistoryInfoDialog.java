package com.example.bankapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.ViewModelProviders;

public class CustomerTransactionHistoryInfoDialog extends AppCompatDialogFragment {

    private TextView accountFrom, accountTo, bicFrom, bicTo, date, amount, message;
    private SharedViewModelCustomer viewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_transaction_history_info, null);
        builder.setView(view).setTitle("Additional information").setNeutralButton("Close", null);
        if (getActivity() != null)
            viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModelCustomer.class);
        setInfo(view);
        return builder.create();
    }

    // Set texts to represent the right info
    private void setInfo(View view) {
        // Init elements
        accountFrom = view.findViewById(R.id.tw_account_from);
        accountTo = view.findViewById(R.id.tw_account_to);
        bicFrom = view.findViewById(R.id.tw_bic_from);
        bicTo = view.findViewById(R.id.tw_bic_to);
        date = view.findViewById(R.id.tw_date);
        amount = view.findViewById(R.id.tw_amount);
        message = view.findViewById(R.id.tw_message);

        accountFrom.setText("From: "+viewModel.getAccountFrom());
        accountTo.setText("To: "+viewModel.getAccountTo());
        bicFrom.setText("BIC: "+viewModel.getBicFrom());
        bicTo.setText("BIC: "+viewModel.getBicTo());
        date.setText("Date: "+viewModel.getDate());
        amount.setText("Amount: " +viewModel.getAmount());
        message.setText("Message: "+viewModel.getMessage());
    }
}
