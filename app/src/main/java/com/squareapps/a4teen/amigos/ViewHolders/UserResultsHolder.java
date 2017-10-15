package com.squareapps.a4teen.amigos.ViewHolders;

import android.view.View;
import android.widget.CheckedTextView;

import com.squareapps.a4teen.amigos.Abstract.HolderBase;
import com.squareapps.a4teen.amigos.Common.Objects.User;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserResultsHolder extends HolderBase {


    @BindView(android.R.id.text1)
    CheckedTextView textView1;

    public UserResultsHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

    }

    public void bind(User model) {
        textView1.setText(model.getEmail());

    }


}