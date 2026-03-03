package com.nsoft.mybakery;

import android.app.AlertDialog;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nsoft.mybakery.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private DashboardRecyclerviewAdapter dashboardAdapter;
    private DBHelper dbHelper;
    private boolean isKeyboardVisible = false;
    private ViewTreeObserver.OnGlobalLayoutListener keyboardLayoutListener;
    private List<Customer> customerList;
    private CustomerDialogAdapter dialogAdapter;
    private DialogProductRecyclerviewAdapter dialogProductAdapter;
    private List<ProductSell> productSellList;
    private List<Product> productList;
    private ProductSell productSell;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // my code here ----------------------------------------------------------------------------

        dbHelper = new DBHelper(getContext());
        customerList = new ArrayList<>();
        productList = new ArrayList<>();
        productSellList = new ArrayList<>();


        dashboardAdapter = new DashboardRecyclerviewAdapter(productSellList);
        binding.dashboardRecyclerView.setLayoutManager(new LinearLayoutManager(
                getContext(),
                LinearLayoutManager.HORIZONTAL,
                false));
        binding.dashboardRecyclerView.setAdapter(dashboardAdapter);


        binding.customerEditText.setOnClickListener(v -> {
            View customerListView = LayoutInflater.from(getContext()).inflate(R.layout.customer_list_dialog, null, false);

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

            builder.setView(customerListView);
//            builder.setCancelable(false);

            AlertDialog dialog = builder.create();
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.show();

            EditText dialogSearchEditText = customerListView.findViewById(R.id.dialogSearchEditText);
            RecyclerView dialogCustomerRecyclerView = customerListView.findViewById(R.id.dialogCustomerRecyclerView);

            fetchCustomers();

            dialogAdapter = new CustomerDialogAdapter(getContext(), customerList, customer -> {
                binding.customerEditText.setText(customer.getName());
                dialog.dismiss();
            });

            dialogCustomerRecyclerView.setAdapter(dialogAdapter);
            dialogCustomerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            dialogSearchEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.toString().isEmpty()) {
                        fetchCustomers();
                    } else {
                        getCustomerByName(s.toString());
                    }
                }
            });

        });

        binding.addItemButton.setOnClickListener(v -> {
            View dashboardAddProductDialogView = LayoutInflater.from(getContext()).inflate(R.layout.dashboard_add_product_dialog, null, false);
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setView(dashboardAddProductDialogView);
            builder.setCancelable(false);

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            RecyclerView dialogProductRecyclerView = dashboardAddProductDialogView.findViewById(R.id.dialogProductRecyclerView);
            Button dialogCancelButton = dashboardAddProductDialogView.findViewById(R.id.dialogCancelButton);
            Button dialogAddButton = dashboardAddProductDialogView.findViewById(R.id.dialogAddButton);
            EditText dialogProductSearchEditText = dashboardAddProductDialogView.findViewById(R.id.dialogProductSearchEditText);

            dialogProductSearchEditText.addTextChangedListener(new TextWatcher() {
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


            dialogProductAdapter = new DialogProductRecyclerviewAdapter(productList, (holder, product) -> {
                holder.plusImageView.setOnClickListener(v1 -> {
                    int count = Integer.parseInt(holder.itemCountTextView.getText().toString());
                    holder.itemCountTextView.setText("" + (count + 1));

                    int totalCount = Integer.parseInt(holder.itemCountTextView.getText().toString());

                    productSell = new ProductSell(totalCount, product.getName(), String.valueOf(Integer.parseInt(product.getPrice()) * totalCount));
                });

                holder.minusImageView.setOnClickListener(v1 -> {
                    int count = Integer.parseInt(holder.itemCountTextView.getText().toString());
                    if (count > 0) {
                        holder.itemCountTextView.setText("" + (count - 1));
                    }
                });
            });

            getAllProducts();

            dialogProductRecyclerView.setAdapter(dialogProductAdapter);
            dialogProductRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            dialogCancelButton.setOnClickListener(v1 -> {
                alertDialog.dismiss();
            });

            dialogAddButton.setOnClickListener(v1 -> {
                alertDialog.dismiss();
                productSellList.add(productSell);
                dashboardAdapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "Item added...", Toast.LENGTH_SHORT).show();
            });

        });

        binding.sellButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Everything is working fine...", Toast.LENGTH_SHORT).show();
        });

        keyboardLayoutListener = () -> {
            if (binding == null) {
                return;
            }
            Rect rect = new Rect();
            binding.getRoot().getWindowVisibleDisplayFrame(rect);

            int screenHeight = binding.getRoot().getRootView().getHeight();
            int keypadHeight = screenHeight - rect.bottom;

            if (keypadHeight > screenHeight * 0.15) {
                // Keyboard OPEN
                if (!isKeyboardVisible) {
                    isKeyboardVisible = true;
                    onKeyboardShown(keypadHeight);
                }
            } else {
                // Keyboard CLOSED
                if (isKeyboardVisible) {
                    isKeyboardVisible = false;
                    onKeyboardHidden();
                }
            }
        };

        binding.getRoot().getViewTreeObserver().addOnGlobalLayoutListener(keyboardLayoutListener);


        // -----------------------------------------------------------------------------------------
    }

    private void updateUI() {
        binding.totalCustomersTextView.setText("" + dbHelper.getCustomerCount());
    }


    @Override
    public void onResume() {
        super.onResume();

        updateUI();
    }

    @Override
    public void onDestroyView() {
        if (binding != null && keyboardLayoutListener != null) {
            binding.getRoot().getViewTreeObserver().removeOnGlobalLayoutListener(keyboardLayoutListener);
        }
        super.onDestroyView();
        binding = null;
    }


    private void onKeyboardShown(int height) {
        if (binding != null) {
            binding.topSectionGroup.setVisibility(View.GONE);
        }
    }

    private void onKeyboardHidden() {
        if (binding != null) {
            binding.topSectionGroup.setVisibility(View.VISIBLE);
        }
    }

    private void fetchCustomers() {
        Cursor cursor = dbHelper.getAllCustomers();
        customerList.clear();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String profile = cursor.getString(1);
                String name = cursor.getString(2);
                String address = cursor.getString(3);
                String number = cursor.getString(4);

                customerList.add(new Customer(id, profile, name, address, number));
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        dialogAdapter.notifyDataSetChanged();
        if (dialogAdapter != null) {
            dialogAdapter.notifyDataSetChanged();
        }
    }

    private void getCustomerByName(String name) {
        Cursor cursor = dbHelper.getCustomerByName(name);
        customerList.clear();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String customerProfile = cursor.getString(1);
                String customerName = cursor.getString(2);
                String customerAddress = cursor.getString(3);
                String customerNumber = cursor.getString(4);

                customerList.add(new Customer(id, customerProfile, customerName, customerAddress, customerNumber));
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        dialogAdapter.notifyDataSetChanged();
        if (dialogAdapter != null) {
            dialogAdapter.notifyDataSetChanged();
        }
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
            dialogProductAdapter.notifyDataSetChanged();
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
            dialogProductAdapter.notifyDataSetChanged();
        } else {
            if (cursor != null) {
                cursor.close();
            }
            Toast.makeText(getContext(), "No product found!", Toast.LENGTH_SHORT).show();
        }
    }


}
