package com.example.todoapp;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.Adapter.todoAdapter;


public class RecyclerViewItemTouchHelper extends ItemTouchHelper.SimpleCallback {
       private todoAdapter adapter;

    public RecyclerViewItemTouchHelper(todoAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
    }


    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition();
        if (direction == ItemTouchHelper.LEFT) {
            AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
            builder.setTitle("Delete Task");
            builder.setMessage("Are Sure to Delete Task");
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.deleteItem(position);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            adapter.editItem(position);
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        Drawable icon;
        Drawable background;
        View itemview = viewHolder.itemView;
        int backgroundCornerOffset = 20;

        if(dX>0){
            icon = ContextCompat.getDrawable(adapter.getContext(),R.drawable.edit);
            background = new ColorDrawable(ContextCompat.getColor(adapter.getContext(),R.color.purple_700));
        }
        else {

            icon = ContextCompat.getDrawable(adapter.getContext(),R.drawable.delete);
            background = new ColorDrawable(ContextCompat.getColor(adapter.getContext(),R.color.design_default_color_error));
        }
        int iconMargin = (itemview.getHeight()-icon.getIntrinsicHeight())/2;
        int iconTop = itemview.getTop()+(itemview.getHeight()-icon.getIntrinsicHeight())/2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        if (dX > 0) { // Swiping to the right
            int iconLeft = itemview.getLeft() + iconMargin;
            int iconRight = itemview.getLeft() + iconMargin + icon.getIntrinsicWidth();
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemview.getLeft(), itemview.getTop(),
                    itemview.getLeft() + ((int) dX) + backgroundCornerOffset, itemview.getBottom());
        } else if (dX < 0) { // Swiping to the left
            int iconLeft = itemview.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemview.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemview.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemview.getTop(), itemview.getRight(), itemview.getBottom());
        } else { // view is unSwiped
            background.setBounds(0, 0, 0, 0);
        }
        background.draw(c);
        icon.draw(c);

    }
}