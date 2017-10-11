package com.squareapps.a4teen.amigos.ViewHolders;

import android.view.View;
import android.widget.TextView;

import com.squareapps.a4teen.amigos.Abstract.HolderBase;
import com.squareapps.a4teen.amigos.Common.Objects.School;
import com.squareapps.a4teen.amigos.Fragments.CollegePickerFragment;
import com.squareapps.a4teen.amigos.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SchoolHolder extends HolderBase<School> implements View.OnClickListener {

    @BindView(R.id.course_list_item_text1)
    TextView mTitleTextView;
    @BindView(R.id.course_list_item_text2)
    TextView mSubtitleTextView;

    private Callback callback;

    public interface Callback {
        void onItemClicked(View itemView, int pos);
    }

    public SchoolHolder(View itemView, CollegePickerFragment fragment) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);

        callback = fragment;
    }

    public void bind(School model) {
        mTitleTextView.setText(model.getInstitution_Name());
        mSubtitleTextView.setText(model.getCampus_Name());

    }

    @Override
    public void onClick(View v) {
        callback.onItemClicked(v, getAdapterPosition());
    }
}
