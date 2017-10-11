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

import com.firebase.ui.database.FirebaseIndexRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.squareapps.a4teen.amigos.Abstract.FragmentBase;
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
    private FirebaseIndexRecyclerAdapter<User, UserHolder> recyclerAdapter;


    @BindView(R.id.group_list_recycler_View)
    RecyclerView recyclerView;
    @BindView(R.id.chat_members_toolbar)
    Toolbar toolbar;


    public static ChatMembersFragment newInstance(Bundle bundle) {
        ChatMembersFragment fragment = new ChatMembersFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        recyclerAdapter.cleanup();
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

        DatabaseReference locationKeysDataRef = null;
        DatabaseReference locationDataDataRef = myDatabaseReference.child(USERS);
        if (getAction().equals(ACTION_SEARCH_CHAT_MEMEBRS)) {
            locationKeysDataRef = myDatabaseReference.child(MEMBERS).child(param1);
        } else {
            locationKeysDataRef = myDatabaseReference.child(STUDENTS).child(param1); //keyref - location of keys
        }

        recyclerAdapter = new FirebaseIndexRecyclerAdapter<User, UserHolder>(User.class,
                R.layout.circle_image_view,
                UserHolder.class,
                locationKeysDataRef,
                locationDataDataRef) {
            @Override
            protected void populateViewHolder(UserHolder viewHolder, User model, int position) {
                viewHolder.bind(model);

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
