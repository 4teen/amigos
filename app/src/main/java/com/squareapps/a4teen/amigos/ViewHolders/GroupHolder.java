package com.squareapps.a4teen.amigos.ViewHolders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import com.squareapps.a4teen.amigos.Abstract.HolderBase;
import com.squareapps.a4teen.amigos.Activities.ChatActivity;
import com.squareapps.a4teen.amigos.Common.Contract;
import com.squareapps.a4teen.amigos.Common.Objects.Group;
import com.squareapps.a4teen.amigos.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class GroupHolder extends HolderBase<Group> implements View.OnClickListener, View.OnCreateContextMenuListener {


    private static void initiateChatActivity(String groupId, String groupName, View view) {
        Intent i = new Intent(view.getContext(), ChatActivity.class);
        Bundle values = new Bundle();
        values.putString(Contract.GROUP_ID, groupId);
        values.putString(Contract.GROUP_NAME, groupName);
        i.putExtras(values);
        view.getContext().startActivity(i);
    }


    @BindView(R.id.main_list_item_text1)
    TextView mNameField;

    @BindView(R.id.main_list_item_text2)
    TextView lastMessageField;

    @BindView(R.id.main_list_item_text3)
    TextView timestamp;

    @BindView(R.id.avatar)
    CircleImageView avatar;


    private String groupID;
    private String groupName;


    public GroupHolder(View v) {
        super(v);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
        ButterKnife.bind(this, v);
        Log.d("inside Recycleer", "GroupViewHolder was called");
    }

    public void bind(Group group) {
        mNameField.setText(group.getName());
        lastMessageField.setText(group.getMessage());
        timestamp.setText(group.getTimestamp());
        setImageView(group.getAvatarUrl(), avatar);

        this.groupID = group.getGroupId();
        this.groupName = group.getName();

    }

    @Override
    public void onClick(View v) {
        initiateChatActivity(this.groupID, this.groupName, v);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Group options");
        menu.add(0, R.id.delete_group_context_menu, getAdapterPosition(), "Delete Group");
    }
}
