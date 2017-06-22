package com.square.apps.amigos.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.square.apps.amigos.Contract;
import com.square.apps.amigos.R;
import com.square.apps.amigos.common.common.Camera.PictureUtils;
import com.square.apps.amigos.common.common.db.DataProvider;


public class FriendFragment extends Fragment {

    @Nullable
    private String friendID;
    @Nullable
    private Callbacks mListener;

    @NonNull
    public static FriendFragment newInstance(String friendID) {
        FriendFragment fragment = new FriendFragment();
        Bundle args = new Bundle();
        args.putString(Contract.PRIMARY_KEY, friendID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context activity) {
        super.onAttach(activity);
        try {
            mListener = (Callbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement Callbacks");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            friendID = getArguments().getString(Contract.PRIMARY_KEY);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friend_profile, container, false);
        ImageView avatar = (ImageView) view.findViewById(R.id.friend_avatar);
        TextView friendName = (TextView) view.findViewById(R.id.friend_name_tv);
        TextView chat = (TextView) view.findViewById(R.id.chat_textView);


        String[] PROJECTION = new String[]{
                Contract.PROFILE_IMAGE,
                Contract.NAME,
                Contract.EMAIL,
        };

        Cursor cursor = getActivity()
                .getContentResolver()
                .query(Uri.withAppendedPath(DataProvider.CONTENT_URI_ALL_FRIENDS, friendID)
                        , PROJECTION, null, null, null);

        assert cursor != null;
        cursor.moveToFirst();
        String stringImage = cursor.getString(cursor.getColumnIndex(Contract.PROFILE_IMAGE));
        cursor.close();
        String name = cursor.getString(cursor.getColumnIndex(Contract.NAME));

        friendName.setText(name);
        Bitmap friendImage = PictureUtils.decodeBase64(stringImage);
        avatar.setImageBitmap(friendImage);

        setChatClickListener(chat);


        return view;
    }

    private void setChatClickListener(@NonNull TextView chat) {
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert mListener != null;
                mListener.onChatWithFriend();
            }
        });

    }

    private void setDeleteClickListener(@NonNull TextView deleteFriend) {
        deleteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert mListener != null;
                mListener.onDeleteFriend();
            }
        });
    }

    public interface Callbacks {
        void onChatWithFriend();

        void onDeleteFriend();
    }


}
