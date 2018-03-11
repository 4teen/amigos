package com.squareapps.a4teen.amigos.ViewHolders;


import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.squareapps.a4teen.amigos.databinding.InvitationListItemBinding;

/**
 * Created by y-pol on 2/27/2018.s
 */

public class InvitationHolder extends RecyclerView.ViewHolder {
    private InvitationListItemBinding binding;

    public InvitationHolder(View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }



    public InvitationListItemBinding getBinding() {
        return binding;
    }

}
