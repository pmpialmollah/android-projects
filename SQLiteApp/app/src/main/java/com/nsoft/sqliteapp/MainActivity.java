package com.nsoft.sqliteapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.nsoft.sqliteapp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private DatabaseHelper databaseHelper;
    private ArrayList<HashMap<String, String>> allData = new ArrayList<>();
    private MyFunctionClasses myFunctionClasses;
    private MyAdapter myAdapter;

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

        databaseHelper = new DatabaseHelper(this);
        myFunctionClasses = new MyFunctionClasses();
        myAdapter = new MyAdapter(this, allData, new MyAdapter.OnclickCallback() {
            @Override
            public void onClickListener(int id) {
                databaseHelper.deleteItemById(id);
                updateUI();
            }
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

        updateUI();

        binding.dashboardRecyclerView.setAdapter(myAdapter);
        binding.dashboardRecyclerView.setLayoutManager(new LinearLayoutManager(this));


    }
    // on create end here ==========================================================================

    @Override
    protected void onResume() {
        super.onResume();

        updateUI();

    }

    private void updateUI(){
        Cursor cursor = databaseHelper.getAllData();
        if (cursor != null && cursor.getCount() > 0) {
            allData.clear();
            while (cursor.moveToNext()){
                HashMap<String, String> hashMap = new HashMap<>();

                int id = cursor.getInt(0);
                String type = cursor.getString(1);
                double amount = cursor.getDouble(2);
                String reason = cursor.getString(3);
                long timeStamp = cursor.getLong(4);

                String formattedDateTime = myFunctionClasses.timeStampToFormattedDateTime(timeStamp);

                hashMap.put("id", String.valueOf(id));
                hashMap.put("type", type);
                hashMap.put("amount", String.valueOf(amount));
                hashMap.put("reason", reason);
                hashMap.put("time", formattedDateTime);

                allData.add(hashMap);
            }
            myAdapter.notifyDataSetChanged();
        }

        double totalIncome = databaseHelper.getTotalIncome();
        binding.totalIncomeTextView.setText("income: " + totalIncome + " BDT.");

        double totalExpense = databaseHelper.getTotalExpense();
        binding.totalExpenseView.setText("expense: " + totalExpense + " BDT.");

        double total = databaseHelper.getTotal();
        binding.totalTextView.setText(total + " BDT.");
    }
}