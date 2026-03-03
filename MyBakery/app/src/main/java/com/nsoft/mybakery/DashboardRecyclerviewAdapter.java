package com.nsoft.mybakery;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DashboardRecyclerviewAdapter extends RecyclerView.Adapter<DashboardRecyclerviewAdapter.MyViewHolder> {
    private List<ProductSell> productSellList;

    public DashboardRecyclerviewAdapter(List<ProductSell> productSellList) {
        this.productSellList = productSellList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView itemCountTextView, itemNameTextView, itemPriceTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemCountTextView = itemView.findViewById(R.id.itemCountTextView);
            itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
            itemPriceTextView = itemView.findViewById(R.id.itemPriceTextView);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_item_layout, parent, false);
        return new MyViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ProductSell productSell = productSellList.get(position);

        holder.itemCountTextView.setText("" + productSell.getItemCount());
        holder.itemNameTextView.setText("" + productSell.getItemName());
        holder.itemPriceTextView.setText("" + productSell.getItemPrice());

    }

    @Override
    public int getItemCount() {
        return productSellList.size();
    }

}
