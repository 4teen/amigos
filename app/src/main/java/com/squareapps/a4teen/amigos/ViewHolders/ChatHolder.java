package com.squareapps.a4teen.amigos.ViewHolders;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareapps.a4teen.amigos.Abstract.HolderBase;
import com.squareapps.a4teen.amigos.Common.Objects.Message;
import com.squareapps.a4teen.amigos.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatHolder extends HolderBase implements View.OnCreateContextMenuListener {
    @BindView(R.id.messageTextView)
    TextView messageTextView;
    @BindView(R.id.messageImageView)
    ImageView messageImageView;
    @BindView(R.id.messengerTextView)
    TextView messengerTextView;
    @BindView(R.id.messengerImageView)
    CircleImageView messengerImageView;

    public ChatHolder(View itemView) {
        super(itemView);
        itemView.setOnCreateContextMenuListener(this);
        ButterKnife.bind(this, itemView);
    }

    public void bind(Message model) {

        if (model.getImageUrl() != null) {
            setImageView(model.getImageUrl(), messageImageView);

            messageTextView.setVisibility(TextView.GONE);
            messageImageView.setVisibility(ImageView.VISIBLE);

        } else if (model.getText() != null) {
            messageTextView.setText(model.getText());

            messageTextView.setVisibility(TextView.VISIBLE);
            messageImageView.setVisibility(ImageView.GONE);
        }

        //populate name of sender
        messengerTextView.setText(model.getName());

        setImageView(model.getPhotoUrl(), messengerImageView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Message Options");
        menu.add(0, R.id.chat_fragment_delete_item_menu, getAdapterPosition(), "Delete");
    }
}