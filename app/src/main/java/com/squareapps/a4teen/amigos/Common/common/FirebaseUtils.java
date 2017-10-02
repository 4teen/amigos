package com.squareapps.a4teen.amigos.Common.common;

import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseUtils {


    public static void setImageView(Uri imageUri, ImageView imageView) {
        if (imageUri == null) {
            return;
        }
        imageView.setImageURI(imageUri);
    }

    public static void setImageView(String url, ImageView imageView) {
        if (url == null) {
            return;
        }
        if (url.startsWith("gs")) {
            StorageReference storageReference = FirebaseStorage
                    .getInstance()
                    .getReferenceFromUrl(url);

            // Load the image using Glide
            Glide.with(imageView.getContext() /* context */)
                    .using(new FirebaseImageLoader())
                    .load(storageReference)
                    .fitCenter()
                    .into(imageView);
        } else {
            Glide.with(imageView.getContext())
                    .load(url)
                    .fitCenter()
                    .into(imageView);
        }
    }


}