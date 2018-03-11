package com.squareapps.a4teen.amigos.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
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
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.squareapps.a4teen.amigos.Abstract.FragmentBase;
import com.squareapps.a4teen.amigos.Abstract.GlideApp;
import com.squareapps.a4teen.amigos.Activities.CreateGroupActivity;
import com.squareapps.a4teen.amigos.Common.POJOS.Group;
import com.squareapps.a4teen.amigos.Common.POJOS.Invitation;
import com.squareapps.a4teen.amigos.Common.POJOS.User;
import com.squareapps.a4teen.amigos.Common.Utils.QueryPreferences;
import com.squareapps.a4teen.amigos.R;
import com.squareapps.a4teen.amigos.ViewHolders.UserHolder;
import com.squareapps.a4teen.amigos.databinding.ContactsListItemAddMembersToGroupsBinding;
import com.squareapps.a4teen.amigos.databinding.CreateGroupUserListItemBinding;

import java.util.ArrayList;
import java.util.HashMap;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.squareapps.a4teen.amigos.Common.Contract.GROUPS;
import static com.squareapps.a4teen.amigos.Common.Contract.GROUP_INVITATIONS;
import static com.squareapps.a4teen.amigos.Common.Contract.Group.GROUP_ID;
import static com.squareapps.a4teen.amigos.Common.Contract.Group.GROUP_NAME;
import static com.squareapps.a4teen.amigos.Common.Contract.TIMESTAMP;
import static com.squareapps.a4teen.amigos.Common.Contract.USERS_IN_APP;
import static com.squareapps.a4teen.amigos.Common.Contract.User.NAME;
import static java.io.File.separator;

/**
 * Created by y-pol on 1/14/2018.s
 */

public class CreateGroupFragment extends FragmentBase implements View.OnClickListener {

    private final String TAG = getSimpleName(this);
    private final ArrayList<User> groupMembers = new ArrayList<>();
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<User, UserHolder> adapter;
    private LinearLayout linearLayout;
    private EditText groupName;
    private TextView groupMembersTile;

    public static CreateGroupFragment newInstance(Bundle bundle) {

        CreateGroupFragment fragment = new CreateGroupFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        QueryPreferences.setPrefUsersSearchQuery(getContext(), null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.create_group, container, false);

        linearLayout = rootView.findViewById(R.id.horizontalScrollViewLL);
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        groupName = rootView.findViewById(R.id.groupName);
        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        groupMembersTile = rootView.findViewById(R.id.text1);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);

        recyclerView.addItemDecoration(itemDecoration);

        adapter = newAdapter(QueryPreferences.getPrefSearchQuery(getContext()));

        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(view -> {
            String gname = groupName.getText().toString();

            if (gname.isEmpty() || gname.equals(" ")) {
                groupName.setError("Invalid group name");
            } else {
                createNewGroup(gname);
            }

        });

        return rootView;

    }

    private void createNewGroup(final String gname) {

        HashMap<String, Object> members = new HashMap<>();
        members.put(getUid(), true);

        final String groupId = getDataRef()
                .child(GROUPS).push().getKey();

        Group newGroup = new Group(gname, getUid(), groupId);
        newGroup.setMembers(members);

        getDataRef().child(GROUPS)
                .child(groupId)
                .setValue(newGroup)
                .addOnCompleteListener(task -> {
                    if (task.isComplete()) {

                        sendGroupInvitations(groupId, gname);

                        Intent data = new Intent();
                        data.putExtra(GROUP_NAME, gname);
                        getActivity().setResult(Activity.RESULT_OK, data);
                        getActivity().finish();

                    }
                });
    }


    private void sendGroupInvitations(String groupID, String groupName) {
        if (!groupMembers.isEmpty()) {

            //[START_EXCLUE]
            //removes my id so I won't recevie a group invitation to my own group
            groupMembers.remove(0);
            //[END_EXCLUE]

            for (User user : groupMembers) {
                DatabaseReference reference = getDataRef()
                        .child(GROUP_INVITATIONS)
                        .child(user.getId());

                String invitationId = reference.push().getKey();

                Bundle bundle = new Bundle();

                bundle.putString(GROUP_ID, groupID);
                bundle.putString(GROUP_NAME, groupName);
                bundle.putString("groupAvatar", "");
                bundle.putString("senderUid", getUid());
                bundle.putString("senderName", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                bundle.putString("senderAvatarUrl", getAvatarUrl());

                Invitation invitation = new Invitation(bundle);
                reference.child(invitationId).setValue(invitation);

                HashMap<String, Object> updates = new HashMap<>();
                updates.put(invitationId + separator + TIMESTAMP, ServerValue.TIMESTAMP);
                updates.put(invitationId + separator + "status", "pending");

                reference.updateChildren(updates);

            }
        }
    }

    private FirebaseRecyclerAdapter<User, UserHolder> newAdapter(final String query) {

        Log.d(TAG, query == null ? "null" : query);


        Query dataRef = getDataRef().child(USERS_IN_APP)
                .orderByChild(NAME)
                .startAt(query)
                .endAt(query + "\uf8ff");

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(dataRef, snapshot -> {
                    User newUser = snapshot.getValue(User.class);
                    newUser.setId(snapshot.getKey());
                    return newUser;
                })
                .setLifecycleOwner(((CreateGroupActivity) getActivity()))
                .build();

        return (query == null) ? null : new FirebaseRecyclerAdapter<User, UserHolder>(options) {

            @Override
            public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = newItemView(parent, R.layout.contacts_list_item_add_members_to_groups);
                view.setOnClickListener(CreateGroupFragment.this);
                return new UserHolder(view);
            }

            @Override
            protected void onBindViewHolder(UserHolder holder, int position, User model) {
                ((ContactsListItemAddMembersToGroupsBinding) holder.getBinding()).setContact(model);
                holder.getBinding().executePendingBindings();

            }

            @Override
            public void onViewRecycled(UserHolder holder) {
                super.onViewRecycled(holder);
                GlideApp.with(getContext()).clear(((ContactsListItemAddMembersToGroupsBinding) holder.getBinding()).avatar);
            }
        };

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        // Set listeners for SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Nothing needs to happen when the user submits the search string
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
                return true;


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

    private void updateSearch() {
        String query = QueryPreferences.getPrefUsersSearchQuery(getContext());
        adapter = newAdapter(query);
        recyclerView.swapAdapter(adapter, false);
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

        if (groupMembers.contains(user)) {
            int pos = groupMembers.indexOf(user);
            linearLayout.removeViewAt(pos);
            groupMembers.remove(user);
        } else {
            linearLayout.addView(binding.getRoot());
            groupMembers.add(user);
        }

        ((HorizontalScrollView) linearLayout.getParent())
                .setVisibility(groupMembers.isEmpty() ? GONE : VISIBLE);

        groupMembersTile.setVisibility(groupMembers.isEmpty() ? GONE : VISIBLE);

    }
}
