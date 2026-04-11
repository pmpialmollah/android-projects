package com.nsoft.mybakery.fragments

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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nsoft.mybakery.R
import com.nsoft.mybakery.adapterdecorators.ProductItemSpacingDecorator
import com.nsoft.mybakery.adapters.ProductRecyclerViewAdapter
import com.nsoft.mybakery.databinding.FragmentProductsBinding
import com.nsoft.mybakery.models.Product
import com.nsoft.mybakery.viewmodels.MyViewModel

class ProductsFragment : Fragment() {
    private lateinit var binding: FragmentProductsBinding
    private lateinit var myAdapter: ProductRecyclerViewAdapter
    private lateinit var myViewModel: MyViewModel
    private var toast: Toast? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // my code here ----------------------------------------------------------------------------

        myViewModel = ViewModelProvider(this@ProductsFragment).get(MyViewModel::class.java)

        myAdapter = ProductRecyclerViewAdapter(
            object : ProductRecyclerViewAdapter.ClickListener {
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

            }
        )

        // product delete confirmation -------------------------------------------------------------
        myViewModel.checkDeleteLiveData.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess == null) return@observe

            if (isSuccess) {
                showToast("Deleted successfully...")
                myViewModel.checkDeleteLiveData.value = null
            } else {
                showToast("Deleted failed! Try again...")
                myViewModel.checkDeleteLiveData.value = null
            }
        }
        // -----------------------------------------------------------------------------------------

        binding.productRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.productRecyclerView.adapter = myAdapter
        binding.productRecyclerView.addItemDecoration(ProductItemSpacingDecorator())

        myViewModel.getAllProducts().observe(viewLifecycleOwner) { productList ->
            myAdapter.setProductList(productList)
        }

        binding.addProductButton.setOnClickListener {
            val insertProductView =
                LayoutInflater.from(context).inflate(R.layout.add_product_dialog, null, false)

            val dialogBuilder = AlertDialog.Builder(context).apply {
                setView(insertProductView)
                setCancelable(false)
            }

            val alertDialog = dialogBuilder.create()
            alertDialog.show()

            alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val cancelButton =
                insertProductView.findViewById<AppCompatButton>(R.id.dialogCancelButton)
            val insertButton = insertProductView.findViewById<AppCompatButton>(R.id.dialogAddButton)
            val imageLayout =
                insertProductView.findViewById<RelativeLayout>(R.id.dialogProductImageLayout)
            val productNameEditText =
                insertProductView.findViewById<EditText>(R.id.dialogProductNameEditText)
            val productPriceEditText =
                insertProductView.findViewById<EditText>(R.id.dialogProductPriceEditText)


            myViewModel.checkInsert().observe(viewLifecycleOwner) { isSuccess ->
                if (isSuccess == null) return@observe

                if (isSuccess) {
                    showToast("Inserted successfully.")
                    myViewModel.checkInsertLiveData.value = null
                } else {
                    showToast("Insertion failed! Try again...")
                    myViewModel.checkInsertLiveData.value = null
                }
            }

            cancelButton.setOnClickListener { alertDialog.dismiss() }
            insertButton.setOnClickListener {

                val productName = productNameEditText.text.toString().trim()
                val productPrice = productPriceEditText.text.toString().trim()

                if (productName.isEmpty() || productPrice.isEmpty()) {
                    showToast("Please fill all field!")
                    return@setOnClickListener
                }

                val product = Product(0, "Null", productName, productPrice)
                myViewModel.insertProduct(product)

                alertDialog?.dismiss()
            }

            imageLayout.setOnClickListener {
                showToast("Working...")
            }

        }

        // -----------------------------------------------------------------------------------------
    }

    private fun showToast(text: String) {
        toast?.cancel()
        toast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
        toast?.show()
    }

    fun hideKeyboard(view: View) {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}
