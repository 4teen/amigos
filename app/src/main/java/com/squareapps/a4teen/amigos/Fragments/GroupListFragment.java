package com.squareapps.a4teen.amigos.Fragments;


import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.squareapps.a4teen.amigos.Abstract.BaseDialogFragment;
import com.squareapps.a4teen.amigos.Abstract.FragmentBase;
import com.squareapps.a4teen.amigos.Common.Objects.Group;
import com.squareapps.a4teen.amigos.DialogFragments.AddGroupDialogFragment;
import com.squareapps.a4teen.amigos.R;
import com.squareapps.a4teen.amigos.ViewHolders.GroupHolder;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.squareapps.a4teen.amigos.Common.Contract.GROUPS;
import static com.squareapps.a4teen.amigos.Common.Contract.MEMBERS;
import static com.squareapps.a4teen.amigos.Common.Contract.USERS;
import static java.io.File.separator;


public class GroupListFragment extends FragmentBase {

    public static final String TAG = "GroupListFragment";
    private final int REQUEST_GROUP = 0;
    //[START declare_RecyclerView]
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private FirebaseRecyclerAdapter<Group, GroupHolder> adapter;
    private DatabaseReference databaseReference;

    // Set up FirebaseRecyclerAdapter with the Querys
    private static final Query keysRef = getDataRef().child(USERS).child(getUid()).child(GROUPS);
    private static final DatabaseReference dataRef = getDataRef().child(GROUPS);

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
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
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        ButterKnife.bind(this, view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setupAdapter(newAdapter());
        registerForContextMenu(mRecyclerView);
        return view;
    }

    private FirebaseRecyclerAdapter<Group, GroupHolder> newAdapter() {

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Group>()
                .setIndexedQuery(keysRef, dataRef, Group.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Group, GroupHolder>(options) {

            @Override
            public GroupHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new GroupHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.main_list_item, parent, false));
            }

            @Override
            protected void onBindViewHolder(GroupHolder holder, int position, Group model) {
                holder.bind(model);
            }
        };
        return adapter;
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

                Group group = new Group(groupName, getUser().getDisplayName(), key);
                databaseReference.child(GROUPS)
                        .child(key)
                        .setValue(group, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                HashMap<String, Object> groupsMap = new HashMap<>();
                                groupsMap.put(MEMBERS + separator + key + separator + getUid(), true);
                                groupsMap.put(USERS + separator + getUid() + separator + GROUPS + separator + key, true);
                                getDataRef().updateChildren(groupsMap);

                            }
                        });

            }

        }


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
        Group group = adapter.getItem(item.getOrder());
        switch (item.getItemId()) {
            case R.id.delete_group_context_menu:
                HashMap<String, Object> map = new HashMap<>();
                map.put(USERS + separator + getUid() + separator + GROUPS + separator + group.getGroupId(), null);
                map.put(GROUPS + separator + group.getGroupId(), null);
                map.put(MEMBERS + separator + group.getGroupId(), null);
                getDataRef().updateChildren(map);
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }

    public void setupAdapter(FirebaseRecyclerAdapter<Group, GroupHolder> adapter) {
        if (isAdded()) {
            mRecyclerView.setAdapter(adapter);
            Log.d("setAdapter", "GroupViewHolder was called");
        }
    }


}
