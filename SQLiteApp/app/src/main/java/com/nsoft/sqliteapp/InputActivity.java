package com.nsoft.sqliteapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.nsoft.sqliteapp.databinding.ActivityInputBinding;

public class InputActivity extends AppCompatActivity {
    private ActivityInputBinding binding;
    DatabaseHelper databaseHelper;
    public boolean isExpense;

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

        isExpense = getIntent().getBooleanExtra("isExpense", false);

        databaseHelper = new DatabaseHelper(this);

        if (isExpense) {
            binding.inputTitle.setText("Add Expense");
            binding.addButton.setText("Add Expense");

            binding.addButton.setOnClickListener(v -> {
                double amount = Double.parseDouble(binding.amountEditText.getText().toString().trim());
                String reason = binding.reasonEditText.getText().toString().trim();

                databaseHelper.addExpense(amount, reason);

                Toast.makeText(this, "Expense added", Toast.LENGTH_SHORT).show();
            });
        } else {
            binding.inputTitle.setText("Add Income");
            binding.addButton.setText("Add Income");

            binding.addButton.setOnClickListener(v -> {
                double amount = Double.parseDouble(binding.amountEditText.getText().toString().trim());
                String reason = binding.reasonEditText.getText().toString().trim();
                databaseHelper.addIncome(amount, reason);

                Toast.makeText(this, "Income added", Toast.LENGTH_SHORT).show();
            });

        }
    }
}