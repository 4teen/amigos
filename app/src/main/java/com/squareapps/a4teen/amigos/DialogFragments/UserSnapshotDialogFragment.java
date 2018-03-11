package com.squareapps.a4teen.amigos.DialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareapps.a4teen.amigos.Abstract.BaseDialogFragment;
import com.squareapps.a4teen.amigos.Fragments.AddToGroupFragment;
import com.squareapps.a4teen.amigos.R;

import static com.squareapps.a4teen.amigos.Common.Contract.User.AVATAR_URL;
import static com.squareapps.a4teen.amigos.Common.Contract.User.NAME;


public class UserSnapshotDialogFragment extends BaseDialogFragment implements View.OnClickListener {

    public static UserSnapshotDialogFragment newInstance(Bundle bundle) {
        UserSnapshotDialogFragment fragment = new UserSnapshotDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.user_snapshop;
    }

    @Override
    protected Dialog createDialog() {

        ImageView imageView = dialogView.findViewById(R.id.header_image_view);
        TextView name = dialogView.findViewById(R.id.name);

        dialogView.findViewById(R.id.add_to_group).setOnClickListener(this);

        name.setText(getArguments().getString(NAME));
        String avatarUrl = getArguments().getString(AVATAR_URL);
        setImageView(getActivity(), avatarUrl, imageView);

        return new AlertDialog.Builder(getActivity())
                .setView(dialogView)
                .create();
    }


    public void setImageView(Context context, String url, ImageView imageView) {
        if (url == null) {
            return;
        }

        url = Uri.decode(url);

        if (url.startsWith("gs")) {
            StorageReference storageReference = FirebaseStorage
                    .getInstance()
                    .getReferenceFromUrl(url);

       /*     // Load the image using Glide
            GlideApp.with(context.getApplicationContext())
                    .load(storageReference)
                    .fitCenter()
                    .into(imageView);*/
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_to_group: {
                Log.d("UserSnapshot", "onclicked pressed");
                AddToGroupFragment addToGroupFragment = AddToGroupFragment.newInstance(getArguments());
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, addToGroupFragment)
                        .commit();

                dismiss();
            }
        }
    }
}

