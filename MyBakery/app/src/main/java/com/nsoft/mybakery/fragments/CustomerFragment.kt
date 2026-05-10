package com.nsoft.mybakery.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.nsoft.mybakery.R
import com.nsoft.mybakery.adapterdecorators.ItemSpacingDecorator
import com.nsoft.mybakery.adapters.CustomerRecyclerViewAdapter
import com.nsoft.mybakery.databinding.FragmentCustomerBinding
import com.nsoft.mybakery.models.Customer
import com.nsoft.mybakery.viewmodels.MyViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CustomerFragment : Fragment() {
    private lateinit var binding: FragmentCustomerBinding
    private val myViewModel: MyViewModel by viewModels()
    private var toast: Toast? = null
    private var searchJob: Job? = null

    private val myAdapter by lazy {
        CustomerRecyclerViewAdapter(
            object : CustomerRecyclerViewAdapter.ClickListener {

                override fun deleteClickListener(customer: Customer) {
                    AlertDialog.Builder(context)
                        .setTitle("Confirm deletion!")
                        .setMessage("Customer details\nName: ${customer.name}\nAddress: ${customer.address}\nMobile: ${customer.number}\n\nDo you really want to delete this customer?")
                        .setPositiveButton("Yes") { dialog, _ ->
                            myViewModel.deleteCustomer(customer.id)
                            dialog.dismiss()
                        }
                        .setNegativeButton("No") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }

                override fun itemLongClick(customer: Customer) {
                    val customerUpdateDialView =
                        LayoutInflater.from(context)
                            .inflate(R.layout.add_customer_dialog, null, false)

                    val dialogBuilder = AlertDialog.Builder(context)
                    dialogBuilder.setView(customerUpdateDialView)
                    dialogBuilder.setCancelable(false)

                    val dialog = dialogBuilder.create()
                    dialog.show()
                    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

                    val cancelButton =
                        customerUpdateDialView.findViewById<Button>(R.id.dialogCancelButton)
                    val updateButton =
                        customerUpdateDialView.findViewById<Button>(R.id.dialogAddButton)
                    val profileImageView =
                        customerUpdateDialView.findViewById<ImageView?>(R.id.dialogCustomerImage)
                    val nameEditText =
                        customerUpdateDialView.findViewById<EditText>(R.id.dialogCustomerNameEditText)
                    val addressEditText =
                        customerUpdateDialView.findViewById<EditText>(R.id.dialogCustomerAddressEditText)
                    val numberEditText =
                        customerUpdateDialView.findViewById<EditText>(R.id.dialogCustomerNumberEditText)

                    updateButton.text = "Update"
                    nameEditText.setText(customer.name)
                    addressEditText.setText(customer.address)
                    numberEditText.setText(customer.number)


                    cancelButton.setOnClickListener {
                        dialog.dismiss()
                    }

                    updateButton.setOnClickListener {
                        val customerProfile = ""
                        val customerName = nameEditText.text.toString().trim()
                        val customerAddress = addressEditText.text.toString().trim()
                        val customerNumber = numberEditText.text.toString().trim()

                        if (customerName.isEmpty() || customerAddress.isEmpty() || customerNumber.isEmpty()) {
                            showToast("Please fill all fields...")
                            return@setOnClickListener
                        }

                        val customer =
                            Customer(
                                customer.id,
                                customerProfile,
                                customerName,
                                customerAddress,
                                customerNumber
                            )
                        myViewModel.updateCustomer(customer)
                        dialog.dismiss()

                    }
                }
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCustomerBinding.inflate(inflater, container, false)
        return binding.getRoot()
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // my code here ----------------------------------------------------------------------------

        // adapters start here ---------------------------------------------------------------------------
        binding.customerRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(ItemSpacingDecorator())
            adapter = myAdapter
        }
        // adapters end here -----------------------------------------------------------------------


        // observers start here --------------------------------------------------------------------------

        myViewModel.getAllCustomers().observe(viewLifecycleOwner) { allCustomers ->
            myAdapter.setAllCustomersList(allCustomers)
            binding.totalCustomersTextView.text = "Tota: ${allCustomers.size}"
        }

        observeResult(
            myViewModel.checkCustomerInsertLiveData,
            "Customer inserted successfully.",
            "Customer insertion failed! Try again..."
        )

        observeResult(
            myViewModel.checkCustomerDeleteLiveData,
            "Customer deleted successfully.",
            "Customer deletion failed! Try again..."
        )

        observeResult(
            myViewModel.checkCustomerUpdateLiveData,
            "Customer updated successfully.",
            "Failed to update! Try again..."
        )

        // observers end here ----------------------------------------------------------------------

        // customer add starts here ----------------------------------------------------------------

        binding.addCustomer.setOnClickListener {
            val customerAddDialView =
                LayoutInflater.from(context).inflate(R.layout.add_customer_dialog, null, false)

            val dialogBuilder = AlertDialog.Builder(context)
            dialogBuilder.setView(customerAddDialView)
            dialogBuilder.setCancelable(false)

            val dialog = dialogBuilder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

            val cancelButton = customerAddDialView.findViewById<Button>(R.id.dialogCancelButton)
            val addButton = customerAddDialView.findViewById<Button>(R.id.dialogAddButton)
            val profileImageView =
                customerAddDialView.findViewById<ImageView?>(R.id.dialogCustomerImage)
            val nameEditText =
                customerAddDialView.findViewById<EditText>(R.id.dialogCustomerNameEditText)
            val addressEditText =
                customerAddDialView.findViewById<EditText>(R.id.dialogCustomerAddressEditText)
            val numberEditText =
                customerAddDialView.findViewById<EditText>(R.id.dialogCustomerNumberEditText)

            cancelButton.setOnClickListener {
                dialog.dismiss()
            }

            addButton.setOnClickListener {
                val customerProfile = ""
                val customerName = nameEditText.text.toString().trim()
                val customerAddress = addressEditText.text.toString().trim()
                val customerNumber = numberEditText.text.toString().trim()

                if (customerName.isEmpty() || customerAddress.isEmpty() || customerNumber.isEmpty()) {
                    showToast("Please fill all fields...")
                    return@setOnClickListener
                }

                val customer =
                    Customer(0, customerProfile, customerName, customerAddress, customerNumber)
                myViewModel.insertCustomer(customer)
                dialog.dismiss()

            }
        }

        // customer add ends here ------------------------------------------------------------------


        // search filter starts here ---------------------------------------------------------------

        binding.searchEditText.doAfterTextChanged { text ->
            searchJob?.cancel()
            searchJob = viewLifecycleOwner.lifecycleScope.launch {
                delay(100)
                myViewModel.filterCustomer(text?.trim().toString())
            }
        }

        // search filter ends here ---------------------------------------------------------------


    } // on create end here ------------------------------------------------------------------------

    fun showToast(text: String) {
        toast?.cancel()
        toast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
        toast?.show()
    }

    private fun observeResult(
        liveData: MutableLiveData<Boolean?>,
        successMessage: String,
        failedMessage: String
    ) {
        liveData.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess == null) return@observe
            if (isSuccess) {
                showToast(successMessage)
            } else {
                showToast(failedMessage)
            }
            liveData.value = null
        }
    }
}