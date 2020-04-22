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

public class CustomerHomeRecyclerAdapter extends RecyclerView.Adapter<CustomerHomeRecyclerAdapter.CustomerAccountViewHolder> {

    private ArrayList<PendingPayment> paymentList;
    private OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onDeleteItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        clickListener = listener;
    }

    public static class CustomerAccountViewHolder extends RecyclerView.ViewHolder {
        public ImageView deleteImage;
        public TextView recurrence, amount, accountNumber, dueDate, message;

        public CustomerAccountViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            deleteImage = itemView.findViewById(R.id.imageview_delete);
            recurrence = itemView.findViewById(R.id.tw_recurrence);
            amount = itemView.findViewById(R.id.tw_amount);
            accountNumber = itemView.findViewById(R.id.tw_account_number);
            dueDate = itemView.findViewById(R.id.tw_due_date);
            message = itemView.findViewById(R.id.tw_message);

            deleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public CustomerHomeRecyclerAdapter(ArrayList<PendingPayment> payments) {
        paymentList = payments;
    }

    @NonNull
    @Override
    public CustomerAccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_customer_pending_payment, parent, false);
        CustomerAccountViewHolder evh = new CustomerAccountViewHolder(v, clickListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerAccountViewHolder holder, int position) {
        PendingPayment payment = paymentList.get(position);
        holder.deleteImage.setImageResource(R.drawable.ic_delete);
        String recurrence = (payment.isReoccurring()) ? "RECURRING" : "NOT RECURRING";
        holder.recurrence.setText(recurrence);
        holder.amount.setText(String.format(Locale.GERMANY, "%.2f€", payment.getAmount()));
        holder.accountNumber.setText("From: "+payment.getAccountFrom());
        holder.dueDate.setText("Due date: "+payment.getDate().toString());
        holder.message.setText("Message: "+payment.getMessage());
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

}
