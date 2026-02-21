package com.nsoft.sqliteapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "My_DATABASE";
    private static final String DB_NAME = "MY_DATABASE";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
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
    }

    public void addExpense(double amount, String reason) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues conval = new ContentValues();
        conval.put("type", "expense");
        conval.put("amount", amount);
        conval.put("reason", reason);
        conval.put("time", System.currentTimeMillis());

        db.insert("allExpenseIncome", null, conval);

    }

    public double getTotalIncome() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(amount) FROM allExpenseIncome WHERE type = 'income';", null);

        if (cursor.moveToFirst()) {
            double total = cursor.getDouble(0);
            Log.d(TAG, "getTotalIncome: " + total);
            return total;
        }
        return 0;
    }

    public double getTotalExpense() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(amount) FROM allExpenseIncome WHERE type = 'expense';", null);

        if (cursor.moveToFirst()) {
            double total = cursor.getDouble(0);
            Log.d(TAG, "getTotalExpense: " + total);
            return total;
        }
        return 0;
    }

    public double getTotal() {
        return getTotalIncome() - getTotalExpense();
    }

    public Cursor getIndividualData(String type) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM allExpenseIncome WHERE type = '" + type + "' ORDER BY id DESC", null);
        return cursor;
    }
    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM allExpenseIncome ORDER BY id DESC", null);
        return cursor;
    }

    public void deleteItemById(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM allExpenseIncome WHERE id = '" + id + "'");
    }



}
