package com.squareapps.a4teen.amigos.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.squareapps.a4teen.amigos.Abstract.FragmentBase;
import com.squareapps.a4teen.amigos.Activities.SearchUsersActivity;
import com.squareapps.a4teen.amigos.Common.Objects.User;
import com.squareapps.a4teen.amigos.R;
import com.squareapps.a4teen.amigos.ViewHolders.UserResultsHolder;

import static com.squareapps.a4teen.amigos.Common.Contract.CONTACTS;
import static com.squareapps.a4teen.amigos.Common.Contract.USERS;


public class SearchContactsFragment extends FragmentBase {

    private static final String TAG = "Search Contacts in Firebase";
    public static final String EXTRA = "extra";

    // Set up FirebaseRecyclerAdapter with the Querys
    private static final Query keysRef = getDataRef().child(USERS).child(getUid()).child(CONTACTS);
    private static final DatabaseReference dataRef = getDataRef().child("phoneNumbers");

    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<User, UserResultsHolder> adapter;

    private ProgressBar progressBar;


    public static SearchContactsFragment newInstance(Bundle bunlde) {
        SearchContactsFragment fragment = new SearchContactsFragment();
        fragment.setArguments(bunlde);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recycler_view_tool_bar, container, false);

        Toolbar toolbar = v.findViewById(R.id.toolbar);
        recyclerView = v.findViewById(R.id.recycler_view);
        progressBar = v.findViewById(R.id.progress_bar);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("Contacts in Amigos");

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(newAdapter());

        return v;
    }

    public FirebaseRecyclerAdapter<User, UserResultsHolder> newAdapter() {
        progressBar.setVisibility(View.VISIBLE);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<User>()
                .setIndexedQuery(keysRef, dataRef, User.class)
                .setLifecycleOwner(((SearchUsersActivity) getActivity()))
                .build();

        adapter = new FirebaseRecyclerAdapter<User, UserResultsHolder>(options) {

            @Override
            public UserResultsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = newItemView(parent, android.R.layout.simple_list_item_multiple_choice);
                return new UserResultsHolder(itemView);
            }

            @Override
            protected void onBindViewHolder(UserResultsHolder holder, int position, User model) {
                holder.bind(model);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
