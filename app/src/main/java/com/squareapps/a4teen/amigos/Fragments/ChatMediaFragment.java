package com.squareapps.a4teen.amigos.Fragments;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;
import com.squareapps.a4teen.amigos.Abstract.FragmentBase;
import com.squareapps.a4teen.amigos.Activities.ChatMediaActivity;
import com.squareapps.a4teen.amigos.Common.Objects.Photo;
import com.squareapps.a4teen.amigos.R;
import com.squareapps.a4teen.amigos.ViewHolders.MediaHolder;

import static com.squareapps.a4teen.amigos.Common.Contract.MEDIA;


public class ChatMediaFragment extends FragmentBase {

    public static final String PARAM1 = "param1";

    private FirebaseRecyclerAdapter<Photo, MediaHolder> myRecyclerAdapter;
    private String groupId;


    public static ChatMediaFragment newInstance(String param) {
        ChatMediaFragment fragment = new ChatMediaFragment();
        Bundle args = new Bundle();
        args.putString(PARAM1, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            groupId = getArguments().getString(PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_chat_members, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);

        Query dataRef = getDataRef().child(MEDIA).child(groupId);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Photo>()
                .setQuery(dataRef, Photo.class)
                .setLifecycleOwner(((ChatMediaActivity) getActivity()))
                .build();


        myRecyclerAdapter = new FirebaseRecyclerAdapter<Photo, MediaHolder>(options) {
            @Override
            protected void onBindViewHolder(MediaHolder holder, int position, Photo model) {
                holder.bind(model);
            }

            @Override
            public MediaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new MediaHolder(newItemView(parent, R.layout.image_view_object));
            }
        };

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(myRecyclerAdapter);

        return view;
    }

}
