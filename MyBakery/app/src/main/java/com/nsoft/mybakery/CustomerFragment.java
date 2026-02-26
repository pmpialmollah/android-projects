package com.nsoft.mybakery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.nsoft.mybakery.databinding.FragmentCustomerBinding;

public class CustomerFragment extends Fragment {
    private FragmentCustomerBinding binding;
    private CustomerRecyclerViewAdapter myAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCustomerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // my code here ----------------------------------------------------------------------------
        myAdapter = new CustomerRecyclerViewAdapter();

        binding.customerRecyclerView.addItemDecoration(new ProductItemSpacingDecorator());
        binding.customerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.customerRecyclerView.setAdapter(myAdapter);

        // -----------------------------------------------------------------------------------------
    }
}