package com.squareapps.a4teen.amigos.Abstract;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public abstract class HolderBase<T> extends RecyclerView.ViewHolder {

    private static final String TAG = "Viewholder";

    public HolderBase(View itemView) {
        super(itemView);
    }


    protected void setImageView(String url, ImageView imageView) {
        if (url == null) {
            Log.e(TAG, "imageUrl is null");
            return;
        } else if (imageView == null) {
            Log.e(TAG, " imageView is NUll");
            return;
        }

        url = Uri.decode(url);

        if (url.startsWith("gs")) {
            StorageReference storageReference = FirebaseStorage
                    .getInstance()
                    .getReferenceFromUrl(url);

            // Load the image using Glide
            Glide.with(itemView.getContext())
                    .using(new FirebaseImageLoader())
                    .load(storageReference)
                    .fitCenter()
                    .into(imageView);
        } else {
            Log.e(TAG, "imageUrl does not start with gs");
        }
    }

    public DatabaseReference getDataRef(){
        return FirebaseDatabase.getInstance().getReference();
    }
}
