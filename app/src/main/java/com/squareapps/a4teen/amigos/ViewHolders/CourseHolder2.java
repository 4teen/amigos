package com.squareapps.a4teen.amigos.ViewHolders;

import android.app.Fragment;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.squareapps.a4teen.amigos.Abstract.HolderBase;
import com.squareapps.a4teen.amigos.Common.Objects.Course;
import com.squareapps.a4teen.amigos.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CourseHolder2 extends HolderBase implements View.OnClickListener {


    @BindView(R.id.course_list_item_text1)
    TextView mTitleTextView;
    @BindView(R.id.course_list_item_text2)
    TextView mSubtitleTextView;

    private Callbacks callbacks;

    private Course course;

    private ActionMode.Callback actionModeCallback;
    private static ActionMode mActionMode;

    public CourseHolder2(View v, Fragment fragment) {
        super(v);
        callbacks = (Callbacks) fragment;

        actionModeCallback = new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.search_course_menu, menu);
                mode.setTitle("| Selected");
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                callbacks.updateActionModeCounter(mode, menu);
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add_class:
                        callbacks.onAddItem(course);
                        return true;

                    default:
                        return false;
                }


            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                mActionMode = null;
                callbacks.onDestoryActionBar();
            }
        };

        v.setOnClickListener(this);
        ButterKnife.bind(this, v);

    }

    public void bind(Course course) {
        this.course = course;
        String courseCode = course.getCode();
        mTitleTextView.setText(course.getTitle());
        mSubtitleTextView.setText(courseCode + "| " + course.getDepartment());
    }

    @Override
    public void onClick(View view) {
        callbacks.onItemSelected(getAdapterPosition(), course, itemView);

    }

    public void dismissActionBar() {
        if (mActionMode != null)
            mActionMode.finish();

    }

    public void startActionMode() {
        if (mActionMode == null)
            mActionMode = itemView.startActionMode(actionModeCallback);
        else {
            mActionMode.invalidate();
        }
    }

    public interface Callbacks {
        void onItemSelected(int pos, Course course, View itemView);

        void onAddItem(Course course);

        void onDestoryActionBar();

        void updateActionModeCounter(ActionMode Mode, Menu menu);
    }


}