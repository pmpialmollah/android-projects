package com.nsoft.sqliteapp;

import android.database.Cursor;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.nsoft.sqliteapp.databinding.ActivityShowBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class ShowActivity extends AppCompatActivity {
    private ActivityShowBinding binding;
    private boolean isExpense;
    private DatabaseHelper databaseHelper;
    private ArrayList<HashMap<String, String>> allData = new ArrayList<>();
    private MyAdapter myAdapter;
    private MyFunctionClasses myFunctionClasses;


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
        databaseHelper = new DatabaseHelper(this);
        myFunctionClasses = new MyFunctionClasses();

        if (isExpense) {
            binding.title.setText("Expenses");
            updateUI("expense");

        } else {
            binding.title.setText("Incomes");
            updateUI("income");
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isExpense) {
            updateUI("expense");
        } else {
            updateUI("income");
        }
    }

    private void updateUI(String expenseIncome) {
        Cursor cursor = databaseHelper.getIndividualData(expenseIncome);

        if (cursor != null && cursor.getCount() > 0) {
            allData.clear();
            while (cursor.moveToNext()) {
                HashMap<String, String> hashMap = new HashMap<>();

                int id = cursor.getInt(0);
                String type = cursor.getString(1);
                double amount = cursor.getDouble(2);
                String reason = cursor.getString(3);
                long time = cursor.getLong(4);

                String formattedDateTime = myFunctionClasses.timeStampToFormattedDateTime(time);

                hashMap.put("id", String.valueOf(id));
                hashMap.put("type", type);
                hashMap.put("amount", String.valueOf(amount));
                hashMap.put("reason", reason);
                hashMap.put("time", formattedDateTime);

                allData.add(hashMap);
            }

        }
        myAdapter = new MyAdapter(this, allData, id -> {
            databaseHelper.deleteItemById(id);
            updateUI(expenseIncome);
        });
        binding.recyclerView.setAdapter(myAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}