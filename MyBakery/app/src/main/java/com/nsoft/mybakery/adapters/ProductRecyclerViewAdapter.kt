package com.nsoft.mybakery.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nsoft.mybakery.R
import com.nsoft.mybakery.models.Product

class ProductRecyclerViewAdapter(private val clickListener: ClickListener) :
    RecyclerView.Adapter<ProductRecyclerViewAdapter.MyViewHolder?>() {

    private var productList = ArrayList<Product>()
    fun setProductList(productList: List<Product>) {
        this.productList = productList as ArrayList<Product>
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val productImageView = itemView.findViewById<ImageView?>(R.id.productImageView)
        val productDeleteImageView = itemView.findViewById<ImageView?>(R.id.productDeleteImageView)
        val productNameTextView = itemView.findViewById<TextView?>(R.id.productNameTextView)
        val productPriceTextView = itemView.findViewById<TextView?>(R.id.productPriceTextView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myView = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_item_layout, parent, false)

        return ProductRecyclerViewAdapter.MyViewHolder(myView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val product = productList[position]

        holder.productNameTextView.text = product.name
        holder.productPriceTextView.text = "${product.price}"

        holder.productDeleteImageView.setOnClickListener {
            clickListener.onDeleteClickListener(product)
        }

        holder.itemView.setOnLongClickListener {
            clickListener.onItemLongClick(product)
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    interface ClickListener {
        fun onDeleteClickListener(product: Product)
        fun onItemLongClick(product: Product)
    }
}
