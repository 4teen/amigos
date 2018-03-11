package com.squareapps.a4teen.amigos.ViewHolders;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;

import com.bumptech.glide.Glide;
import com.squareapps.a4teen.amigos.Abstract.GlideApp;
import com.squareapps.a4teen.amigos.R;
import com.squareapps.a4teen.amigos.databinding.ActivityChatListItemBinding;

public class ChatHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

    private ActivityChatListItemBinding binding;

    public ChatHolder(View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
        itemView.setOnCreateContextMenuListener(this);

    }

    public ActivityChatListItemBinding getBinding() {
        return binding;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Message Options");
        menu.add(0, R.id.chat_fragment_delete_item_menu, getAdapterPosition(), "Delete");
    }

    public void clearImageHolders(){
        Glide.with(binding.messengerImageView).clear(binding.messengerImageView);
        GlideApp.with(binding.messengerImageView).clear(binding.messengerImageView);
        GlideApp.with(binding.messageImageView).clear(binding.messageImageView);
    }
}