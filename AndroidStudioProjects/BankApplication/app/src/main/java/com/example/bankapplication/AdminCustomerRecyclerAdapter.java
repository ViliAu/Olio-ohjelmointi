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

public class AdminCustomerRecyclerAdapter extends RecyclerView.Adapter<AdminCustomerRecyclerAdapter.AdminCustomerViewHolder> {

    private ArrayList<Customer> customerList;
    private OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        clickListener = listener;
    }

    public static class AdminCustomerViewHolder extends RecyclerView.ViewHolder {
        public ImageView cardImage;
        public TextView twUsername;
        public TextView twId;
        public TextView twBank;
        public TextView twOwner;
        public TextView twAccType;

        public AdminCustomerViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            cardImage = itemView.findViewById(R.id.imageView);
            twUsername = itemView.findViewById(R.id.tw_username);
            twId = itemView.findViewById(R.id.tw_id);
            twBank = itemView.findViewById(R.id.tw_bank);
            twOwner = itemView.findViewById(R.id.tw_owner);
            twAccType = itemView.findViewById(R.id.tw_account_type);

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
        }
    }

    public AdminCustomerRecyclerAdapter(ArrayList<Customer> custList) {
        customerList = custList;
    }

    @NonNull
    @Override
    public AdminCustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_admin_customer, parent, false);
        AdminCustomerViewHolder evh = new AdminCustomerViewHolder(v, clickListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull AdminCustomerViewHolder holder, int position) {
        Customer cust = customerList.get(position);

        holder.cardImage.setImageResource(R.drawable.ic_account_circle);
        holder.twUsername.setText(cust.getAccountName());
        holder.twId.setText(String.format(Locale.GERMANY, "ID: %d", cust.getId()));
        holder.twBank.setText(cust.getBankString());
        holder.twOwner.setText(cust.getName());
        holder.twAccType.setText(cust.getAccTypeString());
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }
}
