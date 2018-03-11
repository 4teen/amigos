package com.squareapps.a4teen.amigos.ViewHolders;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.squareapps.a4teen.amigos.Abstract.HolderBase;
import com.squareapps.a4teen.amigos.databinding.NotificationsListItemBinding;

public class NotificationHolder extends HolderBase {

    private NotificationsListItemBinding binding;

    public NotificationHolder(View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }

    public NotificationsListItemBinding getBinding() {
        return binding;
    }
}


