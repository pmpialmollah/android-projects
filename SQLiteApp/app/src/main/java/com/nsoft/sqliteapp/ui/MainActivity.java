package com.nsoft.sqliteapp.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.nsoft.sqliteapp.R;
import com.nsoft.sqliteapp.adapters.MyAdapter;
import com.nsoft.sqliteapp.databinding.ActivityMainBinding;
import com.nsoft.sqliteapp.viewmodel.DataViewModel;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MyAdapter myAdapter;
    private DataViewModel dataViewModel;

    // income এবং expense আলাদা ট্র্যাক করার জন্য
    private double income = 0;
    private double expense = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        myAdapter = new MyAdapter(id -> {
            Toast.makeText(this, "Item: " + id, Toast.LENGTH_SHORT).show();
        });

        binding.dashboardRecyclerView.setAdapter(myAdapter);
        binding.dashboardRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // সব ডাটা অবজার্ভ করা
        dataViewModel.getAllData().observe(this, expenseModelList -> {
            myAdapter.setExpenseModelList(expenseModelList);
        });

        // Income আপডেট হলে টোটাল আপডেট করা
        dataViewModel.getTotalIncome().observe(this, aDouble -> {
            income = aDouble;
            binding.tvTotalIncome.setText(getString(R.string.bdt_icon) + income);
            updateBalance();
        });

        // Expense আপডেট হলে টোটাল আপডেট করা
        dataViewModel.getTotalExpense().observe(this, aDouble -> {
            expense = aDouble;
            binding.tvTotalExpense.setText(getString(R.string.bdt_icon) + expense);
            updateBalance();
        });

        // বাটনের লজিকগুলো...
//        binding.addIncome.setOnClickListener(v -> {
//            Intent myIntent = new Intent(MainActivity.this, InputActivity.class);
//            myIntent.putExtra("isExpense", false);
//            startActivity(myIntent);
//        });
//
//        binding.addExpense.setOnClickListener(v -> {
//            Intent myIntent = new Intent(MainActivity.this, InputActivity.class);
//            myIntent.putExtra("isExpense", true);
//            startActivity(myIntent);
//        });
//
//        binding.showIncomes.setOnClickListener(v -> {
//            Intent showIncomeIntent = new Intent(MainActivity.this, ShowActivity.class);
//            showIncomeIntent.putExtra("isExpense", false);
//            startActivity(showIncomeIntent);
//        });
//
//        binding.showExpenses.setOnClickListener(v -> {
//            Intent showExpenseIntent = new Intent(MainActivity.this, ShowActivity.class);
//            showExpenseIntent.putExtra("isExpense", true);
//            startActivity(showExpenseIntent);
//        });


        // বাকি বাটন লজিক একই থাকবে...
    }

    // ব্যালেন্স ক্যালকুলেট করার মেথড
    private void updateBalance() {
        double balance = income - expense;
        binding.tvTotal.setText(getString(R.string.bdt_icon) + balance);
    }
}