package com.nsoft.sqliteapp.adapters;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.nsoft.sqliteapp.R;
import com.nsoft.sqliteapp.model.ExpenseModel;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private OnclickCallback onclickCallback;

    private List<ExpenseModel> expenseModelList = new ArrayList<>();

    public void setExpenseModelList(List<ExpenseModel> expenseModelList) {
        this.expenseModelList = expenseModelList;
        notifyDataSetChanged();
    }

    public MyAdapter(OnclickCallback onclickCallback) {
        this.onclickCallback = onclickCallback;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView amountTextView, reasonTextView, timeTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
            reasonTextView = itemView.findViewById(R.id.reasonTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new MyViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ExpenseModel expenseModel = expenseModelList.get(position);

        String bdt_icon = holder.itemView.getContext().getString(R.string.bdt_icon);

        holder.reasonTextView.setText(expenseModel.getReason());
        holder.timeTextView.setText("Time: " + expenseModel.getTime());

        String type = expenseModel.getType();
        if (type.equals("expense")) {
            holder.amountTextView.setText("+" + bdt_icon + expenseModel.getAmount());
            holder.amountTextView.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.dark_green));
        } else {
            holder.amountTextView.setText("-" + bdt_icon + expenseModel.getAmount());
            holder.amountTextView.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.dark_red));
        }

        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Confirm delete")
                    .setMessage("Do you really want to delete this item?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        Toast.makeText(v.getContext(), "Done.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
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
        return expenseModelList.size();
    }

    public interface OnclickCallback {
        void onClickListener(int id);
    }

}
