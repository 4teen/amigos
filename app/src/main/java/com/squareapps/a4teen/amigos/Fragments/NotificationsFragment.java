package com.squareapps.a4teen.amigos.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.squareapps.a4teen.amigos.Common.POJOS.Notification;
import com.squareapps.a4teen.amigos.R;
import com.squareapps.a4teen.amigos.ViewHolders.NotificationHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.squareapps.a4teen.amigos.Common.Contract.NOTIFICATIONS;

public class NotificationsFragment extends FragmentBase implements View.OnClickListener {
    private final String TAG = getSimpleName(this);

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    FirebaseRecyclerAdapter<Notification, NotificationHolder> adapter;


    public static NotificationsFragment newInstance(Bundle bundle) {
        NotificationsFragment fragment = new NotificationsFragment();
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

        setToolbar(toolbar, R.drawable.ic_arrow_back_black_24dp);

        LinearLayoutManager ll = new LinearLayoutManager(getActivity());
        ll.setStackFromEnd(true);
        ll.setReverseLayout(true);

        mRecyclerView.setLayoutManager(ll);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);

        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setAdapter(newAdapter());
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private FirebaseRecyclerAdapter<Notification, NotificationHolder> newAdapter() {
        final Query query = getDataRef()
                .child(NOTIFICATIONS)
                .child(getUid());

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Notification>()
                .setQuery(query, Notification.class)
                .setLifecycleOwner(((AppCompatActivity) getActivity()))
                .build();

        adapter = new FirebaseRecyclerAdapter<Notification, NotificationHolder>(options) {

            @Override
            public NotificationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = newItemView(parent, R.layout.notifications_list_item);
                itemView.setOnClickListener(NotificationsFragment.this);
                return new NotificationHolder(itemView);
            }

            @Override
            protected void onBindViewHolder(NotificationHolder holder, int position, Notification model) {
                holder.getBinding().setNotification(model);
                holder.getBinding().executePendingBindings();
            }

        };
        return adapter;
    }

    @Override
    public void onClick(View v) {
        int pos = mRecyclerView.getChildAdapterPosition(v);
        Notification notification = adapter.getItem(pos);
        if (notification.getType().equals("groupInvite")) {
            Snackbar.make(v.getRootView(), "notification is a group Add", Snackbar.LENGTH_SHORT);
        } else {
            Snackbar.make(v.getRootView(), "notification is not a group Add", Snackbar.LENGTH_SHORT);
        }

    }
}
