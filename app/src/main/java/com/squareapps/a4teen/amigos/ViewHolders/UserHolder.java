package com.squareapps.a4teen.amigos.ViewHolders;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class UserHolder extends RecyclerView.ViewHolder {

    private final ViewDataBinding binding;

    public UserHolder(View itemView) {
        super(itemView);
        this.binding = DataBindingUtil.bind(itemView);
    }

    public ViewDataBinding getBinding() {
        return binding;
    }

}
