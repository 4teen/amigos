package com.squareapps.a4teen.amigos.ViewHolders;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.squareapps.a4teen.amigos.databinding.ImageViewObjectBinding;

public class MediaHolder extends RecyclerView.ViewHolder {

    private ImageViewObjectBinding binding;

    public MediaHolder(View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }


    public ImageViewObjectBinding getBinding() {
        return binding;
    }


}