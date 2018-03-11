package com.squareapps.a4teen.amigos.Common.Utils;


import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareapps.a4teen.amigos.Abstract.GlideApp;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static com.squareapps.a4teen.amigos.Common.FirebaseListeners.users.LISTENER;

public class CustomDataBindingAdapters {

    @BindingAdapter({"bind:imageUrl", "bind:error"})
    public static void loadImage(ImageView view, String url, Drawable error) {
        if (Objects.isNull(url)) {
            Log.d("Download error ", "url is null");
        } else if (url.startsWith("gs")) {
            final String decodedUrl = Uri.decode(url);
            StorageReference storageReference = FirebaseStorage
                    .getInstance()
                    .getReferenceFromUrl(decodedUrl);

            // Load the image using Glide
            GlideApp.with(view)
                    .load(storageReference)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .into(view);
        } else if (URLUtil.isValidUrl(url)) {
            Log.d("Dowloading", url);
            Glide.with(view)
                    .load(url)
                    .into(view);

        } else {
            Log.d("Dowload Error", url + "url is invalid");
        }
    }

    @BindingAdapter("bind:timeStamp")
    public static void formatTimeStamp(TextView textView, Long timeStamp) {
        if (timeStamp == null) {
            return;
        }

        Date newDate = new Date(timeStamp);
        Date todaysDate = new Date();

        if (todaysDate.getDay() != newDate.getDay())
            textView.setText(DateFormat.getDateInstance(DateFormat.SHORT, Locale.US).format(newDate));
        else {
            textView.setText(DateFormat.getTimeInstance(DateFormat.SHORT, Locale.US).format(newDate));
        }

    }

    @BindingAdapter({"bind:ref"})
    public static void setRef(TextView textview, DatabaseReference reference) {
        reference.addChildEventListener(LISTENER);

    }

}
