package com.nsoft.sqliteapp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.nsoft.sqliteapp.R;
import com.nsoft.sqliteapp.adapters.MyAdapter;
import com.nsoft.sqliteapp.databinding.FragmentHomeBinding;
import com.nsoft.sqliteapp.viewmodel.DataViewModel;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;

    private MyAdapter myAdapter;
    private DataViewModel dataViewModel;

    // income এবং expense আলাদা ট্র্যাক করার জন্য
    private double income = 0;
    private double expense = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // my code here ----------------------------------------------------------------------------
        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        myAdapter = new MyAdapter(id -> {
            if (isAdded()) {
                Toast.makeText(requireContext(), "Item: " + id, Toast.LENGTH_SHORT).show();
            }
        });

        binding.dashboardRecyclerView.setAdapter(myAdapter);
        binding.dashboardRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // সব ডাটা অবজার্ভ করা
        dataViewModel.getAllData().observe(getViewLifecycleOwner(), expenseModelList -> {
            myAdapter.setExpenseModelList(expenseModelList);
        });

        // Income আপডেট হলে টোটাল আপডেট করা
        dataViewModel.getTotalIncome().observe(getViewLifecycleOwner(), aDouble -> {
            if (aDouble != null) {
                income = aDouble;
            } else {
                income = 0;
            }
            if (isAdded()) {
                binding.tvTotalIncome.setText(getString(R.string.bdt_icon) + income);
                updateBalance();
            }
        });

        // Expense আপডেট হলে টোটাল আপডেট করা
        dataViewModel.getTotalExpense().observe(getViewLifecycleOwner(), aDouble -> {
            if (aDouble != null) {
                expense = aDouble;
            } else {
                expense = 0;
            }
            if (isAdded()) {
                binding.tvTotalExpense.setText(getString(R.string.bdt_icon) + expense);
                updateBalance();
            }
        });


        // -----------------------------------------------------------------------------------------
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // ব্যালেন্স ক্যালকুলেট করার মেথড
    private void updateBalance() {
        if (!isAdded() || binding == null) return;
        double balance = income - expense;
        binding.tvTotal.setText(getString(R.string.bdt_icon) + balance);
    }
}
