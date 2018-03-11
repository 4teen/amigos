package com.squareapps.a4teen.amigos.ViewHolders;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;

import com.squareapps.a4teen.amigos.R;
import com.squareapps.a4teen.amigos.databinding.CourseListItemBinding;

public class CourseListHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

    private final CourseListItemBinding binding;

    public CourseListHolder(View itemView) {
        super(itemView);
        this.binding = DataBindingUtil.bind(itemView);
    }

    public CourseListItemBinding getBinding() {
        return binding;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Class options");
        menu.add(0, R.id.delete, getAdapterPosition(), "Delete Class");

    }


}