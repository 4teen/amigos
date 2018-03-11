package com.squareapps.a4teen.amigos.Fragments;


import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;
import com.squareapps.a4teen.amigos.Abstract.FragmentBase;
import com.squareapps.a4teen.amigos.Abstract.LeaveGroupDialogFragment;
import com.squareapps.a4teen.amigos.Activities.InvitationActivity;
import com.squareapps.a4teen.amigos.Activities.InvitationDetailActivity;
import com.squareapps.a4teen.amigos.Common.POJOS.Invitation;
import com.squareapps.a4teen.amigos.R;
import com.squareapps.a4teen.amigos.ViewHolders.InvitationHolder;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.squareapps.a4teen.amigos.Common.Contract.GROUPS;
import static com.squareapps.a4teen.amigos.Common.Contract.GROUP_INVITATIONS;
import static com.squareapps.a4teen.amigos.Common.Contract.MEMBERS;
import static com.squareapps.a4teen.amigos.Common.Contract.TIMESTAMP;
import static java.io.File.separator;

public class InvitationListFragment extends FragmentBase implements View.OnClickListener, LeaveGroupDialogFragment.DialogFragmentCallbacks {
    private final static int INVITATION_DETAIL = 10;
    public final String TAG = getSimpleName(InvitationListFragment.this);
    private final int REQUEST_GROUP = 0;
    @BindView(R.id.recycler_view)
    EmptyRecyclerView mRecyclerView;
    @BindView(R.id.emptyView)
    TextView emptyView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    private FirebaseRecyclerAdapter<Invitation, InvitationHolder> adapter;

    public static InvitationListFragment newInstance(Bundle bundle) {

        InvitationListFragment fragment = new InvitationListFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.empty_recycler_view, container, false);
        ButterKnife.bind(this, view);

        progressBar.setVisibility(View.VISIBLE);

        LinearLayoutManager ll = new LinearLayoutManager(getActivity());
        ll.setStackFromEnd(true);
        ll.setReverseLayout(true);

        mRecyclerView.setLayoutManager(ll);

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);

        mRecyclerView.addItemDecoration(itemDecoration);

        mRecyclerView.setAdapter(newAdapter());

        emptyView.setText("No invitations");
        mRecyclerView.setEmptyView(emptyView);

        registerForContextMenu(mRecyclerView);
        return view;
    }

    private FirebaseRecyclerAdapter<Invitation, InvitationHolder> newAdapter() {

        // Set up FirebaseRecyclerAdapter with the Querys
        final Query keysRef = getDataRef()
                .child(GROUP_INVITATIONS)
                .child(getUid());


        FirebaseRecyclerOptions<Invitation> options = new FirebaseRecyclerOptions.Builder<Invitation>()
                .setQuery(keysRef, Invitation.class)
                .setLifecycleOwner((InvitationActivity) getActivity())
                .build();

        adapter = new FirebaseRecyclerAdapter<Invitation, InvitationHolder>(options) {

            @Override
            public InvitationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = newItemView(parent, R.layout.invitation_list_item);
                view.setOnClickListener(InvitationListFragment.this);
                return new InvitationHolder(view);
            }

            @Override
            protected void onBindViewHolder(final InvitationHolder holder, final int position, final Invitation model) {
                holder.getBinding().setInvite(model);

                holder.getBinding().switch1.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        HashMap<String, Object> hashMap = new HashMap<>();

                        hashMap.put(GROUPS + separator + model.getGroupId() + separator + MEMBERS + separator + getUid(), true);
                        hashMap.put(GROUP_INVITATIONS + separator + getUid() + separator + getRef(position).getKey() + separator + "status", "approved");

                        getDataRef().updateChildren(hashMap);
                    } else {


                        // DialogFragment.show() will take care of adding the fragment
                        // in a transaction.  We also want to remove any currently showing
                        // dialog, so make our own transaction and take care of that here.
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                        if (prev != null) {
                            ft.remove(prev);
                        }
                        ft.addToBackStack(null);

                        Bundle bundle = new Bundle();
                        bundle.putInt("position", position);

                        LeaveGroupDialogFragment dialogFragment = LeaveGroupDialogFragment.newInstance(InvitationListFragment.this, bundle);

                        dialogFragment.setArguments(bundle);

                        dialogFragment.show(ft, "dialog");


                    }
                });

                holder.getBinding().executePendingBindings();

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
    public void onClick(View v) {
        int pos = mRecyclerView.getChildAdapterPosition(v);
        Invitation invitation = adapter.getItem(pos);

        Intent i = new Intent(getActivity(), InvitationDetailActivity.class);

        i.putExtra("groupId", invitation.getGroupId());
        i.putExtra("groupName", invitation.getGroupName());
        i.putExtra("groupAvatar", invitation.getGroupAvatarUrl());
        i.putExtra("senderUid", invitation.getSenderUid());
        i.putExtra("senderName", invitation.getSenderName());
        i.putExtra("senderAvatarUrl", invitation.getGroupAvatarUrl());
        i.putExtra("status", invitation.getStatus());
        i.putExtra(TIMESTAMP, invitation.getTimeStamp());

        startActivityForResult(i, INVITATION_DETAIL);


    }

    @Override
    public void onDialogPositiveClick(final DialogFragment dialog) {

        int pos = dialog.getArguments().getInt("position");
        Invitation invitation = adapter.getItem(pos);

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put(GROUPS + separator + invitation.getGroupId() + separator + MEMBERS + separator + getUid(), null);
        hashMap.put(GROUP_INVITATIONS + separator + getUid() + separator + adapter.getRef(pos).getKey() + separator + "status", "rejected");

        getDataRef().updateChildren(hashMap)
                .addOnCompleteListener(task -> dialog.dismiss());

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

        int pos = dialog.getArguments().getInt("position");
        dialog.dismiss();
        Invitation invitation = adapter.getItem(pos);
        invitation.setStatus("approved");
        dialog.dismiss();

    }
}
