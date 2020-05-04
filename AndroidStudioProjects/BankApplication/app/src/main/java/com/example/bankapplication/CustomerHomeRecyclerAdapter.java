package com.example.bankapplication;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Locale;

public class CustomerHomeRecyclerAdapter extends RecyclerView.Adapter<CustomerHomeRecyclerAdapter.CustomerAccountViewHolder> {

    private ArrayList<PendingPayment> paymentList;
    private OnItemClickListener clickListener;
    private TimeManager time;

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
        time = TimeManager.getInstance();
    }

    @NonNull
    @Override
    public CustomerAccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_customer_pending_payment, parent, false);
        CustomerAccountViewHolder evh = new CustomerAccountViewHolder(v, clickListener);
        return evh;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CustomerAccountViewHolder holder, int position) {
        PendingPayment payment = paymentList.get(position);
        String recurrence = (payment.isReoccurring()) ? "RECURRING" : "NOT RECURRING";
        holder.recurrence.setText(recurrence);
        holder.amount.setText(String.format(Locale.GERMANY, "%.2f€", payment.getAmount()));

        // Money lost
        if (payment.getAccountFrom().equals(payment.getTargetAccount())) {
            holder.accountNumber.setText("To: "+payment.getAccountFrom());
            holder.amount.setText(String.format(Locale.GERMANY, "-%.2f€", payment.getAmount()));
            holder.deleteImage.setImageResource(R.drawable.ic_delete);
        }
        // Money gained
        else {
            holder.accountNumber.setText("From: " + payment.getAccountFrom());
            if (payment.isInterest())
                holder.amount.setText(String.format(Locale.GERMANY, "+%.2f%%", payment.getAmount()));
            else
                holder.amount.setText(String.format(Locale.GERMANY, "+%.2f€", payment.getAmount()));
            holder.amount.setTextColor(Color.rgb(153, 204, 0));
            holder.deleteImage.setVisibility(View.INVISIBLE);
        }
        //
        if (payment.getDate().before(new Date(time.today())))
            holder.dueDate.setTextColor(Color.rgb(255,68,68));
        holder.dueDate.setText("Due date: "+payment.getDate().toString());
        holder.message.setText("Message: "+payment.getMessage());
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

}
