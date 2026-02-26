package com.nsoft.mybakery;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.nsoft.mybakery.databinding.FragmentHomeBinding;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private DashboardRecyclerviewAdapter myAdapter;

    private boolean isKeyboardVisible = false;
    private ViewTreeObserver.OnGlobalLayoutListener keyboardLayoutListener;

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

}
