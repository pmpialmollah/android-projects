package com.nsoft.mybakery;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductRecyclerViewAdapter.MyViewHolder> {
    private List<Product> productList;
    private ClickListener listener;
    private LongClickListener longClickListener;

    public ProductRecyclerViewAdapter(List<Product> productList, ClickListener listener, LongClickListener longClickListener) {
        this.productList = productList;
        this.listener = listener;
        this.longClickListener = longClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageView, productDeleteImageView;
        TextView productNameTextView, productPriceTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.productImageView);
            productDeleteImageView = itemView.findViewById(R.id.productDeleteImageView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            productPriceTextView = itemView.findViewById(R.id.productPriceTextView);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
        return new MyViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product product = productList.get(position);
        int id = product.getId();
        String image = product.getImage();
        String name = product.getName();
        String price = product.getPrice();

        holder.productNameTextView.setText(name);
        holder.productPriceTextView.setText("৳" + price);

        holder.productDeleteImageView.setOnClickListener(v -> {
            listener.onDeleteListener(id, position);
        });

        holder.itemView.setOnLongClickListener(v -> {
            longClickListener.onLongClickListener(id);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public interface ClickListener {
        void onDeleteListener(int id, int position);
    }

    public interface LongClickListener {
        void onLongClickListener(int position);
    }

}
