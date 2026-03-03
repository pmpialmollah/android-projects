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

public class CustomerRecyclerViewAdapter extends RecyclerView.Adapter<CustomerRecyclerViewAdapter.MyViewHolder> {
    private Context context;
    private List<Customer> customerList;
    private ClickListener clickListener;
    private LongClickListener longClickListener;

    public CustomerRecyclerViewAdapter(Context context, List<Customer> customerList, ClickListener clickListener, LongClickListener longClickListener) {
        this.context = context;
        this.customerList = customerList;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView customerProfileImageView, customerDeleteImageView;
        TextView customerNameTextView, customerAddressTextView, customerNumberTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            customerProfileImageView = itemView.findViewById(R.id.customerProfileImageView);
            customerDeleteImageView = itemView.findViewById(R.id.customerDeleteImageView);
            customerNameTextView = itemView.findViewById(R.id.customerNameTextView);
            customerAddressTextView = itemView.findViewById(R.id.customerAddressTextView);
            customerNumberTextView = itemView.findViewById(R.id.customerNumberTextView);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_item_layout, parent, false);
        return new MyViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Customer customer = customerList.get(position);

        holder.customerNameTextView.setText(customer.getName());
        holder.customerAddressTextView.setText(customer.getAddress());
        holder.customerNumberTextView.setText(customer.getNumber());

        holder.customerDeleteImageView.setOnClickListener(v -> {
            clickListener.onClick(customer.getId());
        });

        holder.itemView.setOnLongClickListener(v -> {
            longClickListener.onLongClick(customer);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public interface ClickListener {
        void onClick(int id);
    }

    public interface LongClickListener {
        void onLongClick(Customer customer);
    }

}
