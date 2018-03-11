package com.squareapps.a4teen.amigos.Fragments;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.squareapps.a4teen.amigos.Abstract.FragmentBase;
import com.squareapps.a4teen.amigos.Activities.SearchUsersActivity;
import com.squareapps.a4teen.amigos.BR;
import com.squareapps.a4teen.amigos.Common.POJOS.Invitation;
import com.squareapps.a4teen.amigos.Common.POJOS.User;
import com.squareapps.a4teen.amigos.Common.Utils.QueryPreferences;
import com.squareapps.a4teen.amigos.R;
import com.squareapps.a4teen.amigos.ViewHolders.UserHolder;
import com.squareapps.a4teen.amigos.databinding.CreateGroupUserListItemBinding;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.squareapps.a4teen.amigos.Common.Contract.GROUP_INVITATIONS;
import static com.squareapps.a4teen.amigos.Common.Contract.TIMESTAMP;
import static com.squareapps.a4teen.amigos.Common.Contract.USERS_IN_APP;
import static com.squareapps.a4teen.amigos.Common.Contract.User.EMAIL;
import static java.io.File.separator;

public class SearchUsersFragment extends FragmentBase implements View.OnClickListener {

    public static final String EXTRA_SEARCH_BY = "searchBy";
    private static final String TAG = "searchUsersFragment";

    @BindView(R.id.horizontalScrollViewLL)
    LinearLayout linearLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private FirebaseRecyclerAdapter<User, UserHolder> adapter;
    private ArrayList<User> newMembers = new ArrayList<>();

    public static SearchUsersFragment newInstance(Bundle bundle) {
        SearchUsersFragment fragment = new SearchUsersFragment();
        fragment.setArguments(bundle);
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
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_users, container, false);
        ButterKnife.bind(this, view);

        linearLayout = view.findViewById(R.id.horizontalScrollViewLL);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(newAdapter(QueryPreferences.getPrefUsersSearchQuery(getContext())));

        fab.setOnClickListener(view1 -> {
            if (!newMembers.isEmpty()) {

                for (User user : newMembers) {
                    DatabaseReference reference = getDataRef()
                            .child(GROUP_INVITATIONS)
                            .child(user.getId());

                    String invitationId = reference.push().getKey();

                    Invitation invitation = new Invitation(getArguments());
                    reference.child(invitationId).setValue(invitation);

                    HashMap<String, Object> updates = new HashMap<>();
                    updates.put(invitationId + separator + TIMESTAMP, ServerValue.TIMESTAMP);
                    updates.put(invitationId + separator + "status", "pending");

                    reference.updateChildren(updates);

                }


                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            } else {
                Snackbar.make(view1, "no users are selected", Snackbar.LENGTH_SHORT).show();

            }

        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        QueryPreferences.setPrefUsersSearchQuery(getContext(), null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Called when the action bar search text has changed.  Updates
                // the search filter, and restarts the loader to do a new query
                // using the new search string.
                String newFilter = !TextUtils.isEmpty(newText) ? newText : null;
                String oldFilter = QueryPreferences.getPrefUsersSearchQuery(getContext());

                // Don't do anything if the filter is empty
                if (oldFilter == null && newFilter == null) {
                    return true;
                }

                // Don't do anything if the new filter is the same as the current filter
                if (oldFilter != null && oldFilter.equals(newFilter)) {
                    return true;
                }

                QueryPreferences.setPrefUsersSearchQuery(getContext(), newFilter);
                updateSearch();
                return false;


            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_clear:
                QueryPreferences.setPrefUsersSearchQuery(getContext(), null);
                updateSearch();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private FirebaseRecyclerAdapter<User, UserHolder> newAdapter(final String query) {

        Log.d(TAG, query == null ? "null" : query);

        //search by email or name
        String param = getArguments().getString(EXTRA_SEARCH_BY);

        //conduct a default search by e-mail if no value in EXTRA_SEARCH_BY
        if (param == null)
            param = EMAIL;

        Query dataRef = getDataRef().child(USERS_IN_APP)
                .orderByChild(param)
                .startAt(query)
                .endAt(query + "\uf8ff");

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(dataRef, snapshot -> {
                    User newUser = snapshot.getValue(User.class);
                    newUser.setId(snapshot.getKey());
                    return newUser;
                })
                .setLifecycleOwner(((SearchUsersActivity) getActivity()))
                .build();

        adapter = (query == null) ? null : new FirebaseRecyclerAdapter<User, UserHolder>(options) {

            @Override
            public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = newItemView(parent, R.layout.contacts_list_item_add_members_to_groups);
                view.setOnClickListener(SearchUsersFragment.this);
                return new UserHolder(view);
            }

            @Override
            protected void onBindViewHolder(UserHolder holder, int position, User model) {
                holder.getBinding().setVariable(BR.contact, model);
                holder.getBinding().executePendingBindings();

            }

        };
        return adapter;


    }

    public void updateSearch() {
        recyclerView.swapAdapter(newAdapter(QueryPreferences.getPrefUsersSearchQuery(getContext())), false);
    }

    @Override
    public void onClick(View view) {

        int position = recyclerView.getChildAdapterPosition(view);
        User user = adapter.getItem(position);

        View contactView = LayoutInflater.from(getContext())
                .inflate(R.layout.create_group_user_list_item, linearLayout, false);
        contactView.setPadding(1, 1, 1, 1);

        CreateGroupUserListItemBinding binding = DataBindingUtil.bind(contactView);
        binding.setUser(user);
        binding.executePendingBindings();

        if (newMembers.contains(user)) {
            int pos = newMembers.indexOf(user);
            linearLayout.removeViewAt(pos);
            newMembers.remove(user);
        } else {
            linearLayout.addView(binding.getRoot());
            newMembers.add(user);
        }

        ((HorizontalScrollView) linearLayout.getParent()).setVisibility(newMembers.isEmpty() ? GONE : VISIBLE);

    }

}
