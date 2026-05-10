package com.nsoft.mybakery.fragments

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nsoft.mybakery.R
import com.nsoft.mybakery.adapterdecorators.ItemDividerDecoration
import com.nsoft.mybakery.adapterdecorators.ItemSpacingDecorator
import com.nsoft.mybakery.adapters.DashboardTransactionsAdapter
import com.nsoft.mybakery.databinding.FragmentHomeBinding
import com.nsoft.mybakery.viewmodels.MyViewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var dashboardTransactionsAdapter: DashboardTransactionsAdapter
    private val myViewModel: MyViewModel by viewModels()
    private var toast: Toast? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // my code here ----------------------------------------------------------------------------

        // adapter code start ----------
        dashboardTransactionsAdapter = DashboardTransactionsAdapter()

        binding.transactionRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.transactionRecyclerView.adapter = dashboardTransactionsAdapter
        // adapter code end ---------


        // observers start here --------------------------------------------------------------------
        myViewModel.getAllCustomers().observe(viewLifecycleOwner) { allCustomers ->
            binding.totalCustomersTextView.text = "${allCustomers.size}"
        }

        myViewModel.getAllTransactions().observe(viewLifecycleOwner) { allTransactions ->
            dashboardTransactionsAdapter.setTransactions(allTransactions)
        }
        // observers end here ----------------------------------------------------------------------

        binding.leftStockTextView.text = binding.stockTextView.text.toString()

        // button clicks start here ------------------------
        binding.stockEdit.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.edit_stock_dialog, null, false)
            val builder = AlertDialog.Builder(context).apply {
                setView(dialogView)
                setCancelable(false)
            }

            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialogView.findViewById<Button>(R.id.dialogCancelButton).setOnClickListener {
                dialog.dismiss()
            }

            val initialStock = binding.stockTextView.text.toString()

            val dialogEditText = dialogView.findViewById<EditText>(R.id.dialogStockEditText)

            dialogEditText.setText(initialStock)


            dialogView.findViewById<Button>(R.id.dialogAddButton).setOnClickListener {
                binding.stockTextView.text = if (dialogEditText.text.isNotEmpty()) dialogEditText.text.toString() else "0"
                dialog.dismiss()
                showToast("Stock updated.")
            }

        }
        // button clicks ends here ---------------------

        // -----------------------------------------------------------------------------------------
    }

    fun showToast(text: String){
        toast?.cancel()
        toast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
        toast?.show()
    }


}