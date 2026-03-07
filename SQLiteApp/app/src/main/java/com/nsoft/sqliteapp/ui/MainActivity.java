package com.nsoft.sqliteapp.ui;

import android.content.Intent;
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
        // -----------------------------------------------------------------------------------------
        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        myAdapter = new MyAdapter(id -> {
            Toast.makeText(this, "Item: " + id, Toast.LENGTH_SHORT).show();
        });

        binding.dashboardRecyclerView.setAdapter(myAdapter);
        binding.dashboardRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        dataViewModel.getAllData().observe(this, expenseModelList -> {
            myAdapter.setExpenseModelList(expenseModelList);
        });


        binding.addIncome.setOnClickListener(v -> {
            Intent myIntent = new Intent(MainActivity.this, InputActivity.class);
            myIntent.putExtra("isExpense", false);
            startActivity(myIntent);
        });

        binding.addExpense.setOnClickListener(v -> {
            Intent myIntent = new Intent(MainActivity.this, InputActivity.class);
            myIntent.putExtra("isExpense", true);
            startActivity(myIntent);
        });

        binding.showIncomes.setOnClickListener(v -> {
            Intent myIntent = new Intent(MainActivity.this, ShowActivity.class);
            myIntent.putExtra("isExpense", false);
            startActivity(myIntent);
        });

        binding.showExpenses.setOnClickListener(v -> {
            Intent myIntent = new Intent(MainActivity.this, ShowActivity.class);
            myIntent.putExtra("isExpense", true);
            startActivity(myIntent);
        });


    }
    // on create end here ==========================================================================

    @Override
    protected void onResume() {
        super.onResume();
        double totalIncome = dataViewModel.getTotalIncome();
        double totalExpense = dataViewModel.getTotalExpense();
        double total = totalIncome - totalExpense;

        binding.tvTotal.setText(total + " BDT.");
        binding.tvTotalIncome.setText(totalIncome + " BDT.");
        binding.tvTotalExpense.setText(totalExpense + " BDT.");

    }
}