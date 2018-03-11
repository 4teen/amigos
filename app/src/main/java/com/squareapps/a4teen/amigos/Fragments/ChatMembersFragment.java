package com.squareapps.a4teen.amigos.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.squareapps.a4teen.amigos.Abstract.FragmentBase;
import com.squareapps.a4teen.amigos.Activities.ChatMembersActivity;
import com.squareapps.a4teen.amigos.BR;
import com.squareapps.a4teen.amigos.Common.POJOS.User;
import com.squareapps.a4teen.amigos.R;
import com.squareapps.a4teen.amigos.ViewHolders.UserHolder;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.squareapps.a4teen.amigos.Common.Contract.GROUPS;
import static com.squareapps.a4teen.amigos.Common.Contract.MEMBERS;
import static com.squareapps.a4teen.amigos.Common.Contract.USERS_IN_APP;

public class ChatMembersFragment extends FragmentBase {

    public static final String EXTRA_PARAM1 = "extra_param_1";
    public static final String ACTION = "Action";
    public static final String ACTION_SEARCH_CHAT_MEMEBRS = "search chat members";
    public static final String ACTION_SEARCH_COURSE_MEMEBRS = "search course members";

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    public static ChatMembersFragment newInstance(Bundle bundle) {
        ChatMembersFragment fragment = new ChatMembersFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view_tool_bar, container, false);
        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(newAdapter());

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        return view;
    }

    @NonNull
    private FirebaseRecyclerAdapter<User, UserHolder> newAdapter() {
        Bundle bundle = getArguments();
        String groupId = bundle.getString(EXTRA_PARAM1);
        assert groupId != null;

        Query keysRef = getDataRef().child(GROUPS).child(groupId).child(MEMBERS);

        DatabaseReference dataRef = getDataRef().child(USERS_IN_APP);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<User>()
                .setIndexedQuery(keysRef, dataRef, User.class)
                .setLifecycleOwner(((ChatMembersActivity) getActivity()))
                .build();

        return new FirebaseRecyclerAdapter<User, UserHolder>(options) {
            @Override
            protected void onBindViewHolder(UserHolder holder, int position, User model) {
                holder.getBinding().setVariable(BR.contact, model);
                holder.getBinding().executePendingBindings();
            }

            @Override
            public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new UserHolder(newItemView(parent, R.layout.contacts_list_item_add_members_to_groups));
            }
        };


    }

    private
    @ActionType
    String getAction() {
        String action = getArguments().get(ACTION).toString();
        return action.equals(ACTION_SEARCH_CHAT_MEMEBRS) ? ACTION_SEARCH_CHAT_MEMEBRS : ACTION_SEARCH_COURSE_MEMEBRS;
    }


    @StringDef({ACTION_SEARCH_CHAT_MEMEBRS, ACTION_SEARCH_COURSE_MEMEBRS})
    @Retention(RetentionPolicy.SOURCE)
    @interface ActionType {
    }

}
