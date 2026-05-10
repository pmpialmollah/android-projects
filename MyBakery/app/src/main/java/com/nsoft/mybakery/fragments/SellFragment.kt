package com.nsoft.mybakery.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nsoft.mybakery.R
import com.nsoft.mybakery.adapterdecorators.SellProductRecyclerViewSpacingDecorator
import com.nsoft.mybakery.adapters.CustomerDialogAdapter
import com.nsoft.mybakery.adapters.SelectProductAdapter
import com.nsoft.mybakery.adapters.SellProductRecyclerViewAdapter
import com.nsoft.mybakery.databinding.FragmentSellProductBinding
import com.nsoft.mybakery.models.Customer
import com.nsoft.mybakery.models.Product
import com.nsoft.mybakery.models.SellTransaction
import com.nsoft.mybakery.models.Seller
import com.nsoft.mybakery.viewmodels.MyViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.core.graphics.drawable.toDrawable


class SellFragment : Fragment() {
    private lateinit var binding: FragmentSellProductBinding
    private val myViewModel: MyViewModel by viewModels()
    private lateinit var alertDialogView: View
    private lateinit var alertDialogBuilder: AlertDialog.Builder
    private lateinit var alertDialog: AlertDialog
    private var toast: Toast? = null
    private var searchJob: Job? = null
    private var selectedCustomer: Customer? = null
    private var selectedProducts: ArrayList<HashMap<Product, Double>> =
        ArrayList<HashMap<Product, Double>>()
    private var totalPrice: Double = 0.0
    private var totalPaid: Double = 0.0
    private var totalDue: Double = 0.0

    private val customerDialogAdapter by lazy {
        CustomerDialogAdapter(object : CustomerDialogAdapter.ClickListener {
            override fun onClick(customer: Customer) {
                selectedCustomer = customer
                binding.selectCustomer.text = customer.name
                binding.selectCustomer.typeface = Typeface.DEFAULT_BOLD
                alertDialog.dismiss()
            }

        })
    }
    private val selectProductAdapter by lazy {
        SelectProductAdapter()
    }
    private lateinit var sellProductRecyclerviewAdapter: SellProductRecyclerViewAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSellProductBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("InflateParams", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // my code starts here ---------------------------------------------------------------------


        // Observers start here --------------------------------------

        myViewModel.getAllCustomers().observe(viewLifecycleOwner) { allCustomers ->
            customerDialogAdapter.setCustomersList(allCustomers)
        }

        myViewModel.getAllProducts().observe(viewLifecycleOwner) { allProducts ->
            selectProductAdapter.setProductList(allProducts)
        }

        // observers end here ------------------------------

        // adapters starts here -------------
        sellProductRecyclerviewAdapter = SellProductRecyclerViewAdapter()
        binding.sellProductRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.sellProductRecyclerView.addItemDecoration(SellProductRecyclerViewSpacingDecorator())
        binding.sellProductRecyclerView.adapter = sellProductRecyclerviewAdapter

        sellProductRecyclerviewAdapter.setOnListChangedListener {
            updateCalculations()
        }

        // adapters ends here ------------------

        updateCalculations()

        binding.paidEditText.doAfterTextChanged {
            updateCalculations()
        }

        // click listener starts here ------------

        binding.selectCustomer.setOnClickListener {
            alertDialogView =
                LayoutInflater.from(requireContext())
                    .inflate(R.layout.customer_list_dialog, null, false)

            alertDialogBuilder = AlertDialog.Builder(requireContext()).apply {
                setView(alertDialogView)
                setCancelable(true)
            }

            alertDialog = alertDialogBuilder.create()
            alertDialog.show()

            alertDialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())

            val searchEditText =
                alertDialog.findViewById<EditText>(R.id.dialogCustomerSearchEditText)
            val recyclerView =
                alertDialog.findViewById<RecyclerView>(R.id.dialogCustomerRecyclerView)

            recyclerView.apply {
                this?.layoutManager = LinearLayoutManager(context)
                this?.adapter = customerDialogAdapter
            }
            // Initial data
            myViewModel.getAllCustomers().value?.let {
                customerDialogAdapter.setCustomersList(it)
            }

            // search logic here
            searchEditText?.doAfterTextChanged { text ->
                searchJob?.cancel()
                searchJob = viewLifecycleOwner.lifecycleScope.launch {
                    delay(100)
                    myViewModel.filterCustomer(text?.trim().toString())
                }
            }

        }

        binding.addProduct.setOnClickListener {
            val selectedHashMap: HashMap<Product, Double> = HashMap<Product, Double>()

            val dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.select_product_dialog, null)
            val dialogBuilder = AlertDialog.Builder(requireContext()).apply {
                setView(dialogView)
                setCancelable(true)
            }

