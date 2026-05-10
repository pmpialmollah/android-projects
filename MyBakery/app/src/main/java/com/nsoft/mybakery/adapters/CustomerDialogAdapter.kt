package com.nsoft.mybakery.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nsoft.mybakery.R
import com.nsoft.mybakery.models.Customer

class CustomerDialogAdapter(val clickListener: ClickListener) :
    RecyclerView.Adapter<CustomerDialogAdapter.MyViewHolder?>() {

    private var customersList = listOf<Customer>()

    fun setCustomersList(customersList: List<Customer>) {
        this.customersList = customersList
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dialogCustomerProfileImageView =
            itemView.findViewById<ImageView?>(R.id.dialogCustomerProfileImageView)
        val dialogCustomerNameTextView =
            itemView.findViewById<TextView?>(R.id.dialogCustomerNameTextView)
        val dialogCustomerAddressTextView =
            itemView.findViewById<TextView?>(R.id.dialogCustomerAddressTextView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myView = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.dialog_customer_item, parent, false)
        return CustomerDialogAdapter.MyViewHolder(myView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val customer = customersList[position];
        holder.dialogCustomerNameTextView.setText(customer.name);
        holder.dialogCustomerAddressTextView.setText("(" + customer.address + ")");

        holder.itemView.setOnClickListener {
            clickListener.onClick(customer)
        }

    }

    override fun getItemCount(): Int {
        return customersList.size
    }

    public interface ClickListener {
        fun onClick(customer: Customer)
    }
}
