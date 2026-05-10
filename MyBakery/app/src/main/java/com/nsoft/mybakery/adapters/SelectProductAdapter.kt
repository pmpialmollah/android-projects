package com.nsoft.mybakery.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nsoft.mybakery.R
import com.nsoft.mybakery.models.Product

class SelectProductAdapter(private var clickListener: ClickListener? = null) :
    RecyclerView.Adapter<SelectProductAdapter.MyViewHolder?>() {

    private var productList = listOf<Product>()

    fun setOnItemClickListener(listener: ClickListener) {
        this.clickListener = listener
    }

    public fun setProductList(productList: List<Product>) {
        this.productList = productList
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName = itemView.findViewById<TextView>(R.id.productAddItemName)
        val productPrice = itemView.findViewById<TextView>(R.id.productAddItemPrice)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myView = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.product_add_item, parent, false)
        return SelectProductAdapter.MyViewHolder(myView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val context = holder.itemView.context
        val product = productList[position]

        holder.productName.text = "${product.name}"

        holder.productPrice.text = "${context.getString(R.string.bdt)}${product.price}"

        holder.itemView.setOnClickListener {
            clickListener?.onClick(product)
        }

    }

    override fun getItemCount(): Int {
        return productList.size
    }

    public interface ClickListener {
        public fun onClick(product: Product)
    }
}
