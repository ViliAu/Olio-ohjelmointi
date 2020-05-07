package com.example.bankapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class CustomerTransactionHistoryRecyclerAdapter extends RecyclerView.Adapter<CustomerTransactionHistoryRecyclerAdapter.CustomerAccountViewHolder> {

    private ArrayList<PaymentTransaction> paymentList;
    private OnItemClickListener clickListener;
    private TimeManager time;

    public interface OnItemClickListener {
        void onInfoItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        clickListener = listener;
    }

    public static class CustomerAccountViewHolder extends RecyclerView.ViewHolder {
        public ImageView infoImage;
        public TextView action, amount, accountNumber, date, message;

        public CustomerAccountViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            infoImage = itemView.findViewById(R.id.imageview_info);
            action = itemView.findViewById(R.id.tw_action);
            amount = itemView.findViewById(R.id.tw_amount);
            accountNumber = itemView.findViewById(R.id.tw_account_number);
            date = itemView.findViewById(R.id.tw_date);
            message = itemView.findViewById(R.id.tw_message);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onInfoItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public CustomerTransactionHistoryRecyclerAdapter(ArrayList<PaymentTransaction> payments) {
        paymentList = payments;
        time = TimeManager.getInstance();
    }

    @NonNull
    @Override
    public CustomerAccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_customer_payment_transaction, parent, false);
        CustomerAccountViewHolder evh = new CustomerAccountViewHolder(v, clickListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerAccountViewHolder holder, int position) {
        PaymentTransaction payment = paymentList.get(position);

        holder.infoImage.setImageResource(R.drawable.ic_info);
        holder.action.setText(payment.getAction());
        holder.date.setText(time.getReadableDateTime(payment.getDate().getTime()));
        holder.message.setText("Message: "+payment.getMessage());

        // Check if money was lost or gained
        char sign = payment.getAccountFrom().equals(payment.getTargetAccount()) ? '-' : '+';

        holder.amount.setText(String.format(Locale.GERMANY, "%c%.2f€", sign, payment.getAmount()));
        holder.accountNumber.setText("To: " + payment.getAccountFrom());

    }

    @Override
    public int getItemCount() {
        if (paymentList == null)
            return 0;
        else
            return paymentList.size();
    }

}
