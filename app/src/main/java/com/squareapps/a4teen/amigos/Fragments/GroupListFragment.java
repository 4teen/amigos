package com.squareapps.a4teen.amigos.Fragments;


import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseIndexRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.squareapps.a4teen.amigos.Abstract.BaseDialogFragment;
import com.squareapps.a4teen.amigos.Abstract.FragmentBase;
import com.squareapps.a4teen.amigos.Activities.ChatActivity;
import com.squareapps.a4teen.amigos.Activities.LoginActivity;
import com.squareapps.a4teen.amigos.Common.Objects.Group;
import com.squareapps.a4teen.amigos.DialogFragments.AddGroupDialogFragment;
import com.squareapps.a4teen.amigos.R;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.squareapps.a4teen.amigos.Common.Contract.GROUPS;
import static com.squareapps.a4teen.amigos.Common.Contract.GROUP_ID;
import static com.squareapps.a4teen.amigos.Common.Contract.GROUP_NAME;
import static com.squareapps.a4teen.amigos.Common.Contract.MEMBERS;
import static com.squareapps.a4teen.amigos.Common.Contract.USERS;
import static java.io.File.separator;

/**
 * Created by y-pol on 6/30/2017.
 */

public class GroupListFragment extends FragmentBase {

    public static final String ARGS = "args";
    public static final String TAG = "GroupListFragment";
    private final int REQUEST_GROUP = 0;
    //[START declare_RecyclerView]
    @BindView(R.id.group_list_recycler_View)
    RecyclerView mRecyclerView;
    private FirebaseUser user;
    private FirebaseIndexRecyclerAdapter<Group, GroupViewHolder> groupRecyclerAdapter;
    private DatabaseReference databaseReference;
    //[END declare_RecyclerView]

    public static void initiateChatActivity(String groupId, String groupName, View view) {
        Intent i = new Intent(view.getContext(), ChatActivity.class);
        Bundle values = new Bundle();
        values.putString(GROUP_ID, groupId);
        values.putString(GROUP_NAME, groupName);
        i.putExtra(ARGS, values);
        view.getContext().startActivity(i);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        groupRecyclerAdapter.cleanup();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        databaseReference = getDataRef();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.group_list_recycler_view, container, false);
        ButterKnife.bind(this, view);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        groupRecyclerAdapter = new FirebaseIndexRecyclerAdapter<Group, GroupViewHolder>(Group.class,
                R.layout.main_list_item,
                GroupViewHolder.class,
                databaseReference.child(USERS).child(getUid()).child(GROUPS),
                databaseReference.child(GROUPS)
        ) {
            @Override
            protected void populateViewHolder(GroupViewHolder viewHolder, Group model, int position) {
                viewHolder.bind(model);

            }
        };

        setupAdapter();
        registerForContextMenu(mRecyclerView);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;

        if (requestCode == REQUEST_GROUP) {
            String groupName = data
                    .getSerializableExtra(BaseDialogFragment.EXTRA1)
                    .toString();

            if (groupName != null) {
                final String key = databaseReference.child(GROUPS).push().getKey();

                Group group = new Group(groupName, user.getDisplayName(), key);
                databaseReference.child(GROUPS)
                        .child(key)
                        .setValue(group, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                HashMap<String, Object> groupsMap = new HashMap<>();
                                groupsMap.put(MEMBERS + separator + key + separator + getUid(), true);
                                groupsMap.put(USERS + separator + getUid() + separator + GROUPS + separator + key, true);
                                updateDataSet(groupsMap);

                            }
                        });

            }

        }


    }

    private void updateDataSet(HashMap<String, Object> groupsMap) {
        databaseReference.updateChildren(groupsMap);
        groupRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, R.id.create_group, 2, "Create group");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.create_group:
                FragmentManager manager = getFragmentManager();
                AddGroupDialogFragment dialogFragment = new AddGroupDialogFragment();
                dialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
                dialogFragment.setTargetFragment(GroupListFragment.this, REQUEST_GROUP);
                dialogFragment.show(manager, "DIALOG_GROUP");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.d(TAG, "onContextItemSelected was called");
        Group group = groupRecyclerAdapter.getItem(item.getOrder());
        switch (item.getItemId()) {
            case R.id.delete_group_context_menu:
                HashMap<String, Object> map = new HashMap<>();
                map.put(USERS + separator + getUid() + separator + GROUPS + separator + group.getGroupId(), null);
                map.put(GROUPS + separator + group.getGroupId(), null);
                map.put(MEMBERS + separator + group.getGroupId(), null);
                updateDataSet(map);
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }

    public void setupAdapter() {
        if (isAdded()) {
            mRecyclerView.setAdapter(groupRecyclerAdapter);
            Log.d("setAdapter", "GroupViewHolder was called");
        }
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

        @BindView(R.id.main_list_item_text1)
        TextView mNameField;

        @BindView(R.id.main_list_item_text2)
        TextView lastMessageField;

        @BindView(R.id.main_list_item_text3)
        TextView timestamp;

        String groupID;
        String groupName;

        public GroupViewHolder(View v) {
            super(v);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
            ButterKnife.bind(this, itemView);
            Log.d("inside Recycleer", "GroupViewHolder was called");
        }

        public void bind(Group group) {
            mNameField.setText(group.getName());
            lastMessageField.setText(group.getMessage());
            timestamp.setText(group.getTimestamp());
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


}
