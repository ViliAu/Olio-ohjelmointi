package com.example.bankapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class CustomerAccountsRecyclerAdapter extends RecyclerView.Adapter<CustomerAccountsRecyclerAdapter.CustomerAccountViewHolder> {

    private ArrayList<Account> accountList;
    private OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onCardClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        clickListener = listener;
    }

    public static class CustomerAccountViewHolder extends RecyclerView.ViewHolder {
        public ImageView accountImage;
        public ImageButton cardButton;
        public TextView accountName, balance, accountNumber, accountType, specialText, paymentEnabled;

        public CustomerAccountViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            accountImage = itemView.findViewById(R.id.imageview_account);
            cardButton = itemView.findViewById(R.id.button_card);
            accountName = itemView.findViewById(R.id.tw_account_name);
            balance = itemView.findViewById(R.id.tw_balance);
            accountNumber = itemView.findViewById(R.id.tw_account_number);
            accountType = itemView.findViewById(R.id.tw_account_type);
            specialText = itemView.findViewById(R.id.tw_special_info);
            paymentEnabled = itemView.findViewById(R.id.tw_payment_enabled);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
            cardButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onCardClick(position);
                        }
                    }
                }
            });
        }
    }

    public CustomerAccountsRecyclerAdapter(ArrayList<Account> accList) {
        accountList = accList;
    }

    @NonNull
    @Override
    public CustomerAccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_customer_account, parent, false);
        CustomerAccountViewHolder evh = new CustomerAccountViewHolder(v, clickListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerAccountViewHolder holder, int position) {
        Account account = accountList.get(position);
        createCard(account.getType(), account, holder);
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }

    // Create menu card based on account type
    private void createCard(int type, Account account, @NonNull CustomerAccountViewHolder holder) {
        // Init certain menu items based on acc type
        switch (type) {
            case 1:
                holder.accountImage.setImageResource(R.drawable.ic_current_account);
                holder.specialText.setVisibility(View.GONE);
                holder.accountType.setText("Type: Current account");
                break;
            case 2:
                CreditAccount cAcc = (CreditAccount)account;
                holder.accountImage.setImageResource(R.drawable.ic_money);
                holder.specialText.setText(String.format(Locale.GERMANY, "Credit limit: %.2f€", cAcc.getCreditLimit()));
                holder.accountType.setText("Type: Credit account");
                break;
            case 3:
                SavingsAccount sAcc = (SavingsAccount) account;
                holder.accountImage.setImageResource(R.drawable.ic_save);
                holder.specialText.setText(String.format(Locale.GERMANY, "Interest: %.2f%%", sAcc.getInterest()));
                holder.accountType.setText("Type: Savings account");
                break;
            case 4:
                FixedTermAccount fAcc = (FixedTermAccount) account;
                holder.accountImage.setImageResource(R.drawable.ic_calendar);
                holder.specialText.setText(String.format(Locale.GERMANY, "Due date: %s, Interest: %.2f%%", fAcc.getDueDate(), fAcc.getInterest()));
                holder.accountType.setText("Type: Fixed term account");
                break;
        }

        holder.accountName.setText(account.getName());
        holder.balance.setText(String.format(Locale.GERMANY, "%.2f€", account.getBalance()));
        holder.accountNumber.setText(account.getAccountNumber());
        String paymentText = "Payment enabled";
        if (account.getState() == 4)
            paymentText = "Payment disabled";
        holder.paymentEnabled.setText(paymentText);
    }
}
