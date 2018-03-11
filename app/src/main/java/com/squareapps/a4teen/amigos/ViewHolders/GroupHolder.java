package com.squareapps.a4teen.amigos.ViewHolders;

import android.databinding.DataBindingUtil;
import android.view.ContextMenu;
import android.view.View;

import com.squareapps.a4teen.amigos.Abstract.HolderBase;
import com.squareapps.a4teen.amigos.R;
import com.squareapps.a4teen.amigos.databinding.MainListItemBinding;

public class GroupHolder extends HolderBase implements View.OnCreateContextMenuListener {

    private MainListItemBinding binding;

    public GroupHolder(View v) {
        super(v);
        v.setOnCreateContextMenuListener(this);
        this.binding = DataBindingUtil.bind(v);
    }

    public MainListItemBinding getBinding() {
        return binding;
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Group options");
        menu.add(0, R.id.delete_group_context_menu, getAdapterPosition(), "Delete Group");

    }

}
