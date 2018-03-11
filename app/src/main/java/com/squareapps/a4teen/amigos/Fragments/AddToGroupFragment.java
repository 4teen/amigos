package com.squareapps.a4teen.amigos.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.squareapps.a4teen.amigos.Abstract.FragmentBase;
import com.squareapps.a4teen.amigos.BR;
import com.squareapps.a4teen.amigos.BuildConfig;
import com.squareapps.a4teen.amigos.Common.POJOS.Group;
import com.squareapps.a4teen.amigos.R;
import com.squareapps.a4teen.amigos.ViewHolders.GroupHolder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.squareapps.a4teen.amigos.Common.Contract.GROUPS;
import static com.squareapps.a4teen.amigos.Common.Contract.MEMBERS;
import static com.squareapps.a4teen.amigos.Common.Contract.UNIQUE_ID;
import static com.squareapps.a4teen.amigos.Common.Contract.USERS;


public class AddToGroupFragment extends FragmentBase implements View.OnClickListener {

    public final String TAG = getSimpleName(AddToGroupFragment.this);
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private FirebaseRecyclerAdapter<Group, GroupHolder> adapter;
    private String userID;
    private ArrayList<Group> groups;

    public static AddToGroupFragment newInstance(Bundle bundle) {
        AddToGroupFragment fragment = new AddToGroupFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

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
        userID = getArguments().getString(UNIQUE_ID);

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view_tool_bar, container, false);
        ButterKnife.bind(this, view);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("Add to group");

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);


        mRecyclerView.addItemDecoration(itemDecoration);

        if (BuildConfig.DEBUG) {
            Log.d("Arguments passed: ", getArguments().toString());
        }

        setupAdapter(newAdapter());
        registerForContextMenu(mRecyclerView);
        return view;
    }

    public void setupAdapter(FirebaseRecyclerAdapter<Group, GroupHolder> adapter) {
        if (isAdded()) {
            mRecyclerView.setAdapter(adapter);
            Log.d("setAdapter", "GroupViewHolder was called");
        }
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
                .setIndexedQuery(keysRef, dataRef, snapshot -> {
                    Log.d("Snapshot", snapshot.toString());
                    Group group = new Group();
                    if (snapshot.child(MEMBERS).hasChild(userID)) {
                        group.setGroupId(null);
                        return group;
                    } else {
                        group = snapshot.getValue(Group.class);
                        group.setGroupId(snapshot.getKey());
                        return group;
                    }
                })
                .build();

        ArrayList<Group> groups = new ArrayList<>();
        ObservableSnapshotArray<Group> array = options.getSnapshots();
        for (int i = 0; i < array.size(); i++) {
            if (!array.get(i).getMembers().containsKey(userID)) {
                groups.add(array.get(i));
            }
        }


        adapter = new FirebaseRecyclerAdapter<Group, GroupHolder>(options) {


            @Override
            public GroupHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.main_list_item, parent, false);
                view.setOnClickListener(AddToGroupFragment.this);

                return new GroupHolder(view);
            }

            @Override
            protected void onBindViewHolder(final GroupHolder holder, final int position, final Group model) {
                getSnapshots().get(position);
                if (model.getGroupId() != null) {
                    holder.getBinding().setVariable(BR.group, model);
                    holder.getBinding().executePendingBindings();
                }
            }


            @Override
            public int getItemCount() {
                int size = 0;
                for (int i = 0; i < getSnapshots().size(); i++) {
                    if (getSnapshots().get(i).getGroupId() != null)
                        size++;

                }
                return size;
            }
        };
        return adapter;
    }


    @Override
    public void onClick(final View v) {

    /*    final int position = mRecyclerView.getChildAdapterPosition(v);
        Group group = adapter.getItem(position);
        // mRecyclerView.removeViewAt(position);
        mRecyclerView.getLayoutManager().removeView(v);

       *//* final String groupId = group.getGroupId();
        final String groupName = group.getName();
        assert groupId != null;

        final String fromUid = FirebaseAuth.getInstance().getUid();
        final String to = userID;

        getDataRef().child(GROUPS)
                .child(groupId)
                .child(MEMBERS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(to)) {
                    showSnackbar(v.getRootView(), getArguments().getString(NAME) + " already belongs to " + groupName);
                } else {
                    dataSnapshot.child(to).getRef().setValue(true, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            showSnackbar(v.getRootView(), getArguments().getString(NAME) + " was added to " + groupName);
                            NotificationService.startActionGroupAdd(getContext(), fromUid, to, groupId);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());

            }
        });*/

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
}
