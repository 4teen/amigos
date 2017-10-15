package com.squareapps.a4teen.amigos.ViewHolders;

import android.view.View;
import android.widget.ImageView;

import com.squareapps.a4teen.amigos.Abstract.HolderBase;
import com.squareapps.a4teen.amigos.Common.Objects.Photo;

public class MediaHolder extends HolderBase {


    public MediaHolder(View itemView) {
        super(itemView);
    }

    public void bind(Photo model) {
        String photoUrl = model.getPhotoUrl();
        setImageView(photoUrl, (ImageView) itemView);

    }


}