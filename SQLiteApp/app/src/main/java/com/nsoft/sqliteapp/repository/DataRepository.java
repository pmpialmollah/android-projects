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
    private MutableLiveData<List<ExpenseModel>> allLiveData = new MutableLiveData<>();
    private MutableLiveData<List<ExpenseModel>> individualLiveData = new MutableLiveData<>();
    private double totalIncome = 0;
    private double totalExpense = 0;

    public DataRepository(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        fetchAllData();
        fetchTotalIncome();
        fetchTotalExpense();
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
    }

    public void fetchTotalIncome() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(amount) FROM allExpenseIncome WHERE type = 'income';", null);

        if (cursor.moveToFirst()) {
            double total = cursor.getDouble(0);
            Log.d(TAG, "getTotalIncome: " + total);
            totalIncome = total;
        }
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public void fetchTotalExpense() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(amount) FROM allExpenseIncome WHERE type = 'expense';", null);

        if (cursor.moveToFirst()) {
            double total = cursor.getDouble(0);
            Log.d(TAG, "getTotalExpense: " + total);
            totalExpense = total;
        }
    }

    public double getTotalExpense() {
        return totalExpense;
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
        db.execSQL("DELETE FROM allExpenseIncome WHERE id = '" + id + "'");
    }

    private void fetchAllData() {
        Cursor individualData = queryAllData();

        if (individualData != null && individualData.getCount() > 0) {

            List<ExpenseModel> expenseModelList = new ArrayList<>();
            while (individualData.moveToNext()) {
                int id = individualData.getInt(0);
                String type = individualData.getString(1);
                double amount = individualData.getDouble(2);
                String reason = individualData.getString(3);
                long time = individualData.getLong(4);

                expenseModelList.add(new ExpenseModel(type, id, amount, reason, time));
            }

            allLiveData.setValue(expenseModelList);

        }
    }

    public LiveData<List<ExpenseModel>> getAllData() {
        return allLiveData;
    }

    private void fetchIndividualData(String type) {
        Cursor individualData = queryIndividualData(type);

        if (individualData != null && individualData.getCount() > 0) {

            List<ExpenseModel> expenseModelList = new ArrayList<>();
            while (individualData.moveToNext()) {
                int id = individualData.getInt(0);
                String expenseType = individualData.getString(1);
                double amount = individualData.getDouble(2);
                String reason = individualData.getString(3);
                long time = individualData.getLong(4);

                expenseModelList.add(new ExpenseModel(expenseType, id, amount, reason, time));
            }

            individualLiveData.setValue(expenseModelList);

        }

    }

    public LiveData<List<ExpenseModel>> getIndividualData(String type) {
        fetchIndividualData(type);
        return individualLiveData;
    }


}
