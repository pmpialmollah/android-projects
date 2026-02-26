package com.nsoft.mybakery;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductItemSpacingDecorator extends RecyclerView.ItemDecoration {
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int position = parent.getChildAdapterPosition(view);

        int margin = 20;

        outRect.left = margin;
        outRect.right = margin;

        if (position == parent.getAdapter().getItemCount() - 1) {
            outRect.bottom = 220;
        } else {
            outRect.bottom = margin;
        }

        if (position == 0) {
            outRect.top = margin;
        }

    }
}
