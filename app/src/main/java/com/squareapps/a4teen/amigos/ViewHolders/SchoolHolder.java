package com.squareapps.a4teen.amigos.ViewHolders;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.squareapps.a4teen.amigos.databinding.SchoolListItemBinding;

/**
 * Created by y-pol on 1/7/2018.s
 */

public class SchoolHolder extends RecyclerView.ViewHolder {
    private SchoolListItemBinding binding;


    public SchoolHolder(View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }

    public SchoolListItemBinding getBinding() {
        return binding;
    }

}
