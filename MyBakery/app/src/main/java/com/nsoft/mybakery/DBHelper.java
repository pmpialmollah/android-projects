package com.nsoft.mybakery;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "BAKERY_DATABASE";
    private static final int DB_VERSION = 1;
    private Context context;

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE customers_table " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "profile TEXT," +
                "name TEXT," +
                "address TEXT," +
                "number TEXT);");
        db.execSQL("CREATE TABLE products_table (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "image TEXT," +
                "name TEXT," +
                "price TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE if exists customers_table");
        db.execSQL("DROP TABLE if exists products_table");
    }

    public void addProduct(String image, String name, String price) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO products_table (image, name, price) VALUES ('" + image + "', '" + name + "', '" + price + "')");
    }

    public void updateProduct(String image, String name, String price, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE products_table SET image = '" + image + "', name = '" + name + "', price = '" + price + "' WHERE id = '" + id + "'; ");
    }

    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from products_table;", null);
        return cursor;
    }

    public void deleteProductById(int position) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM products_table WHERE id = '" + position + "';");
    }

    public Cursor getProductByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM products_table WHERE name LIKE ? ORDER BY name;", new String[]{"%" + name + "%"}, null);
        return cursor;
    }


    public void addCustomer(String profile, String name, String address, String number) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(String.format("INSERT INTO customers_table (profile, name, address, number) VALUES ('%s', '%s', '%s', '%s');", profile, name, address, number));
        Toast.makeText(context, "Customer inserted...", Toast.LENGTH_SHORT).show();
    }

    public Cursor getAllCustomers() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM customers_table ORDER BY name;", null);
        return cursor;
    }

    public int getCustomerCount() {
        int totalCustomers = getAllCustomers().getCount();
        return totalCustomers;
    }

    public Cursor getCustomerByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM customers_table WHERE name LIKE ? ORDER BY name;", new String[]{"%" + name + "%"}, null);
        return cursor;
    }

    public void deleteCustomerById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(String.format("DELETE FROM customers_table WHERE id = %s", id));
    }

    public void updateCustomer(String profile, String name, String address, String number, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(String.format("UPDATE customers_table SET profile = '%s', name = '%s', address = '%s', number = '%s' WHERE id = '%s'", profile, name, address, number, id));
    }

}
