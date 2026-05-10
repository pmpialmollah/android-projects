package com.nsoft.mybakery.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nsoft.mybakery.R
import com.nsoft.mybakery.models.Customer

class CustomerRecyclerViewAdapter(private val clickListener: ClickListener) :
    RecyclerView.Adapter<CustomerRecyclerViewAdapter.MyViewHolder?>() {

    private var allCustomersList = ArrayList<Customer>()

    fun setAllCustomersList(customersList: List<Customer>) {
        this.allCustomersList = customersList as ArrayList<Customer>
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val customerProfileImageView =
            itemView.findViewById<ImageView>(R.id.customerProfileImageView)
        val customerDeleteImageView = itemView.findViewById<ImageView>(R.id.customerDeleteImageView)
        val customerNameTextView = itemView.findViewById<TextView>(R.id.customerNameTextView)
        val customerAddressTextView = itemView.findViewById<TextView>(R.id.customerAddressTextView)
        val customerNumberTextView = itemView.findViewById<TextView>(R.id.customerNumberTextView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myView = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.customer_item_layout, parent, false)
        return CustomerRecyclerViewAdapter.MyViewHolder(myView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val customer = allCustomersList[position]

        holder.customerNameTextView.text = customer.name
        holder.customerAddressTextView.text = customer.address
        holder.customerNumberTextView.text = "(${customer.number})"

        holder.customerDeleteImageView.setOnClickListener {
            clickListener.deleteClickListener(customer)
        }

        holder.itemView.setOnClickListener {
            clickListener.itemLongClick(customer)
        }
    }

    override fun getItemCount(): Int {
        return allCustomersList.size
    }

    interface ClickListener {
        fun deleteClickListener(customer: Customer)
        fun itemLongClick(customer: Customer)
    }

}
