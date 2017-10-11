package com.squareapps.a4teen.amigos.Fragments;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareapps.a4teen.amigos.Abstract.FragmentBase;
import com.squareapps.a4teen.amigos.Common.Objects.User;
import com.squareapps.a4teen.amigos.R;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.squareapps.a4teen.amigos.Common.Contract.EMAIL;
import static com.squareapps.a4teen.amigos.Common.Contract.GROUPS;
import static com.squareapps.a4teen.amigos.Common.Contract.GROUP_ID;
import static com.squareapps.a4teen.amigos.Common.Contract.MEMBERS;
import static com.squareapps.a4teen.amigos.Common.Contract.USERS;
import static java.io.File.separator;

public class SearchUsersFragment extends FragmentBase {


    private static ActionMode mActionMode;
    private static String groupID;
    @BindView(R.id.search_friends_recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.search_friends_toolbar)
    Toolbar toolbar;
    @BindView(R.id.search_friends_nested_scroll_view)
    NestedScrollView view;
    private DatabaseReference myDatabaseReference;

    private LinearLayoutManager layoutManager;

    private FirebaseRecyclerAdapter<User, UserViewHolder> firebaseRecyclerAdapter;

    public static SearchUsersFragment newInstance(String groupID) {
        Bundle args = new Bundle();
        args.putSerializable(GROUP_ID, groupID);
        SearchUsersFragment fragment = new SearchUsersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        groupID = getArguments().getSerializable(GROUP_ID).toString();
        myDatabaseReference = getDataRef();

        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setStackFromEnd(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_friends_activity, container, false);
        ButterKnife.bind(this, view);

        setToolbar(toolbar, R.drawable.ic_arrow_back_black_24dp);
        setupRecyclerAdapter(null);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        firebaseRecyclerAdapter.cleanup();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search_byemail, menu);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        MenuItem searchItem = menu.findItem(R.id.search_friends_search_icon);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
    }

    public void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            setupRecyclerAdapter(query.trim());

        }
    }

    private void setupRecyclerAdapter(final String query) {
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(
                User.class,
                android.R.layout.simple_list_item_multiple_choice,
                UserViewHolder.class,
                myDatabaseReference.child(USERS).orderByChild(EMAIL).equalTo(query)
        ) {

            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, User model, int position) {
                if (model != null)
                    viewHolder.bind(model);

            }
        };

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }


    public static class UserViewHolder extends RecyclerView.ViewHolder {


        @BindView(android.R.id.text1)
        CheckedTextView textView1;

        String userID;
        DatabaseReference databaseReference;
        private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

            // Called when the action mode is created; startActionMode() was called
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Inflate a menu resource providing context menu items
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.menu_add, menu);
                return true;
            }

            // Called each time the action mode is shown. Always called after onCreateActionMode, but
            // may be called multiple times if the mode is invalidated.
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false; // Return false if nothing is done
            }

            // Called when the user selects a contextual menu item
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_add_user:
                        updateDatabase();
                        mode.finish(); // Action picked, so close the CAB
                        return true;
                    default:
                        return false;
                }
            }

            // Called when the user exits the action mode
            @Override
            public void onDestroyActionMode(ActionMode mode) {
                mActionMode = null;
            }
        };

        public UserViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, itemView);

            databaseReference = FirebaseDatabase.getInstance().getReference();
            textView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textView1.setChecked(!textView1.isChecked());

                    if (textView1.isChecked()) {
                        // Start the CAB using the ActionMode.Callback defined above
                        mActionMode = ((Activity) v.getContext()).startActionMode(mActionModeCallback);
                    } else if (mActionMode != null) {
                        mActionMode.finish();
                    }

                }
            });
        }

        public void bind(User model) {
            textView1.setText(model.getEmail());
            userID = model.getId();

        }

        private void updateDatabase() {
            HashMap<String, Object> map = new HashMap<>();
            map.put(MEMBERS + separator + groupID + separator + userID, true);
            map.put(USERS + separator + userID + separator + GROUPS + separator + groupID, true);
            databaseReference.updateChildren(map);
        }


    }


}
