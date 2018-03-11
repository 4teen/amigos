package com.squareapps.a4teen.amigos.Abstract;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;

import com.squareapps.a4teen.amigos.Common.POJOS.Photo;
import com.squareapps.a4teen.amigos.databinding.ImageViewObjectPreferenceBinding;


import java.util.Objects;


/**
 * Created by y-pol on 1/16/2018.s
 */

public class ChangeGroupAvatarPreference extends Preference {

    private GroupSettingsCallback callbacks;

    private Photo myPhoto;

    public void setPhotoUrl(String url) {
        myPhoto.setPhotoUrl(url);
    }

    public ChangeGroupAvatarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        callbacks = (GroupSettingsCallback) context;
        myPhoto = new Photo();
        myPhoto.setPhotoUrl(callbacks.getAvatarUrl());
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        ImageViewObjectPreferenceBinding binding = DataBindingUtil.bind(view);
        binding.setPhoto(myPhoto);
        binding.executePendingBindings();
    }


    @Override
    protected void onClick() {
        if (!Objects.isNull(callbacks))
            callbacks.selectImage();

    }

}
