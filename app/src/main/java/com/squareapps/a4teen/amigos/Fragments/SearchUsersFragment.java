package com.squareapps.a4teen.amigos.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;
import com.squareapps.a4teen.amigos.Abstract.FragmentBase;
import com.squareapps.a4teen.amigos.Activities.SearchUsersActivity;
import com.squareapps.a4teen.amigos.Common.Objects.User;
import com.squareapps.a4teen.amigos.Common.QueryPreferences;
import com.squareapps.a4teen.amigos.R;
import com.squareapps.a4teen.amigos.ViewHolders.UserResultsHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.squareapps.a4teen.amigos.Common.Contract.EMAIL;
import static com.squareapps.a4teen.amigos.Common.Contract.USERS;

public class SearchUsersFragment extends FragmentBase {

    private static final String TAG = "searchUsersFragment";

    public static final String EXTRA_SEARCH_BY = "searchBy";

    private FirebaseRecyclerAdapter<User, UserResultsHolder> adapter;


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view_tool_bar, container, false);
        ButterKnife.bind(this, view);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        //setToolbar(toolbar, R.drawable.ic_arrow_back_black_24dp);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(newAdapter(QueryPreferences.getPrefUsersSearchQuery(getContext())));

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
                QueryPreferences.setPrefUsersSearchQuery(getContext(), query);
                updateSearch();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "QuerryTextChanged: " + newText);
                if (!newText.isEmpty()) {
                    QueryPreferences.setPrefUsersSearchQuery(getContext(), newText);
                    updateSearch();
                    return true;
                }

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

    private FirebaseRecyclerAdapter<User, UserResultsHolder> newAdapter(final String query) {
        Log.d(TAG, query == null ? "null" : query);

        //search by email or name
        String param = getArguments().getString(EXTRA_SEARCH_BY);

        //conduct a default search by e-mail if no value in EXTRA_SEARCH_BY
        if (param == null)
            param = EMAIL;

        Query dataRef = getDataRef().child(USERS)
                .orderByChild(param)
                .startAt(query)
                .endAt(query + "\uf8ff");

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(dataRef, User.class)
                .setLifecycleOwner(((SearchUsersActivity) getActivity()))
                .build();

        adapter = (query == null) ? null : new FirebaseRecyclerAdapter<User, UserResultsHolder>(options) {

            @Override
            public UserResultsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = newItemView(parent, android.R.layout.simple_list_item_multiple_choice);
                return new UserResultsHolder(itemView);
            }

            @Override
            protected void onBindViewHolder(UserResultsHolder holder, int position, User model) {
                holder.bind(model);
            }
        };
        return adapter;

    }

    public void updateSearch() {
        recyclerView.swapAdapter(newAdapter(QueryPreferences.getPrefUsersSearchQuery(getContext())), true);

    }


}
