package com.nsoft.sqliteapp.ui.fragments;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nsoft.sqliteapp.R;
import com.nsoft.sqliteapp.adapters.MyAdapter;
import com.nsoft.sqliteapp.databinding.FragmentShowBinding;
import com.nsoft.sqliteapp.viewmodel.DataViewModel;

public class ShowFragment extends Fragment {
    private FragmentShowBinding binding;
    private DataViewModel viewModel;
    private MyAdapter myAdapter;
    private String currentTab = "all";
    private boolean isSearchVisible = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentShowBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(DataViewModel.class);

        myAdapter = new MyAdapter(id -> {
            // Handle item click
        });

        binding.showFragmentRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.showFragmentRecyclerView.setAdapter(myAdapter);

        // Initial UI state
        updateUI(binding.tabAll);

        binding.tabAll.setOnClickListener(v -> {
            hideSearch();
            updateUI(binding.tabAll);
        });
        binding.tabExpenses.setOnClickListener(v -> {
            hideSearch();
            updateUI(binding.tabExpenses);
        });
        binding.tabIncomes.setOnClickListener(v -> {
            hideSearch();
            updateUI(binding.tabIncomes);
        });

        binding.ivSearch.setOnClickListener(v -> {
            if (!isSearchVisible) {
                showSearch();
            } else {
                hideSearch();
            }
        });

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                performSearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Hide search on scroll
        binding.showFragmentRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (Math.abs(dy) > 10) {
                    hideSearch();
                }
            }
        });

        // Hide search when clicking root layout
        binding.rootLayout.setOnClickListener(v -> hideSearch());
    }

    private void showSearch() {
        isSearchVisible = true;
        binding.tvDetailsTitle.setVisibility(View.GONE);
        binding.etSearch.setVisibility(View.VISIBLE);
        binding.etSearch.animate()
                .alpha(1f)
                .translationX(0f)
                .setDuration(300)
                .withEndAction(() -> {
                    binding.etSearch.requestFocus();
                    showKeyboard();
                })
                .start();
        binding.ivSearch.setImageResource(R.drawable.search); // Close icon set kora dorkar if available
    }

    private void hideSearch() {
        if (!isSearchVisible) return;
        isSearchVisible = false;
        hideKeyboard();
        binding.etSearch.animate()
                .alpha(0f)
                .translationX(100f)
                .setDuration(300)
                .withEndAction(() -> {
                    binding.etSearch.setVisibility(View.GONE);
                    binding.etSearch.setText("");
                    binding.tvDetailsTitle.setVisibility(View.VISIBLE);
                })
                .start();
        binding.ivSearch.setImageResource(R.drawable.search);
    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(binding.etSearch, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(binding.etSearch.getWindowToken(), 0);
        }
    }

    private void performSearch(String query) {
        if (query.isEmpty()) {
            refreshTabData();
        } else {
            viewModel.getFilteredData(currentTab, query).observe(getViewLifecycleOwner(), list -> {
                myAdapter.setExpenseModelList(list);
            });
        }
    }

    private void refreshTabData() {
        if (currentTab.equals("all")) {
            viewModel.getAllData().observe(getViewLifecycleOwner(), list -> myAdapter.setExpenseModelList(list));
        } else {
            viewModel.getIndividualData(currentTab).observe(getViewLifecycleOwner(), list -> myAdapter.setExpenseModelList(list));
        }
    }

    private void updateUI(TextView selectedTab) {
        if (!isAdded() || binding == null) return;

        resetTabStyle(binding.tabAll);
        resetTabStyle(binding.tabExpenses);
        resetTabStyle(binding.tabIncomes);

        selectedTab.setTextColor(Color.WHITE);
        selectedTab.setBackgroundTintList(
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.dark_primary))
        );

        if (selectedTab == binding.tabAll) {
            currentTab = "all";
            binding.tvDetailsTitle.setText("All Transactions");
        } else if (selectedTab == binding.tabExpenses) {
            currentTab = "expense";
            binding.tvDetailsTitle.setText("Expense List");
        } else if (selectedTab == binding.tabIncomes) {
            currentTab = "income";
            binding.tvDetailsTitle.setText("Income List");
        }

        performSearch(binding.etSearch.getText().toString());
    }

    private void resetTabStyle(TextView tab) {
        tab.setTextColor(Color.BLACK);
        tab.setBackgroundResource(R.drawable.rectangle_item_background);
        tab.setBackgroundTintList(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
