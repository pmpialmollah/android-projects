package com.nsoft.mybakery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomerDialogAdapter extends RecyclerView.Adapter<CustomerDialogAdapter.MyViewHolder> {
    private Context context;
    private List<Customer> customerList;
    private ClickListener clickListener;

    public interface ClickListener {
        void onClick(Customer customer);
    }

    public CustomerDialogAdapter(Context context, List<Customer> customerList, ClickListener clickListener) {
        this.context = context;
        this.customerList = customerList;
        this.clickListener = clickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView dialogCustomerProfileImageView;
        TextView dialogCustomerNameTextView, dialogCustomerAddressTextView, dialogCustomerNumberTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            dialogCustomerProfileImageView = itemView.findViewById(R.id.dialogCustomerProfileImageView);
            dialogCustomerNameTextView = itemView.findViewById(R.id.dialogCustomerNameTextView);
            dialogCustomerAddressTextView = itemView.findViewById(R.id.dialogCustomerAddressTextView);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_customer_item, parent, false);
        return new MyViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Customer customer = customerList.get(position);
        holder.dialogCustomerNameTextView.setText(customer.getName());
        holder.dialogCustomerAddressTextView.setText("(" + customer.getAddress() + ")");

        holder.itemView.setOnClickListener(v -> {
            clickListener.onClick(customer);
        });
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }
}
