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
import com.nsoft.sqliteapp.databinding.ActivityShowBinding;
import com.nsoft.sqliteapp.viewmodel.DataViewModel;

public class ShowActivity extends AppCompatActivity {
    private ActivityShowBinding binding;
    private boolean isExpense;
    private MyAdapter myAdapter;
    private DataViewModel dataViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityShowBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        isExpense = getIntent().getBooleanExtra("isExpense", false);
        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        myAdapter = new MyAdapter(id -> {
            Toast.makeText(this, "Item id: " + id, Toast.LENGTH_SHORT).show();
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(myAdapter);

        if (isExpense) {
            binding.title.setText("Expenses");
            dataViewModel.getIndividualData("expense").observe(this, expenseModelList -> {
                myAdapter.setExpenseModelList(expenseModelList);
            });

        } else {
            binding.title.setText("Incomes");
            dataViewModel.getIndividualData("income").observe(this, expenseModelList -> {
                myAdapter.setExpenseModelList(expenseModelList);
            });
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