            val dialog = dialogBuilder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())

            val searchEditText = dialogView.findViewById<EditText>(R.id.dialogProductSearchEditText)
            val frameLayout = dialogView.findViewById<FrameLayout>(R.id.selectedProductFrameLayout)
            val recyclerView = dialogView.findViewById<RecyclerView>(R.id.selectProductRecyclerView)
            val quantityPlus = dialogView.findViewById<ImageView>(R.id.selectProductQuantityPlus)
            val quantityMinus = dialogView.findViewById<ImageView>(R.id.selectProductQuantityMinus)
            val quantity = dialogView.findViewById<TextView>(R.id.selectProductQuantity)
            val cancelButton = dialogView.findViewById<Button>(R.id.dialogCancelButton)
            val addButton = dialogView.findViewById<Button>(R.id.dialogAddButton)
            val quantityContainer =
                dialogView.findViewById<LinearLayout>(R.id.selectProductQuantityContainer)
            val totalPriceTextView = dialogView.findViewById<TextView>(R.id.selectProductTotalPrice)

            cancelButton.setOnClickListener {
                dialog.dismiss()
            }

            var selectedProduct: Product? = null
            var selectedProductPrice: Double = 0.0

            recyclerView.visibility = View.VISIBLE
            quantityContainer.visibility = View.GONE
            totalPriceTextView.visibility = View.GONE

            selectProductAdapter.setOnItemClickListener(object :
                SelectProductAdapter.ClickListener {
                @SuppressLint("SetTextI18n")
                override fun onClick(product: Product) {
                    selectedProduct = product
                    recyclerView.visibility = View.GONE
                    val selectedProductView =
                        layoutInflater.inflate(R.layout.product_add_item, frameLayout, false)
                    frameLayout.removeAllViews()
                    frameLayout.addView(selectedProductView)

                    quantityContainer.visibility = View.VISIBLE
                    totalPriceTextView.visibility = View.VISIBLE

                    selectedProductView.findViewById<TextView>(R.id.productAddItemName).text =
                        product.name
                    selectedProductView.findViewById<TextView>(R.id.productAddItemPrice).text =
                        "${getString(R.string.bdt)}${product.price}"

                    // Set initial quantity
                    if (product.quantity == 0.0) product.quantity = 1.0
                    quantity.text = product.quantity.toString()

                    selectedProductPrice = selectedProduct.price

                    totalPriceTextView.text = "Total: $selectedProductPrice"
                }
            })

            recyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = selectProductAdapter
            }
            // Initial data
            myViewModel.getAllProducts().value?.let {
                selectProductAdapter.setProductList(it)
            }

            // search logic here
            searchEditText.doAfterTextChanged { text ->
                searchJob?.cancel()
                searchJob = viewLifecycleOwner.lifecycleScope.launch {
                    delay(100)
                    myViewModel.filterProducts(text?.trim().toString())
                }
            }

            // quantity handle here -----------
            quantityPlus.setOnClickListener {
                selectedProduct?.let {
                    it.quantity++
                    selectedProductPrice += it.price
                    quantity.text = it.quantity.toString()
                    totalPriceTextView.text = "Total: $selectedProductPrice"
                } ?: showToast("Please select a product first")
            }

            quantityMinus.setOnClickListener {
                selectedProduct?.let {
                    if (it.quantity > 1) {
                        it.quantity--
                        quantity.text = it.quantity.toString()
                        selectedProductPrice -= it.price
                        totalPriceTextView.text = "Total: $selectedProductPrice"
                    }
                } ?: showToast("Please select a product first")
            }

            addButton.setOnClickListener {
                selectedProduct?.let { product ->

                    selectedHashMap[product] = product.quantity

                    selectedProducts.add(selectedHashMap)

                    sellProductRecyclerviewAdapter.addProduct(product)
                    dialog.dismiss()
                } ?: showToast("Please select a product first")
            }

        }

        binding.sellProduct.setOnClickListener {

            if (selectedCustomer == null) return@setOnClickListener
            if (selectedProducts.isNullOrEmpty()) return@setOnClickListener

            totalPrice = sellProductRecyclerviewAdapter.getTotalPrice()
            val paidText = binding.paidEditText.text.toString().trim()

            totalPaid = if (paidText.isNotEmpty()) paidText.toDouble() else 0.0

            totalDue = totalPrice - totalPaid

            val seller = Seller(0, "", "Shawon", "Belabor", "01303889828")

            val sellTransaction =
                SellTransaction(
                    0,
                    selectedCustomer,
                    selectedProducts,
                    totalPrice,
                    totalPaid,
                    totalDue,
                    seller,
                    ""
                )

            myViewModel.insertTransaction(sellTransaction)

            showToast("Data inserted!")
        }

        // click listener ends here ------------


        // my code ends here ---------------------------------------------------------------------
    }

    @SuppressLint("SetTextI18n")
    private fun updateCalculations() {
        val total = sellProductRecyclerviewAdapter.getTotalPrice()
        binding.totalPrice.text = "Total: $total"

        val paidText = binding.paidEditText.text.toString()
        var paidAmount = if (paidText.isNotEmpty()) paidText.toDouble() else 0.0

        if (paidAmount > total) {
            paidAmount = total
            binding.paidEditText.setText(total.toString())
            binding.paidEditText.setSelection(binding.paidEditText.text.length)
            showToast("Paid amount cannot exceed total price")
        }

        val due = total - paidAmount
        binding.totalDue.text = "due: $due"

    }

    // toast
    private fun showToast(text: String) {
        toast?.cancel()
        toast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
        toast?.show()
    }


}