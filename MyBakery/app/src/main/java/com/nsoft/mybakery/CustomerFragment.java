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

import com.nsoft.mybakery.databinding.FragmentCustomerBinding;

import java.util.ArrayList;
import java.util.List;

public class CustomerFragment extends Fragment {
    private FragmentCustomerBinding binding;
    private CustomerRecyclerViewAdapter myAdapter;
    private DBHelper dbHelper;
    private List<Customer> customerList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCustomerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // my code here ----------------------------------------------------------------------------
        dbHelper = new DBHelper(getContext());
        customerList = new ArrayList<>();
        myAdapter = new CustomerRecyclerViewAdapter(getContext(), customerList, new CustomerRecyclerViewAdapter.ClickListener() {
            @Override
            public void onClick(int id) {

                new AlertDialog.Builder(getContext())
                        .setTitle("Confirm delete")
                        .setMessage("Do you really want to delete this customer?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            dbHelper.deleteCustomerById(id);
                            fetchCustomers();
                        })
                        .setNegativeButton("No", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
            }
        }, new CustomerRecyclerViewAdapter.LongClickListener() {
            @Override
            public void onLongClick(Customer customer) {
                View updateCustomerDialogView = LayoutInflater.from(getContext()).inflate(R.layout.update_customer_dialog, null, false);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(updateCustomerDialogView);
                builder.setCancelable(false);

                AlertDialog dialog = builder.create();
                dialog.show();

                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                ImageView updateDialogCustomerImage = updateCustomerDialogView.findViewById(R.id.updateDialogCustomerImage);
                EditText updateDialogCustomerNameEditText = updateCustomerDialogView.findViewById(R.id.updateDialogCustomerNameEditText);
                EditText updateDialogCustomerAddressEditText = updateCustomerDialogView.findViewById(R.id.updateDialogCustomerAddressEditText);
                EditText updateDialogCustomerNumberEditText = updateCustomerDialogView.findViewById(R.id.updateDialogCustomerNumberEditText);
                Button updateDialogAddButton = updateCustomerDialogView.findViewById(R.id.updateDialogAddButton);
                Button updateDialogCancelButton = updateCustomerDialogView.findViewById(R.id.updateDialogCancelButton);

                updateDialogCustomerNameEditText.setText(customer.getName());
                updateDialogCustomerAddressEditText.setText(customer.getAddress());
                updateDialogCustomerNumberEditText.setText("" + customer.getNumber());

                updateDialogCancelButton.setOnClickListener(v -> {
                    dialog.dismiss();
                });

                updateDialogAddButton.setOnClickListener(v -> {

                    String updatedName = updateDialogCustomerNameEditText.getText().toString().trim();
                    String updatedAddress = updateDialogCustomerAddressEditText.getText().toString().trim();
                    String updatedNumber = updateDialogCustomerNumberEditText.getText().toString().trim();

                    if (updatedName.isEmpty() && updatedAddress.isEmpty() && updatedNumber.isEmpty()) {
                        Toast.makeText(getContext(), "Please fill all fields...", Toast.LENGTH_SHORT).show();
                    } else {
                        dbHelper.updateCustomer("profile", updatedName, updatedAddress, updatedNumber, customer.getId());
                        dialog.dismiss();
                        fetchCustomers();
                    }
                });

            }
        });

        fetchCustomers();

        binding.customerRecyclerView.addItemDecoration(new ProductItemSpacingDecorator());
        binding.customerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.customerRecyclerView.setAdapter(myAdapter);

        binding.addCustomer.setOnClickListener(v -> {
            View customerAddDialView = LayoutInflater.from(getContext()).inflate(R.layout.add_customer_dialog, null, false);

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
            dialogBuilder.setView(customerAddDialView);
            dialogBuilder.setCancelable(false);

            AlertDialog dialog = dialogBuilder.create();
            dialog.show();

            Button cancelButton = customerAddDialView.findViewById(R.id.dialogCancelButton);
            Button addButton = customerAddDialView.findViewById(R.id.dialogAddButton);
            ImageView profileImageView = customerAddDialView.findViewById(R.id.dialogCustomerImage);
            EditText nameEditText = customerAddDialView.findViewById(R.id.dialogCustomerNameEditText);
            EditText addressEditText = customerAddDialView.findViewById(R.id.dialogCustomerAddressEditText);
            EditText numberEditText = customerAddDialView.findViewById(R.id.dialogCustomerNumberEditText);

            cancelButton.setOnClickListener(v1 -> {
                dialog.dismiss();
            });

            addButton.setOnClickListener(v1 -> {
                String customerProfile = "";
                String customerName = nameEditText.getText().toString().trim();
                String customerAddress = addressEditText.getText().toString().trim();
                String customerNumber = numberEditText.getText().toString().trim();

                if (customerName.isEmpty() && customerAddress.isEmpty() && customerNumber.isEmpty()) {
                    Toast.makeText(getContext(), "Please fill all fields...", Toast.LENGTH_SHORT).show();
                } else {
                    dbHelper.addCustomer(customerProfile, customerName, customerAddress, customerNumber);
                    dialog.dismiss();
                    fetchCustomers();
                }

            });

            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
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
                if (s.toString().isEmpty()) {
                    fetchCustomers();
                } else {
                    getCustomerByName(s.toString());
                }
            }
        });

        // -----------------------------------------------------------------------------------------
    }


    private void fetchCustomers() {
        Cursor cursor = dbHelper.getAllCustomers();
        if (cursor != null && cursor.getCount() > 0) {
            customerList.clear();
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String profile = cursor.getString(1);
                String name = cursor.getString(2);
                String address = cursor.getString(3);
                String number = cursor.getString(4);

                customerList.add(new Customer(id, profile, name, address, number));
            }
            myAdapter.notifyDataSetChanged();
        }
    }

    private void getCustomerByName(String name) {
        Cursor cursor = dbHelper.getCustomerByName(name);
        if (cursor != null && cursor.getCount() > 0) {
            customerList.clear();
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String customerProfile = cursor.getString(1);
                String customerName = cursor.getString(2);
                String customerAddress = cursor.getString(3);
                String customerNumber = cursor.getString(4);

                customerList.add(new Customer(id, customerProfile, customerName, customerAddress, customerNumber));
            }
            myAdapter.notifyDataSetChanged();
        } else {
//            Toast.makeText(getContext(), "No data found!", Toast.LENGTH_SHORT).show();
        }
    }

}