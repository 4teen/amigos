package com.squareapps.a4teen.amigos.ViewHolders;

import android.view.View;
import android.widget.TextView;

import com.squareapps.a4teen.amigos.Abstract.HolderBase;
import com.squareapps.a4teen.amigos.Common.Objects.User;
import com.squareapps.a4teen.amigos.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class UserHolder extends HolderBase {
    @BindView(R.id.circleImageView)
    CircleImageView circleImageView;
    @BindView(R.id.cirlceImage_text1)
    TextView textview1;

    public UserHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(User model) {
        String photoUrl = model.getAvatarUrl();

        setImageView(photoUrl, circleImageView);

        textview1.setText(model.getName());

    }
}
