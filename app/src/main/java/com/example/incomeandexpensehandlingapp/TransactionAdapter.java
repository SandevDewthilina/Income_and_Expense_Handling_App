package com.example.incomeandexpensehandlingapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {

    private List<Transactions> transactionsList;

    public TransactionAdapter(List<Transactions> transactionsList) {
        this.transactionsList = transactionsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.type.setText(transactionsList.get(position).getType());
        holder.desc.setText(transactionsList.get(position).getDesc());
        holder.amount.setText(transactionsList.get(position).getAmount());
        holder.date.setText(transactionsList.get(position).getTime_stamp().toString());

    }

    @Override
    public int getItemCount() {
        return transactionsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView type, amount, date, desc;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            type = itemView.findViewById(R.id.list_type);
            amount = itemView.findViewById(R.id.list_amount);
            date = itemView.findViewById(R.id.list_timestamp);
            desc = itemView.findViewById(R.id.list_desc);

        }

    }
}
