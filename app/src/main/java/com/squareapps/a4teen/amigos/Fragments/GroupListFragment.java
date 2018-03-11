package com.squareapps.a4teen.amigos.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.squareapps.a4teen.amigos.Abstract.FragmentBase;
import com.squareapps.a4teen.amigos.Abstract.GlideApp;
import com.squareapps.a4teen.amigos.Activities.ChatActivity;
import com.squareapps.a4teen.amigos.Activities.CreateGroupActivity;
import com.squareapps.a4teen.amigos.Activities.MainActivity;
import com.squareapps.a4teen.amigos.BR;
import com.squareapps.a4teen.amigos.Common.POJOS.Group;
import com.squareapps.a4teen.amigos.Common.Utils.AppPreferences;
import com.squareapps.a4teen.amigos.R;
import com.squareapps.a4teen.amigos.ViewHolders.GroupHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.squareapps.a4teen.amigos.Common.Contract.GROUPS;
import static com.squareapps.a4teen.amigos.Common.Contract.Group.GROUP_ID;
import static com.squareapps.a4teen.amigos.Common.Contract.Group.GROUP_NAME;
import static com.squareapps.a4teen.amigos.Common.Contract.USERS;
import static com.squareapps.a4teen.amigos.Common.Contract.User.AVATAR_URL;
import static com.squareapps.a4teen.amigos.Common.Contract.User.NAME;


public class GroupListFragment extends FragmentBase implements View.OnClickListener {

    public final String TAG = getSimpleName(GroupListFragment.this);
    private final int REQUEST_GROUP = 0;
    @BindView(R.id.recycler_view)
    EmptyRecyclerView mRecyclerView;
    @BindView(R.id.emptyView)
    TextView emptyView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    private FirebaseRecyclerAdapter<Group, GroupHolder> adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.empty_recycler_view, container, false);
        ButterKnife.bind(this, v);

        progressBar.setVisibility(View.VISIBLE);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);

        mRecyclerView.addItemDecoration(itemDecoration);

        mRecyclerView.setAdapter(newAdapter());

        emptyView.setText("No groups");
        mRecyclerView.setEmptyView(emptyView);

        registerForContextMenu(mRecyclerView);
        return v;
    }

    private FirebaseRecyclerAdapter<Group, GroupHolder> newAdapter() {

        // Set up FirebaseRecyclerAdapter with the Querys
        final Query keysRef = getDataRef()
                .child(USERS)
                .child(getUid())
                .child(GROUPS);

        final DatabaseReference dataRef = getDataRef()
                .child(GROUPS);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Group>()
                .setIndexedQuery(keysRef, dataRef, Group.class)
                .setLifecycleOwner((MainActivity) getActivity())
                .build();

        adapter = new FirebaseRecyclerAdapter<Group, GroupHolder>(options) {

            @Override
            public GroupHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = newItemView(parent, R.layout.main_list_item);
                view.setOnClickListener(GroupListFragment.this);
                return new GroupHolder(view);
            }

            @Override
            protected void onBindViewHolder(GroupHolder holder, int position, Group model) {
                holder.getBinding().setVariable(BR.group, model);
                holder.getBinding().executePendingBindings();

                //persists group by groupId
                AppPreferences.setPrefGroupName(getContext(), model.getGroupId(), model.getName());
                AppPreferences.setPrefGroupAvatarUrl(getContext(), model.getGroupId(), model.getAvatarUrl());

                //TODO delete persisted data when group is deleted
            }

            @Override
            public void onViewRecycled(GroupHolder holder) {
                super.onViewRecycled(holder);
                GlideApp.with(getContext()).clear(holder.getBinding().avatar);

                holder.getBinding()
                        .avatar.setImageDrawable(getResources()
                        .getDrawable(R.drawable.ic_group_work_black_24dp));
            }


            @Override
            public void onDataChanged() {
                if (progressBar != null && progressBar.isShown())
                    progressBar.setVisibility(View.GONE);
            }
        };
        return adapter;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_GROUP) {
            String groupName = data.getStringExtra(GROUP_NAME);

            Toast.makeText(getContext()
                    , "Creating group " + groupName + "..."
                    , Toast.LENGTH_LONG)
                    .show();

          /*  Snackbar.make((View)rootView.getParent().getParent()
                    , "Creating group " + groupName + ""
                    , Snackbar.LENGTH_LONG).show();*/
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
                startActivityForResult(new Intent(getContext(), CreateGroupActivity.class), REQUEST_GROUP);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_group_context_menu:
                Group group = adapter.getItem(item.getOrder());
                getDataRef().child(GROUPS).child(group.getGroupId()).removeValue();
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }

    @Override
    public void onClick(View v) {
        Group group = adapter.getItem(mRecyclerView.getChildAdapterPosition(v));
        Intent i = new Intent(v.getContext(), ChatActivity.class);
        i.putExtra(GROUP_ID, group.getGroupId());
        i.putExtra(AVATAR_URL, group.getAvatarUrl());
        i.putExtra(NAME, group.getName());
        startActivity(i);

    }

}
