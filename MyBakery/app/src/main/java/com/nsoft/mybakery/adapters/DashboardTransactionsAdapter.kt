package com.nsoft.mybakery.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nsoft.mybakery.R
import com.nsoft.mybakery.models.ProductSell
import com.nsoft.mybakery.models.SellTransaction

class DashboardTransactionsAdapter :
    RecyclerView.Adapter<DashboardTransactionsAdapter.MyViewHolder?>() {
    private var transactionList: List<SellTransaction> = ArrayList<SellTransaction>()
    public fun setTransactions(list: List<SellTransaction>) {
        this.transactionList = list
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val customerName = itemView.findViewById<TextView>(R.id.customerName)
        val productName = itemView.findViewById<TextView>(R.id.productName)
        val productQuantity = itemView.findViewById<TextView>(R.id.productQuantity)
        val productPrice = itemView.findViewById<TextView>(R.id.productPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myView = LayoutInflater.from(parent.context)
            .inflate(R.layout.dashboard_transaction_item, parent, false)
        return DashboardTransactionsAdapter.MyViewHolder(myView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val transaction = transactionList[position]

        holder.customerName.text = transaction.customer?.name ?: "Unknown"

        if (transaction.products.isNotEmpty()) {
            val allProducts = transaction.products.flatMap { it.keys }

            val finalResult = allProducts.joinToString(separator = ", ") { it.name }

            holder.productName.text = finalResult
        } else {
            holder.productName.text = "No Products"
        }
        holder.productQuantity.text = "${transaction.products.sumOf { map -> map.values.sum() }}"
        holder.productPrice.text = "${transaction.product_total_price}"
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }
}
