package com.nsoft.mybakery.sqlitedatabase

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.nsoft.mybakery.models.Customer
import com.nsoft.mybakery.models.Product
import com.nsoft.mybakery.models.SellTransaction
import com.nsoft.mybakery.models.Seller
import com.nsoft.mybakery.models.Transaction
import com.nsoft.mybakery.utils.MyUtils

class DBHelper(private val context: Context?) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        val myUtils = MyUtils()

        // db name and version
        const val DB_NAME = "MY_DATABASE"
        const val DB_VERSION = 3

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

        // transaction table
        const val TT_TABLE_NAME = "transaction_table"
        const val TT_COL_ID = "id"
        const val TT_COL_CUSTOMER_ID = "customer_id"
        const val TT_COL_PRODUCT_ID_AND_QUANTITY = "product_id_and_quantity"
        const val TT_COL_PRODUCT_QUANTITY = "product_quantity"
        const val TT_COL_PRODUCT_TOTAL_PRICE = "product_total_price"
        const val TT_COL_TOTAL_PAID = "total_paid"
        const val TT_COL_TOTAL_DUE = "total_due"
        const val TT_COL_SELLER_ID = "seller_id"
        const val TT_COL_TIMESTAMP = "timestamp"
    }


    override fun onCreate(db: SQLiteDatabase) {
        // create customer table
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

        // create product table
        val createProductsTable = """
            CREATE TABLE $PT_TABLE_NAME (
                    $PT_COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $PT_COL_IMAGE TEXT,
                    $PT_COL_NAME TEXT,
                    $PT_COL_PRICE TEXT
);
        """.trimIndent()

        db.execSQL(createProductsTable)


        // create transaction table
        val createTransactionTable = """
            CREATE TABLE $TT_TABLE_NAME (
                $TT_COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $TT_COL_CUSTOMER_ID INTEGER,
                $TT_COL_PRODUCT_ID_AND_QUANTITY TEXT,
                $TT_COL_PRODUCT_QUANTITY DOUBLE,
                $TT_COL_PRODUCT_TOTAL_PRICE DOUBLE,
                $TT_COL_TOTAL_PAID DOUBLE,
                $TT_COL_TOTAL_DUE DOUBLE,
                $TT_COL_SELLER_ID INTEGER,
                $TT_COL_TIMESTAMP DATETIME DEFAULT CURRENT_TIMESTAMP
            );
        """.trimIndent()

        db.execSQL(createTransactionTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $CT_TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $PT_TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $TT_TABLE_NAME")
        onCreate(db)
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

    fun updateProduct(product: Product): Int {
        val db = writableDatabase

        val conVal = ContentValues().apply {
            put(PT_COL_IMAGE, product.image)
            put(PT_COL_NAME, product.name)
            put(PT_COL_PRICE, product.price)
        }

        val result =
            db.update(PT_TABLE_NAME, conVal, "$PT_COL_ID = ?", arrayOf(product.id.toString()))

        db?.close()
        return result
    }

    fun getAllProducts(): List<Product> {
        val allProductList = mutableListOf<Product>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * from $PT_TABLE_NAME ORDER BY $PT_COL_ID DESC;", null)

        while (cursor.moveToNext()) {
            val productId = cursor.getInt(cursor.getColumnIndexOrThrow(PT_COL_ID))
            val productImage = cursor.getString(cursor.getColumnIndexOrThrow(PT_COL_IMAGE))
            val productName = cursor.getString(cursor.getColumnIndexOrThrow(PT_COL_NAME))
            val productPrice = cursor.getString(cursor.getColumnIndexOrThrow(PT_COL_PRICE))

            val product = Product(productId, productImage, productName, 0.0, productPrice.toDouble())

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

    fun insertCustomer(customer: Customer): Long {
        val db = writableDatabase

        val conVal = ContentValues().apply {
            put(CT_COL_PROFILE, customer.profile)
            put(CT_COL_NAME, customer.name)
            put(CT_COL_ADDRESS, customer.address)
            put(CT_COL_NUMBER, customer.number)
        }
        val result = db.insert(CT_TABLE_NAME, null, conVal)

        db?.close()
        return result
    }

    fun getAllCustomers(): List<Customer> {
        val allCustomers = mutableListOf<Customer>()

        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $CT_TABLE_NAME ORDER BY $CT_COL_ID DESC;", null)

        while (cursor.moveToNext()) {
            val customerId = cursor.getInt(cursor.getColumnIndexOrThrow(CT_COL_ID))
            val customerProfile = cursor.getString(cursor.getColumnIndexOrThrow(CT_COL_PROFILE))
            val customerName = cursor.getString(cursor.getColumnIndexOrThrow(CT_COL_NAME))
            val customerAddress = cursor.getString(cursor.getColumnIndexOrThrow(CT_COL_ADDRESS))
            val customerNumber = cursor.getString(cursor.getColumnIndexOrThrow(CT_COL_NUMBER))

            val customer =
                Customer(customerId, customerProfile, customerName, customerAddress, customerNumber)

            allCustomers.add(customer)
        }

        cursor?.close()
        db?.close()

        return allCustomers
    }

    fun deleteCustomer(id: Int): Int {
        val db = writableDatabase
        val result = db.delete(CT_TABLE_NAME, "$CT_COL_ID = ?", arrayOf(id.toString()))
        db?.close()
        return result
    }

    fun updateCustomer(customer: Customer): Int {
        val db = writableDatabase
        val conVal = ContentValues().apply {
            put(CT_COL_PROFILE, customer.profile)
            put(CT_COL_NAME, customer.name)
            put(CT_COL_ADDRESS, customer.address)
            put(CT_COL_NUMBER, customer.number)
        }
        val result =
            db.update(CT_TABLE_NAME, conVal, "$CT_COL_ID = ?", arrayOf(customer.id.toString()))

        db?.close()
        return result
    }

    fun insertTransaction(sellTransaction: SellTransaction): Long {
        val db = writableDatabase
        val conVal = ContentValues()
        var result: Long = -1

        db.beginTransaction()
        try {
            val product_ids_and_quantity = StringBuilder()
            var total_quantity = 0.0
            for (productMap in sellTransaction.products) {
                for ((product, quantity) in productMap) {
                    product_ids_and_quantity.append("${product.id} => ${product.price} => $quantity\n")
                    total_quantity += quantity.toDouble()
                }
            }

            conVal.apply {
                put(TT_COL_CUSTOMER_ID, sellTransaction.customer?.id)
                put(TT_COL_PRODUCT_ID_AND_QUANTITY, product_ids_and_quantity.toString())
                put(TT_COL_PRODUCT_QUANTITY, total_quantity)
                put(TT_COL_PRODUCT_TOTAL_PRICE, sellTransaction.product_total_price)
                put(TT_COL_TOTAL_PAID, sellTransaction.total_paid)
                put(TT_COL_TOTAL_DUE, sellTransaction.total_due)
                put(TT_COL_SELLER_ID, sellTransaction.seller.id)
            }
            result = db.insert(TT_TABLE_NAME, null, conVal)
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.endTransaction()
            db.close()
        }

        return result
    }

    fun getAllTransactions(): ArrayList<SellTransaction> {
        val allTransactios = mutableListOf<SellTransaction>()

        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TT_TABLE_NAME ORDER BY $TT_COL_ID DESC", null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(TT_COL_ID))
            val customer_id = cursor.getInt(cursor.getColumnIndexOrThrow(TT_COL_CUSTOMER_ID))
            val product_ids_and_quantity =
                cursor.getString(cursor.getColumnIndexOrThrow(TT_COL_PRODUCT_ID_AND_QUANTITY))
            val product_quantity =
                cursor.getDouble(cursor.getColumnIndexOrThrow(TT_COL_PRODUCT_QUANTITY))
            val product_total_price =
                cursor.getDouble(cursor.getColumnIndexOrThrow(TT_COL_PRODUCT_TOTAL_PRICE))
            val total_paid = cursor.getDouble(cursor.getColumnIndexOrThrow(TT_COL_TOTAL_PAID))
            val total_due = cursor.getDouble(cursor.getColumnIndexOrThrow(TT_COL_TOTAL_DUE))
            val seller_id = cursor.getInt(cursor.getColumnIndexOrThrow(TT_COL_SELLER_ID))
            val timestamp = cursor.getString(cursor.getColumnIndexOrThrow(TT_COL_TIMESTAMP))

            // get customer-----------------
            var customer: Customer = Customer(0, "", "", "", "")
            val customerCursor =
                db.rawQuery("SELECT * FROM $CT_TABLE_NAME WHERE $CT_COL_ID = $customer_id", null)
            if (customerCursor.moveToFirst()) {
                val customerId = customerCursor.getInt(customerCursor.getColumnIndexOrThrow(CT_COL_ID))
                val customerProfile =
                    customerCursor.getString(customerCursor.getColumnIndexOrThrow(CT_COL_PROFILE))
                val customerName =
                    customerCursor.getString(customerCursor.getColumnIndexOrThrow(CT_COL_NAME))
                val customerAddress =
                    customerCursor.getString(customerCursor.getColumnIndexOrThrow(CT_COL_ADDRESS))
                val customerNumber =
                    customerCursor.getString(customerCursor.getColumnIndexOrThrow(CT_COL_NUMBER))

                customer = Customer(
                    customerId,
                    customerProfile,
                    customerName,
                    customerAddress,
                    customerNumber
                )
            }
            customerCursor.close()

            // get products -------------------
            var productsAndQuantity = ArrayList<HashMap<Product, Double>>()
            val productsIdAndQuantityMap = myUtils.parseStringToMap(product_ids_and_quantity)

            for (map in productsIdAndQuantityMap) {
                var product: Product = Product(0, "", "", 0.0, 0.0)
                var quantity: Double = 0.0
                for ((product_id, price_quantity) in map) {
                    val productCursor =
                        db.rawQuery(
                            "SELECT * FROM $PT_TABLE_NAME WHERE $PT_COL_ID = $product_id ORDER BY $PT_COL_ID DESC",
                            null
                        )
                    if (productCursor.moveToFirst()) {
                        val productId =
                            productCursor.getInt(productCursor.getColumnIndexOrThrow(PT_COL_ID))
                        val productImage =
                            productCursor.getString(productCursor.getColumnIndexOrThrow(PT_COL_IMAGE))
                        val productName =
                            productCursor.getString(productCursor.getColumnIndexOrThrow(PT_COL_NAME))
                        val productQuantity = price_quantity[1]
                        val productPrice = price_quantity[0]

                        product = Product(
                            productId,
                            productImage,
                            productName,
                            productQuantity,
                            productPrice
                        )

                    }
                    productCursor.close()
                    quantity = price_quantity[1]
                }

                val productAndQuantityMap = HashMap<Product, Double>().apply {
                    put(product, quantity)
                }

                productsAndQuantity.add(0, productAndQuantityMap)
            }


            // seller ------------------------------
            val seller = Seller(0, "", "", "", "")

            // selltransaction -----------------------
            val sellTransaction = SellTransaction(
                id,
                customer,
                productsAndQuantity,
                product_total_price,
                total_paid,
                total_due,
                seller,
                timestamp
            )

            allTransactios.add(0, sellTransaction)

        }


        cursor.close()
        db.close()
        return allTransactios as ArrayList<SellTransaction>
    }


}
