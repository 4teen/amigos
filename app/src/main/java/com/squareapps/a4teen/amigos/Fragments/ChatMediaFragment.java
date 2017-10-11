package com.squareapps.a4teen.amigos.Fragments;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.squareapps.a4teen.amigos.Abstract.FragmentBase;
import com.squareapps.a4teen.amigos.Common.Objects.Photo;
import com.squareapps.a4teen.amigos.R;
import com.squareapps.a4teen.amigos.ViewHolders.MediaHolder;

import static com.squareapps.a4teen.amigos.Common.Contract.MEDIA;


public class ChatMediaFragment extends FragmentBase {

    public static final String PARAM1 = "param1";

    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<Photo, MediaHolder> myRecyclerAdapter;
    private String groupId;

    public ChatMediaFragment() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myRecyclerAdapter.cleanup();
    }

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


        databaseReference = getDataRef();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_chat_members, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.group_list_recycler_View);

        myRecyclerAdapter = new FirebaseRecyclerAdapter<Photo, MediaHolder>(Photo.class,
                R.layout.image_view_object,
                MediaHolder.class,
                databaseReference.child(MEDIA).child(groupId)) {
            @Override
            protected void populateViewHolder(MediaHolder viewHolder, Photo model, int position) {
                viewHolder.bind(model);
            }
        };

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(myRecyclerAdapter);

        return view;
    }

}
