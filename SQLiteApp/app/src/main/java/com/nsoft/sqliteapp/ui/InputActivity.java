package com.nsoft.sqliteapp.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.nsoft.sqliteapp.R;
import com.nsoft.sqliteapp.databinding.ActivityInputBinding;
import com.nsoft.sqliteapp.viewmodel.DataViewModel;

public class InputActivity extends AppCompatActivity {
    private ActivityInputBinding binding;
    private boolean isExpense;
    private DataViewModel dataViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityInputBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // -----------------------------------------------------------------------------------------
        isExpense = getIntent().getBooleanExtra("isExpense", false);
        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);


        if (isExpense) {
            binding.inputTitle.setText("Add Expense");
            binding.addButton.setText("Add Expense");

            binding.addButton.setOnClickListener(v -> {
                String amountString = binding.amountEditText.getText().toString().trim();
                String reason = binding.reasonEditText.getText().toString().trim();

                if (amountString.isEmpty() && reason.isEmpty()) {
                    Toast.makeText(this, "Please fill all field.", Toast.LENGTH_SHORT).show();
                } else {
                    double amount = Double.parseDouble(amountString);
                    dataViewModel.addExpense(amount, reason);
                    Toast.makeText(this, "Expense added", Toast.LENGTH_SHORT).show();
                }

            });
        } else {
            binding.inputTitle.setText("Add ExpenseModel");
            binding.addButton.setText("Add ExpenseModel");

            binding.addButton.setOnClickListener(v -> {
                String amountString = binding.amountEditText.getText().toString().trim();
                String reason = binding.reasonEditText.getText().toString().trim();

                if (amountString.isEmpty() && reason.isEmpty()) {
                    Toast.makeText(this, "Please fill all field.", Toast.LENGTH_SHORT).show();
                } else {
                    double amount = Double.parseDouble(amountString);
                    dataViewModel.addIncome(amount, reason);
                    Toast.makeText(this, "Expense added", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}