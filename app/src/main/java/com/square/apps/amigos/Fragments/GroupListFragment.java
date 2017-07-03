package com.square.apps.amigos.Fragments;

import android.app.Fragment;
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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.square.apps.amigos.Activities.LoginActivity;
import com.square.apps.amigos.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by y-pol on 6/30/2017.
 */

public class GroupListFragment extends Fragment {

    private final String GROUPS_CHILD = "groups";
    List<String> groups = new ArrayList<>();

    private String mUsername;
    private String mPhotoUrl;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    // Firebase instance variables
    private DatabaseReference mFirebaseDatabaseReference;


    //[START declare_RecyclerView]
    private RecyclerView mRecyclerView;
    //[END declare_RecyclerView]

    private FragmentManager fragmentManager;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
        }
        fragmentManager = getFragmentManager();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_groups, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.create_group:
                FragmentManager manager = getFragmentManager();
                AddGroupDialogFragment dialogFragment = new AddGroupDialogFragment();
                dialogFragment.show(manager, "addGroupFragment");
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.group_list_recycler_view, container, false);

        //[START initialize_recycler_view]
        mRecyclerView = (RecyclerView) view.findViewById(R.id.group_list_recycler_View);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //[END initialize_recycler_view]
        setupAdapter();
        return view;
    }

    public void setupAdapter() {

        // New child entries
        String myId = mFirebaseUser.getUid();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users/" + myId + "/groups");
        String ref = mFirebaseDatabaseReference.toString();

        final GroupRecyclerAdapter groupRecyclerAdapter = new GroupRecyclerAdapter(groups);

        Log.d("TAG", myId + "/n" + ref);
        mFirebaseDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                groups.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String result = postSnapshot.getKey();
                    groups.add(result);
                    Log.d("TAG", "yoel is a member of" + result + " group");
                }
                groupRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (isAdded()) {
            mRecyclerView.setAdapter(groupRecyclerAdapter);
            Log.d("setAdapter", "GroupViewHolder was called");
        }
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {

        TextView mNameField;

        public GroupViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.main_list_item, parent, false));
            mNameField = (TextView) itemView.findViewById(R.id.main_list_item_text1);
            Log.d("inside Recycleer", "GroupViewHolder was called");
        }

    }

    public class GroupRecyclerAdapter extends RecyclerView.Adapter<GroupViewHolder> {

        private List<String> mGroups;

        public GroupRecyclerAdapter(List<String> groups) {
            mGroups = groups;
        }

        @Override
        public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new GroupViewHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(GroupViewHolder holder, int position) {
            holder.mNameField.setText(mGroups.get(position));
        }

        @Override
        public int getItemCount() {
            return mGroups.size();
        }
    }

}
