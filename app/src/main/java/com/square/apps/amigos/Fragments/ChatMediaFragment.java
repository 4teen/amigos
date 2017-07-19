package com.square.apps.amigos.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.square.apps.amigos.R;
import com.square.apps.amigos.common.common.FirebaseUtils;
import com.square.apps.amigos.common.common.Photo;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.square.apps.amigos.Contract.MEDIA;

public class ChatMediaFragment extends Fragment {

    public static final String TAG = "ChatMediaFragment";
    public static final String PARAM1 = "param1";
    @BindView(R.id.group_list_recycler_View)
    RecyclerView recyclerView;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<Photo, PhotoViewHolder> myRecyclerAdapter;
    private String groupId;

    public ChatMediaFragment() {
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

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_chat_members, container, false);
        ButterKnife.bind(this, view);


        myRecyclerAdapter = new FirebaseRecyclerAdapter<Photo, PhotoViewHolder>(Photo.class,
                R.layout.circle_image_view,
                PhotoViewHolder.class,
                databaseReference.child(MEDIA).child(groupId)) {
            @Override
            protected void populateViewHolder(PhotoViewHolder viewHolder, Photo model, int position) {
                viewHolder.bind(model);

            }
        };

        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        recyclerView.setAdapter(myRecyclerAdapter);

        return view;
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.circleImageView)
        CircleImageView imageView;
        String photoUrl;
        String owner;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Photo model) {
            photoUrl = model.getPhotoUrl();
            owner = model.getOwner();
            new FirebaseUtils().setImageView(photoUrl, imageView);

        }

    }
}
