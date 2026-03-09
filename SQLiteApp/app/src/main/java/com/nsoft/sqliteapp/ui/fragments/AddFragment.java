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

import com.nsoft.sqliteapp.databinding.FragmentAddBinding;
import com.nsoft.sqliteapp.viewmodel.DataViewModel;

public class AddFragment extends Fragment {
    private FragmentAddBinding binding;
    private boolean isExpense = true;
    private boolean isDropdownOpen = false;
    private DataViewModel dataViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // my code here ----------------------------------------------------------------------------
        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);


        binding.dropDown.setOnClickListener(v -> {
            if (isDropdownOpen) {
                binding.dropdownPopupWindow.setVisibility(View.GONE);
                isDropdownOpen = false;
            } else {
                binding.dropdownPopupWindow.setVisibility(View.VISIBLE);
                isDropdownOpen = true;
            }
        });


        binding.addButton.setOnClickListener(v -> {
            String amountString = binding.amountEditText.getText().toString().trim();
            String reason = binding.reasonEditText.getText().toString().trim();

            if (amountString.isEmpty() && reason.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all field.", Toast.LENGTH_SHORT).show();
            } else {
                double amount = Double.parseDouble(amountString);

                if (isExpense) {

                    dataViewModel.addExpense(amount, reason);
                    Toast.makeText(getContext(), "Expense added", Toast.LENGTH_SHORT).show();

                } else {

                    dataViewModel.addIncome(amount, reason);
                    Toast.makeText(getContext(), "Income added", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.dropdownExpense.setOnClickListener(v -> {
            isExpense = true;
            updateUI();
        });

        binding.dropdownIncome.setOnClickListener(v -> {
            isExpense = false;
            updateUI();
        });


        // -----------------------------------------------------------------------------------------
    }

    private void updateUI() {
        if (isExpense) {
            binding.inputTitle.setText("Add Expense");
            binding.addButton.setText("Add Expense");

        } else {
            binding.inputTitle.setText("Add Income");
            binding.addButton.setText("Add Income");

        }
        binding.dropdownPopupWindow.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}