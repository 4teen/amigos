package com.squareapps.a4teen.amigos.ViewHolders;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import com.squareapps.a4teen.amigos.Abstract.HolderBase;
import com.squareapps.a4teen.amigos.Activities.DetailActivity;
import com.squareapps.a4teen.amigos.Common.Objects.Course;
import com.squareapps.a4teen.amigos.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CourseListHolder extends HolderBase<Course> implements View.OnClickListener, View.OnCreateContextMenuListener {

    /**
     * RecclerVIew.Adapter calls this class to create a viewHolder
     */

    @BindView(R.id.course_list_item_text1)
    TextView mTitleTextView;
    @BindView(R.id.course_list_item_text2)
    TextView mSubtitleTextView;

    private Course course;
    private Callbacks callbacks;

    public CourseListHolder(View itemView, Fragment fragment) {
        super(itemView);
        callbacks = (Callbacks) fragment;
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
    }


    public void bind(Course course) {
        this.course = course;
        mTitleTextView.setText(course.getTitle());
        mSubtitleTextView.setText(course.getCode() + "| " + course.getDepartment());
    }

    @Override
    public void onClick(View view) {
        callbacks.onItemSelected(getAdapterPosition(), itemView);
        Context context = view.getContext();
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(DetailActivity.COURSEID, course.getCode());
        context.startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Class options");
        menu.add(0, R.id.delete, getAdapterPosition(), "Delete Class");

    }

    public interface Callbacks {
        void onItemSelected(int pos, View itemView);
    }



}