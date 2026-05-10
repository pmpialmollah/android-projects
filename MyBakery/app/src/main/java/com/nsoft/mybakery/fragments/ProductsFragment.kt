package com.nsoft.mybakery.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.nsoft.mybakery.R
import com.nsoft.mybakery.adapterdecorators.ItemDividerDecoration
import com.nsoft.mybakery.adapterdecorators.ItemSpacingDecorator
import com.nsoft.mybakery.adapters.ProductRecyclerViewAdapter
import com.nsoft.mybakery.databinding.FragmentProductsBinding
import com.nsoft.mybakery.models.Product
import com.nsoft.mybakery.viewmodels.MyViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.core.graphics.drawable.toDrawable
import androidx.core.graphics.toColorInt

class ProductsFragment : Fragment() {
    private lateinit var binding: FragmentProductsBinding
    private val myViewModel: MyViewModel by viewModels()
    private var toast: Toast? = null
    private var searchJob: Job? = null
    private val myAdapter by lazy {
        ProductRecyclerViewAdapter(object : ProductRecyclerViewAdapter.ClickListener {
            override fun onDeleteClickListener(product: Product) {
                AlertDialog.Builder(context)
                    .setTitle("Confirm delete!")
                    .setMessage("Product details:\nName: ${product.name}\nPrice: ${product.price}\n\nDo you really want to delete this product?")
                    .setPositiveButton("Yes") { dialog, _ ->
                        myViewModel.deleteProductById(product.id)
                        dialog.dismiss()
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }

            @SuppressLint("SetTextI18n")
            override fun onItemLongClick(product: Product) {
                val updateProductView =
                    LayoutInflater.from(context)
                        .inflate(R.layout.add_product_dialog, null, false)

                val dialogBuilder = AlertDialog.Builder(context).apply {
                    setView(updateProductView)
                    setCancelable(false)
                }

                val alertDialog = dialogBuilder.create()
                alertDialog.show()

                alertDialog?.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())

                val cancelButton =
                    updateProductView.findViewById<AppCompatButton>(R.id.dialogCancelButton)
                val updateButton =
                    updateProductView.findViewById<AppCompatButton>(R.id.dialogAddButton)
                val imageLayout =
                    updateProductView.findViewById<RelativeLayout>(R.id.dialogProductImageLayout)
                val productNameEditText =
                    updateProductView.findViewById<EditText>(R.id.dialogProductNameEditText)
                val productPriceEditText =
                    updateProductView.findViewById<EditText>(R.id.dialogProductPriceEditText)


                updateButton.text = "Update"
                productNameEditText.setText(product.name)
                productPriceEditText.setText("${product.price}")

                cancelButton.setOnClickListener { alertDialog.dismiss() }
                updateButton.setOnClickListener {

                    val productName = productNameEditText.text.toString().trim()
                    val productPrice = productPriceEditText.text.toString().trim()

                    if (productName.isEmpty() || productPrice.isEmpty()) {
                        showToast("Please fill all field!")
                        return@setOnClickListener
                    }

                    val updatedProduct =
                        Product(
                            product.id,
                            product.image,
                            productName,
                            0.0,
                            productPrice.toDouble()
                        )
                    myViewModel.updateProduct(updatedProduct)

                    alertDialog?.dismiss()
                }

                imageLayout.setOnClickListener {
                    showToast("Working...")
                }
            }

        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.getRoot()
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // my code here ----------------------------------------------------------------------------


        // adapters start here -------------------------

        binding.productRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = myAdapter
            addItemDecoration(ItemSpacingDecorator())
            addItemDecoration(
                ItemDividerDecoration(
                    color = "#A9967B".toColorInt(),
                    heightPx = 1,
                    marginStartPx = 50
                )
            )
        }

        // adapters end here ----------------------------

        // all observers start here --------------------

        myViewModel.getAllProducts().observe(viewLifecycleOwner) { productList ->
            myAdapter.setProductList(productList)
            binding.totalProductsTextView.text = "Total: ${productList.size}"
        }

        myViewModel.checkUpdateLiveData.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess == null) return@observe

            if (isSuccess) {
                showToast("Updated successfully.")
            } else {
                showToast("Update failed! Try again...")
            }

            myViewModel.checkUpdateLiveData.value = null
        }


        myViewModel.checkDeleteLiveData.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess == null) return@observe

            if (isSuccess) {
                showToast("Deleted successfully...")
            } else {
                showToast("Deleted failed! Try again...")
            }
            myViewModel.checkDeleteLiveData.value = null
        }

        myViewModel.checkInsertLiveData.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess == null) return@observe
            if (isSuccess) {
                showToast("Inserted successfully.")
                myViewModel.checkInsertLiveData.value = null
            } else {
                showToast("Insertion failed! Try again...")
                myViewModel.checkInsertLiveData.value = null
            }
        }


        // all observers end here --------------------

        binding.addProductButton.setOnClickListener {
            val insertProductView =
                LayoutInflater.from(context).inflate(R.layout.add_product_dialog, null, false)

            val dialogBuilder = AlertDialog.Builder(context).apply {
                setView(insertProductView)
                setCancelable(false)
            }

            val alertDialog = dialogBuilder.create()
            alertDialog.show()

            alertDialog?.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())

            val cancelButton =
                insertProductView.findViewById<AppCompatButton>(R.id.dialogCancelButton)
            val insertButton =
                insertProductView.findViewById<AppCompatButton>(R.id.dialogAddButton)
            val imageLayout =
                insertProductView.findViewById<RelativeLayout>(R.id.dialogProductImageLayout)
            val productNameEditText =
                insertProductView.findViewById<EditText>(R.id.dialogProductNameEditText)
            val productPriceEditText =
                insertProductView.findViewById<EditText>(R.id.dialogProductPriceEditText)


            cancelButton.setOnClickListener { alertDialog.dismiss() }
            insertButton.setOnClickListener {

                val productName = productNameEditText.text.toString().trim()
                val productPrice = productPriceEditText.text.toString().trim()

                if (productName.isEmpty() || productPrice.isEmpty()) {
                    showToast("Please fill all field!")
                    return@setOnClickListener
                }

                val product = Product(0, "Null", productName, 0.0, productPrice.toDouble())
                myViewModel.insertProduct(product)

                alertDialog?.dismiss()
            }

            imageLayout.setOnClickListener {
                showToast("Image is selected")
            }

        }

        // search logic here
        binding.searchEditText.doAfterTextChanged { text ->
            searchJob?.cancel()
            searchJob = viewLifecycleOwner.lifecycleScope.launch {
                delay(100)
                myViewModel.filterProducts(text?.trim().toString())
            }
        }


        // -----------------------------------------------------------------------------------------
    }


    fun showToast(text: String) {
        toast?.cancel()
        toast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
        toast?.show()
    }


}

