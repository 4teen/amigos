package com.squareapps.a4teen.amigos.Abstract;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public abstract class HolderBase extends RecyclerView.ViewHolder {

    private static final String TAG = "Viewholder";

    public HolderBase(View itemView) {
        super(itemView);
    }


    protected void setImageView(final Context context, final String url, final ImageView imageView) {
        if (url == null) {
            Log.e(TAG, "imageUrl is null");
            return;
        } else if (imageView == null) {
            Log.e(TAG, " imageView is NUll");
            return;
        }

        final String decodedUrl = Uri.decode(url);

        if (url.startsWith("gs")) {
            StorageReference storageReference = FirebaseStorage
                    .getInstance()
                    .getReferenceFromUrl(decodedUrl);

            // Load the image using Glide

           /* GlideApp.with(context.getApplicationContext())
                    .load(storageReference)
                    .fitCenter()
                    .into(imageView);*/
        } else {
            Log.e(TAG, "imageUrl does not start with gs");
        }
    }

    public static DatabaseReference getDataRef() {
        return FirebaseDatabase.getInstance().getReference();
    }

    public static String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
