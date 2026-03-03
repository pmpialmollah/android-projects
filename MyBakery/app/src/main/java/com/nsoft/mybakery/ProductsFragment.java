package com.nsoft.mybakery;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.nsoft.mybakery.databinding.FragmentProductsBinding;

import java.util.ArrayList;
import java.util.List;

public class ProductsFragment extends Fragment {
    private FragmentProductsBinding binding;
    private ProductRecyclerViewAdapter myAdapter;
    private DBHelper dbHelper;
    private List<Product> productList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProductsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // my code here ----------------------------------------------------------------------------
        productList = new ArrayList<>();
        dbHelper = new DBHelper(getContext());
        myAdapter = new ProductRecyclerViewAdapter(productList, new ProductRecyclerViewAdapter.ClickListener() {
            @Override
            public void onDeleteListener(int id, int position) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Confirm delete")
                        .setMessage("Do you really want to delete this item?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            dbHelper.deleteProductById(id);
                            getAllProducts();

                        })
                        .setNegativeButton("No", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
            }
        }, new ProductRecyclerViewAdapter.LongClickListener() {
            @Override
            public void onLongClickListener(Product product) {
                View updateDialogView = LayoutInflater.from(getContext()).inflate(R.layout.update_product_dialog, null, false);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                dialogBuilder.setView(updateDialogView);
                dialogBuilder.setCancelable(false);

                AlertDialog updateDialog = dialogBuilder.create();
                updateDialog.show();
                updateDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                ImageView productImageView = updateDialogView.findViewById(R.id.updateDialogProductImage);
                EditText productNameEditText = updateDialogView.findViewById(R.id.updateDialogProductNameEditText);
                EditText productPriceEditText = updateDialogView.findViewById(R.id.updateDialogProductPriceEditText);
                Button updateButton = updateDialogView.findViewById(R.id.updateDialogUpdateButton);
                Button cancelButton = updateDialogView.findViewById(R.id.updateDialogCancelButton);


                productNameEditText.setText(product.getName());
                productPriceEditText.setText(product.getPrice());

                updateButton.setOnClickListener(v -> {

                    String updatedImage = "";
                    String updatedName = productNameEditText.getText().toString().trim();
                    String updatedPrice = productPriceEditText.getText().toString().trim();

                    dbHelper.updateProduct(updatedImage, updatedName, updatedPrice, product.getId());

                    updateDialog.dismiss();
                    getAllProducts();
                });

                cancelButton.setOnClickListener(v -> {
                    updateDialog.dismiss();
                });


            }
        });

        getAllProducts();

        binding.productRecyclerView.addItemDecoration(new ProductItemSpacingDecorator());
        binding.productRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.productRecyclerView.setAdapter(myAdapter);


        binding.addProductButton.setOnClickListener(v -> {
            View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.add_product_dialog, null, false);
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
            dialogBuilder.setView(dialogView);
            dialogBuilder.setCancelable(false);

            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
            alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            dialogView.findViewById(R.id.dialogCancelButton).setOnClickListener(v1 -> {
                alertDialog.dismiss();
            });

            dialogView.findViewById(R.id.dialogAddButton).setOnClickListener(v1 -> {
                EditText dialogProductNameEditText = dialogView.findViewById(R.id.dialogProductNameEditText);
                EditText dialogProductPriceEditText = dialogView.findViewById(R.id.dialogProductPriceEditText);

                String productName = dialogProductNameEditText.getText().toString().trim();
                String productPrice = dialogProductPriceEditText.getText().toString();

                if (productName.isEmpty() || productPrice.isEmpty()) {
                    Toast.makeText(getContext(), "Please fill all boxes.", Toast.LENGTH_SHORT).show();
                } else {
                    dbHelper.addProduct("profile", productName, productPrice);
                    getAllProducts();
                    alertDialog.dismiss();
                }
            });

        });

        binding.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getProductByName(s.toString());
            }
        });

        // -----------------------------------------------------------------------------------------
    }

    private void getAllProducts() {

        Cursor allProductsCursor = dbHelper.getAllProducts();
        if (allProductsCursor != null && allProductsCursor.getCount() > 0) {
            productList.clear();
            while (allProductsCursor.moveToNext()) {
                int id = allProductsCursor.getInt(0);
                String image = allProductsCursor.getString(1);
                String name = allProductsCursor.getString(2);
                String price = allProductsCursor.getString(3);

                productList.add(new Product(id, image, name, price));
            }
            allProductsCursor.close();
            myAdapter.notifyDataSetChanged();
        } else {
            if (allProductsCursor != null) {
                allProductsCursor.close();
            }
            Toast.makeText(getContext(), "No product found!", Toast.LENGTH_SHORT).show();
        }
    }

    private void getProductByName(String name) {
        Cursor cursor = dbHelper.getProductByName(name);
        if (cursor != null && cursor.getCount() > 0) {
            productList.clear();
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String productImage = cursor.getString(1);
                String productName = cursor.getString(2);
                String productPrice = cursor.getString(3);

                productList.add(new Product(id, productImage, productName, productPrice));
            }
            myAdapter.notifyDataSetChanged();
        } else {
            if (cursor != null) {
                cursor.close();
            }
            Toast.makeText(getContext(), "No product found!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        getAllProducts();

    }
}
