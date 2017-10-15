package com.squareapps.a4teen.amigos.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.squareapps.a4teen.amigos.Abstract.FragmentBase;
import com.squareapps.a4teen.amigos.Activities.ChatMembersActivity;
import com.squareapps.a4teen.amigos.Common.Objects.User;
import com.squareapps.a4teen.amigos.R;
import com.squareapps.a4teen.amigos.ViewHolders.UserHolder;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.squareapps.a4teen.amigos.Common.Contract.MEMBERS;
import static com.squareapps.a4teen.amigos.Common.Contract.STUDENTS;
import static com.squareapps.a4teen.amigos.Common.Contract.USERS;


/**
 * A placeholder fragment containing a simple view.
 */
public class ChatMembersFragment extends FragmentBase {

    public static final String EXTRA_PARAM1 = "extra_param_1";
    public static final String ACTION = "Action";
    public static final String ACTION_SEARCH_CHAT_MEMEBRS = "search chat " + MEMBERS;
    public static final String ACTION_SEARCH_COURSE_MEMEBRS = "search course " + MEMBERS;


    private DatabaseReference myDatabaseReference;
    private FirebaseRecyclerAdapter<User, UserHolder> recyclerAdapter;

    // Set up FirebaseRecyclerAdapter with the Querys
    private static final DatabaseReference dataRef = getDataRef().child(USERS);


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.chat_members_toolbar)
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

        myDatabaseReference = getDataRef();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_chat_members, container, false);
        ButterKnife.bind(this, view);

        setToolbar(toolbar, R.drawable.ic_arrow_back_black_24dp);
        setupRecyclerAdapter();
        return view;
    }

    private void setupRecyclerAdapter() {
        Bundle bundle = getArguments();
        String param1 = bundle.getString(EXTRA_PARAM1);
        assert param1 != null;

        Log.d("group id ", param1);

        Query keysRef;
        if (getAction().equals(ACTION_SEARCH_CHAT_MEMEBRS)) {
            keysRef = myDatabaseReference.child(MEMBERS).child(param1);
        } else {
            keysRef = myDatabaseReference.child(STUDENTS).child(param1); //keyref - location of keys
        }

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<User>()
                .setIndexedQuery(keysRef, dataRef, User.class)
                .setLifecycleOwner(((ChatMembersActivity) getActivity()))
                .build();

        recyclerAdapter = new FirebaseRecyclerAdapter<User, UserHolder>(options) {
            @Override
            protected void onBindViewHolder(UserHolder holder, int position, User model) {
                holder.bind(model);
            }

            @Override
            public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new UserHolder(newItemView(parent, R.layout.circle_image_view));
            }
        };

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(recyclerAdapter);
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
