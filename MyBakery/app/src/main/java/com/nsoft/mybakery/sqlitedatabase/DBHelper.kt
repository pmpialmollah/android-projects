package com.nsoft.mybakery.sqlitedatabase

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.nsoft.mybakery.models.Product

class DBHelper(private val context: Context?) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        // db name and version
        const val DB_NAME = "MY_DATABASE"
        const val DB_VERSION = 1

        // customer table
        const val CT_TABLE_NAME = "customers_table"
        const val CT_COL_ID = "id"
        const val CT_COL_PROFILE = "profile"
        const val CT_COL_NAME = "name"
        const val CT_COL_ADDRESS = "address"
        const val CT_COL_NUMBER = "number"

        //product table
        const val PT_TABLE_NAME = "products_table"
        const val PT_COL_ID = "id"
        const val PT_COL_IMAGE = "image"
        const val PT_COL_NAME = "name"
        const val PT_COL_PRICE = "price"

    }


    override fun onCreate(db: SQLiteDatabase) {

        val createCustomersTable = """
            CREATE TABLE $CT_TABLE_NAME(
                    $CT_COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $CT_COL_PROFILE TEXT,
                    $CT_COL_NAME TEXT,
                    $CT_COL_ADDRESS TEXT,
                    $CT_COL_NUMBER TEXT
                    );
        """.trimIndent()

        db.execSQL(createCustomersTable)

        val createProductsTable = """
            CREATE TABLE $PT_TABLE_NAME (
                    $PT_COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $PT_COL_IMAGE TEXT,
                    $PT_COL_NAME TEXT,
                    $PT_COL_PRICE TEXT
);
        """.trimIndent()

        db.execSQL(createProductsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE if exists $CT_TABLE_NAME")
        db.execSQL("DROP TABLE if exists $PT_TABLE_NAME")
    }

    fun insertProduct(product: Product): Long {
        val db = writableDatabase
        val conVal = ContentValues().apply {
            put(PT_COL_IMAGE, product.image)
            put(PT_COL_NAME, product.name)
            put(PT_COL_PRICE, product.price)
        }
        val result = db.insert(PT_TABLE_NAME, null, conVal)
        db?.close()

        return result
    }

    fun updateProduct(image: String?, name: String?, price: String?, id: Int) {
        val db = this.getWritableDatabase()
        db.execSQL("UPDATE products_table SET image = '" + image + "', name = '" + name + "', price = '" + price + "' WHERE id = '" + id + "'; ")
    }

    fun getAllProducts(): List<Product> {
        val allProductList = mutableListOf<Product>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * from products_table;", null)

        while (cursor.moveToNext()) {
            val productId = cursor.getInt(cursor.getColumnIndexOrThrow(PT_COL_ID))
            val productImage = cursor.getString(cursor.getColumnIndexOrThrow(PT_COL_IMAGE))
            val productName = cursor.getString(cursor.getColumnIndexOrThrow(PT_COL_NAME))
            val productPrice = cursor.getString(cursor.getColumnIndexOrThrow(PT_COL_PRICE))

            val product = Product(productId, productImage, productName, productPrice)

            allProductList.add(product)
        }

        cursor?.close()
        db?.close()

        return allProductList
    }

    fun deleteProductById(id: Int): Int {
        val db = writableDatabase
        val result = db.delete(PT_TABLE_NAME, "$PT_COL_ID = ?", arrayOf(id.toString()))

        db?.close()
        return result
    }

    fun getProductByName(name: String?): Cursor {
        val db = this.getReadableDatabase()
        val cursor = db.rawQuery(
            "SELECT * FROM products_table WHERE name LIKE ? ORDER BY name;",
            arrayOf<String>("%" + name + "%"),
            null
        )
        return cursor
    }


    fun addCustomer(profile: String, name: String?, address: String?, number: String?) {
        val db = this.getWritableDatabase()
        db.execSQL(
            String.format(
                "INSERT INTO customers_table (profile, name, address, number) VALUES ('%s', '%s', '%s', '%s');",
                profile,
                name,
                address,
                number
            )
        )
        Toast.makeText(context, "Customer inserted...", Toast.LENGTH_SHORT).show()
    }

    val allCustomers: Cursor
        get() {
            val db = this.getReadableDatabase()
            val cursor =
                db.rawQuery("SELECT * FROM customers_table ORDER BY name;", null)
            return cursor
        }

    val customerCount: Int
        get() {
            val totalCustomers = this.allCustomers.getCount()
            return totalCustomers
        }

    fun getCustomerByName(name: String?): Cursor {
        val db = this.getReadableDatabase()
        val cursor = db.rawQuery(
            "SELECT * FROM customers_table WHERE name LIKE ? ORDER BY name;",
            arrayOf<String>("%" + name + "%"),
            null
        )
        return cursor
    }

    fun deleteCustomerById(id: Int) {
        val db = this.getWritableDatabase()
        db.execSQL(String.format("DELETE FROM customers_table WHERE id = %s", id))
    }

    fun updateCustomer(profile: String, name: String?, address: String?, number: String?, id: Int) {
        val db = this.getWritableDatabase()
        db.execSQL(
            String.format(
                "UPDATE customers_table SET profile = '%s', name = '%s', address = '%s', number = '%s' WHERE id = '%s'",
                profile,
                name,
                address,
                number,
                id
            )
        )
    }

}
