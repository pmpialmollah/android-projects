package com.nsoft.mybakery;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DialogProductRecyclerviewAdapter extends RecyclerView.Adapter<DialogProductRecyclerviewAdapter.MyViewHolder> {
    private List<Product> productList;
    private ViewHolderInterface viewHolderInterface;

    public interface ViewHolderInterface {
        void viewHolder(MyViewHolder holder, Product product);
    }

    public DialogProductRecyclerviewAdapter(List<Product> productList, ViewHolderInterface viewHolderInterface) {
        this.productList = productList;
        this.viewHolderInterface = viewHolderInterface;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageView, plusImageView, minusImageView;
        TextView productNameTextView, itemCountTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.productImageView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            itemCountTextView = itemView.findViewById(R.id.itemCountTextView);
            plusImageView = itemView.findViewById(R.id.plusImageView);
            minusImageView = itemView.findViewById(R.id.minusImageView);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_add_item, parent, false);
        return new MyViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.productNameTextView.setText(product.getName());

        viewHolderInterface.viewHolder(holder, product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


}
