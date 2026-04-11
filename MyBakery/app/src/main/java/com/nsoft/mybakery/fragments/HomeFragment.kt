package com.nsoft.mybakery.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.nsoft.mybakery.R
import com.nsoft.mybakery.adapters.CustomerDialogAdapter
import com.nsoft.mybakery.adapters.DashboardRecyclerviewAdapter
import com.nsoft.mybakery.adapters.DialogProductRecyclerviewAdapter
import com.nsoft.mybakery.databinding.FragmentHomeBinding
import com.nsoft.mybakery.viewmodels.MyViewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var dashboardAdapter: DashboardRecyclerviewAdapter
    private lateinit var myViewModel: MyViewModel
    private lateinit var dialogAdapter: CustomerDialogAdapter
    private lateinit var dialogProductAdapter: DialogProductRecyclerviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // my code here ----------------------------------------------------------------------------
        myViewModel = ViewModelProvider(this@HomeFragment).get(MyViewModel::class.java)




        binding.customerEditText.setOnClickListener {
            val customerListView = LayoutInflater.from(context)
                .inflate(R.layout.customer_list_dialog, null, false)

            val builder = AlertDialog.Builder(context).apply {
                setView(customerListView)
            }

            val dialog = builder.create()
            dialog.show()
            dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

            val dialogSearchEditText =
                customerListView.findViewById<EditText>(R.id.dialogSearchEditText)
            val dialogCustomerRecyclerView =
                customerListView.findViewById<RecyclerView>(R.id.dialogCustomerRecyclerView)

        }

        binding.addItemButton.setOnClickListener {
            val dashboardAddProductDialogView = LayoutInflater.from(getContext())
                .inflate(R.layout.dashboard_add_product_dialog, null, false)
            val builder = AlertDialog.Builder(getContext())
            builder.setView(dashboardAddProductDialogView)
            builder.setCancelable(false)

            val alertDialog = builder.create()
            alertDialog.show()

            alertDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

            val dialogProductRecyclerView =
                dashboardAddProductDialogView.findViewById<RecyclerView>(R.id.dialogProductRecyclerView)
            val dialogCancelButton =
                dashboardAddProductDialogView.findViewById<Button>(R.id.dialogCancelButton)
            val dialogAddButton =
                dashboardAddProductDialogView.findViewById<Button>(R.id.dialogAddButton)
            val dialogProductSearchEditText =
                dashboardAddProductDialogView.findViewById<EditText>(R.id.dialogProductSearchEditText)


        }


        // -----------------------------------------------------------------------------------------
    }


    override fun onResume() {
        super.onResume()

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }


}