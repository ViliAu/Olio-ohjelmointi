package com.example.bankapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdminRecyclerAdapter extends RecyclerView.Adapter<AdminRecyclerAdapter.AdminViewHolder> {

    private ArrayList<AdminCustomersCard> customerCardList = new ArrayList<>();
    private OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        clickListener = listener;
    }

    public static class AdminViewHolder extends RecyclerView.ViewHolder {
        public ImageView cardImage;
        public TextView twUsername;
        public TextView twId;
        public TextView twBank;
        public TextView twOwner;
        public TextView twAccType;

        public AdminViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
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

    public AdminRecyclerAdapter(ArrayList<AdminCustomersCard> cardList) {
        customerCardList = cardList;
    }

    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_rw_admin_customers, parent, false);
        AdminViewHolder evh = new AdminViewHolder(v, clickListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewHolder holder, int position) {
        AdminCustomersCard card = customerCardList.get(position);

        holder.cardImage.setImageResource(card.getImageResource());
        holder.twUsername.setText(card.getUsername());
        holder.twId.setText(card.getId());
        holder.twBank.setText(card.getBank());
        holder.twOwner.setText(card.getOwner());
        holder.twAccType.setText(card.getAccType());
    }

    @Override
    public int getItemCount() {
        return customerCardList.size();
    }
}
