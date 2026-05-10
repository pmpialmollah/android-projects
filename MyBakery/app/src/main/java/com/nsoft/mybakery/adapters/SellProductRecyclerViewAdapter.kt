package com.nsoft.mybakery.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.nsoft.mybakery.R
import androidx.recyclerview.widget.RecyclerView
import com.nsoft.mybakery.models.Product

class SellProductRecyclerViewAdapter :
    RecyclerView.Adapter<SellProductRecyclerViewAdapter.MyViewModel>() {

    private var productList = mutableListOf<Product>()
    private var onListChangedListener: (() -> Unit)? = null

    fun setOnListChangedListener(listener: () -> Unit) {
        this.onListChangedListener = listener
    }

    fun getProductList(): List<Product> = productList

    fun setProductList(lists: List<Product>){
        this.productList = lists.toMutableList()
        notifyDataSetChanged()
        onListChangedListener?.invoke()
    }

    fun addProduct(product: Product) {
        val newProduct = Product(product.id, product.image, product.name, product.quantity, product.price)
        this.productList.add(newProduct)
        notifyItemInserted(productList.size - 1)
        onListChangedListener?.invoke()
    }

    fun removeProduct(position: Int) {
        if (position in productList.indices) {
            productList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, productList.size)
            onListChangedListener?.invoke()
        }
    }

    fun getTotalPrice(): Double {
        var total = 0.0
        for (product in productList) {
            total += (product.price * product.quantity)
        }
        return total
    }

    class MyViewModel(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemCountTextView: TextView = itemView.findViewById(R.id.itemCountTextView)
        val itemNameTextView: TextView = itemView.findViewById(R.id.itemNameTextView)
        val itemPriceTextView: TextView = itemView.findViewById(R.id.itemPriceTextView)
        val removeProductImageView: ImageView = itemView.findViewById(R.id.removeProductImageView)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        position: Int
    ): MyViewModel {
        val myView =
            LayoutInflater.from(parent.context).inflate(R.layout.dashboard_item_layout, parent, false)
        return MyViewModel(myView)
    }

    override fun onBindViewHolder(
        holder: MyViewModel,
        position: Int
    ) {
        val product = productList[position]
        holder.itemNameTextView.text = product.name
        holder.itemPriceTextView.text = product.price.toString()
        holder.itemCountTextView.text = product.quantity.toString()

        holder.removeProductImageView.setOnClickListener {
            removeProduct(holder.bindingAdapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

}
