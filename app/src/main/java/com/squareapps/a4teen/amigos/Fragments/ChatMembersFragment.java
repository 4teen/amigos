package com.squareapps.a4teen.amigos.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseIndexRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.squareapps.a4teen.amigos.Activities.LoginActivity;
import com.squareapps.a4teen.amigos.Common.Objects.User;
import com.squareapps.a4teen.amigos.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.squareapps.a4teen.amigos.Common.Contract.MEMBERS;
import static com.squareapps.a4teen.amigos.Common.Contract.STUDENTS;
import static com.squareapps.a4teen.amigos.Common.Contract.USERS;


/**
 * A placeholder fragment containing a simple view.
 */
public class ChatMembersFragment extends Fragment {

    public static final String EXTRA_PARAM1 = "extra_param_1";
    public static final String ACTION = "Action";
    public static final String ACTION_SEARCH_CHAT_MEMEBRS = "search chat " + MEMBERS;
    public static final String ACTION_SEARCH_COURSE_MEMEBRS = "search course " + MEMBERS;
    @BindView(R.id.group_list_recycler_View)
    RecyclerView recyclerView;
    @BindView(R.id.chat_members_toolbar)
    Toolbar toolbar;
    @BindView(R.id.chat_members_coordinatingLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.chat_members_fab)
    FloatingActionButton fab;
    private FirebaseRecyclerAdapter<User, UserViewHolder> mRecyclerAdapter;
    private DatabaseReference myDatabaseReference;
    private FirebaseUser firebaseUser;
    private GridLayoutManager gridLayoutManager;

    public ChatMembersFragment() {
    }

    public static ChatMembersFragment newInstance(Bundle bundle) {
        ChatMembersFragment fragment = new ChatMembersFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (firebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRecyclerAdapter.cleanup();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        myDatabaseReference = FirebaseDatabase.getInstance().getReference();

        gridLayoutManager = new GridLayoutManager(getActivity(), 2);


    }

    private
    @ActionType
    String getAction() {
        String action = getArguments().get(ACTION).toString();
        return action.equals(ACTION_SEARCH_CHAT_MEMEBRS) ? ACTION_SEARCH_CHAT_MEMEBRS : ACTION_SEARCH_COURSE_MEMEBRS;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_chat_members, container, false);
        ButterKnife.bind(this, view);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setupRecyclerAdapter();
        return view;
    }

    private void setupRecyclerAdapter() {
        Bundle bundle = getArguments();
        String param1 = bundle.getString(EXTRA_PARAM1);


        if (getAction().equals(ACTION_SEARCH_CHAT_MEMEBRS)) {
            mRecyclerAdapter = new FirebaseIndexRecyclerAdapter<User, UserViewHolder>(
                    User.class,
                    R.layout.circle_image_view,
                    UserViewHolder.class,
                    myDatabaseReference.child(MEMBERS).child(param1), //keyref - location of keys
                    myDatabaseReference.child(USERS) //dataref - location of data
            ) {

                @Override
                protected User parseSnapshot(DataSnapshot snapshot) {
                    User user1 = super.parseSnapshot(snapshot);
                    Log.d(snapshot.getKey(), "snapshotKey");
                    if (user1 != null) {
                        user1.setId(snapshot.getKey());
                    }
                    return user1;
                }

                @Override
                protected void populateViewHolder(UserViewHolder viewHolder, User model, int position) {
                    if (model != null) {
                        viewHolder.bind(model);

                    }

                }
            };
        } else {
            mRecyclerAdapter = new FirebaseIndexRecyclerAdapter<User, UserViewHolder>(
                    User.class,
                    R.layout.circle_image_view,
                    UserViewHolder.class,
                    myDatabaseReference.child(STUDENTS).child(param1), //keyref - location of keys
                    myDatabaseReference.child(USERS) //dataref - location of data
            ) {

                @Override
                protected User parseSnapshot(DataSnapshot snapshot) {
                    User user1 = super.parseSnapshot(snapshot);
                    Log.d(snapshot.getKey(), "snapshotKey");
                    if (user1 != null) {
                        user1.setId(snapshot.getKey());
                    }
                    return user1;
                }

                @Override
                protected void populateViewHolder(UserViewHolder viewHolder, User model, int position) {
                    if (model != null) {
                        viewHolder.bind(model);

                    }

                }
            };

        }

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(mRecyclerAdapter);
    }


    @StringDef({ACTION_SEARCH_CHAT_MEMEBRS,
            ACTION_SEARCH_COURSE_MEMEBRS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ActionType {
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.circleImageView)
        CircleImageView circleImageView;

        @BindView(R.id.cirlceImage_text1)
        TextView textview1;

        @BindView(R.id.circleImage_progressBar)
        ProgressBar progressBar;

        public UserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void bind(User model) {
            String photoUrl = model.getAvatarUrl();

            circleImageView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            Picasso.with(itemView.getContext())
                    .load(photoUrl)
                    .into(circleImageView, new Callback.EmptyCallback() {
                        @Override
                        public void onSuccess() {
                            progressBar.setVisibility(View.GONE);
                            circleImageView.setVisibility(View.VISIBLE);

                        }
                    });

            textview1.setText(model.getName());

        }
    }
}
