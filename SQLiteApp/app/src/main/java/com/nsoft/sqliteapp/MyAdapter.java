package com.nsoft.sqliteapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Activity activity;
    private ArrayList<HashMap<String, String>> allData;
    private DatabaseHelper databaseHelper;
    private OnclickCallback onclickCallback;

    public MyAdapter(Activity activity, ArrayList<HashMap<String, String>> allData, OnclickCallback onclickCallback) {
        this.activity = activity;
        this.allData = allData;
        databaseHelper = new DatabaseHelper(activity);
        this.onclickCallback = onclickCallback;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView amountTextView, reasonTextView, timeTextView;
        ImageView typeImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
            reasonTextView = itemView.findViewById(R.id.reasonTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            typeImageView = itemView.findViewById(R.id.typeImageView);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = activity.getLayoutInflater().inflate(R.layout.item_layout, parent, false);
        return new MyViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        HashMap<String, String> data = allData.get(position);
        holder.amountTextView.setText(data.get("amount"));
        holder.reasonTextView.setText(data.get("reason"));
        holder.timeTextView.setText("Time: " + data.get("time"));

        String type = data.get("type");
        if (type.equals("expense")) {
            holder.typeImageView.setImageResource(R.drawable.chevron_down_64);
        } else {
            holder.typeImageView.setImageResource(R.drawable.chevron_up_64);
        }

        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(activity)
                    .setTitle("Confirm delete")
                    .setMessage("Do you really want to delete this item?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        onclickCallback.onClickListener(Integer.parseInt(data.get("id")));
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return allData.size();
    }

    public interface OnclickCallback{
        void onClickListener(int id);
    }

}
