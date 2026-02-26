package com.nsoft.mybakery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.nsoft.mybakery.databinding.FragmentHomeBinding;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private DashboardRecyclerviewAdapter myAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // my code here ----------------------------------------------------------------------------

        myAdapter = new DashboardRecyclerviewAdapter();
        binding.dashboardRecyclerView.setLayoutManager(new LinearLayoutManager(
                getContext(),
                LinearLayoutManager.HORIZONTAL,
                false));

        binding.dashboardRecyclerView.setAdapter(myAdapter);

        binding.sellButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Everything is working fine...", Toast.LENGTH_SHORT).show();
        });

        // -----------------------------------------------------------------------------------------
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}