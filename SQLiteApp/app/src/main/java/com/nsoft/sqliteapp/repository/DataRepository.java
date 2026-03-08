package com.nsoft.sqliteapp.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nsoft.sqliteapp.model.ExpenseModel;

import java.util.ArrayList;
import java.util.List;

public class DataRepository extends SQLiteOpenHelper {
    private static final String TAG = "My_DATABASE";
    private static final String DB_NAME = "MY_DATABASE";
    private static final int DB_VERSION = 1;
    
    private static DataRepository instance;
    
    private final MutableLiveData<List<ExpenseModel>> allLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<ExpenseModel>> individualLiveData = new MutableLiveData<>();
    private final MutableLiveData<Double> totalIncomeLiveData = new MutableLiveData<>();
    private final MutableLiveData<Double> totalExpenseLiveData = new MutableLiveData<>();

    private DataRepository(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        fetchAllData();
        fetchTotalIncome();
        fetchTotalExpense();
    }
    
    public static synchronized DataRepository getInstance(Context context) {
        if (instance == null) {
            instance = new DataRepository(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE allExpenseIncome (id INTEGER PRIMARY KEY AUTOINCREMENT,type TEXT, amount DOUBLE, reason TEXT, time LONG)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE if exists allExpenseIncome");
    }

    public void addIncome(double amount, String reason) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues conval = new ContentValues();
        conval.put("type", "income");
        conval.put("amount", amount);
        conval.put("reason", reason);
        conval.put("time", System.currentTimeMillis());

        db.insert("allExpenseIncome", null, conval);

        fetchTotalIncome();
        fetchAllData();
    }

    public void addExpense(double amount, String reason) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues conval = new ContentValues();
        conval.put("type", "expense");
        conval.put("amount", amount);
        conval.put("reason", reason);
        conval.put("time", System.currentTimeMillis());

        db.insert("allExpenseIncome", null, conval);

        fetchTotalExpense();
        fetchAllData();
    }

    public void fetchTotalIncome() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(amount) FROM allExpenseIncome WHERE type = 'income';", null);

        if (cursor.moveToFirst()) {
            double total = cursor.getDouble(0);
            Log.d(TAG, "getTotalIncome: " + total);
            totalIncomeLiveData.setValue(total);
        }
        cursor.close();
    }

    public LiveData<Double> getTotalIncome() {
        return totalIncomeLiveData;
    }

    public void fetchTotalExpense() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(amount) FROM allExpenseIncome WHERE type = 'expense';", null);

        if (cursor.moveToFirst()) {
            double total = cursor.getDouble(0);
            Log.d(TAG, "getTotalExpense: " + total);
            totalExpenseLiveData.setValue(total);
        }
        cursor.close();
    }

    public LiveData<Double> getTotalExpense() {
        return totalExpenseLiveData;
    }

    public Cursor queryIndividualData(String type) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM allExpenseIncome WHERE type = '" + type + "' ORDER BY id DESC", null);
        return cursor;
    }

    public Cursor queryAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM allExpenseIncome ORDER BY id DESC", null);
        return cursor;
    }

    public void deleteItemById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("allExpenseIncome", "id=?", new String[]{String.valueOf(id)});
        fetchAllData();
        fetchTotalIncome();
        fetchTotalExpense();
    }

    private void fetchAllData() {
        Cursor individualData = queryAllData();

        List<ExpenseModel> expenseModelList = new ArrayList<>();
        if (individualData != null) {
            while (individualData.moveToNext()) {
                int id = individualData.getInt(0);
                String type = individualData.getString(1);
                double amount = individualData.getDouble(2);
                String reason = individualData.getString(3);
                long time = individualData.getLong(4);

                expenseModelList.add(new ExpenseModel(type, id, amount, reason, time));
            }
            individualData.close();
        }
        allLiveData.setValue(expenseModelList);
    }

    public LiveData<List<ExpenseModel>> getAllData() {
        return allLiveData;
    }

    private void fetchIndividualData(String type) {
        Cursor individualData = queryIndividualData(type);

        List<ExpenseModel> expenseModelList = new ArrayList<>();
        if (individualData != null) {
            while (individualData.moveToNext()) {
                int id = individualData.getInt(0);
                String expenseType = individualData.getString(1);
                double amount = individualData.getDouble(2);
                String reason = individualData.getString(3);
                long time = individualData.getLong(4);

                expenseModelList.add(new ExpenseModel(expenseType, id, amount, reason, time));
            }
            individualData.close();
        }
        individualLiveData.setValue(expenseModelList);
    }

    public LiveData<List<ExpenseModel>> getIndividualData(String type) {
        fetchIndividualData(type);
        return individualLiveData;
    }


}
