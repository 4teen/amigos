package com.square.apps.amigos.Activities;


import com.square.apps.amigos.Abstract.SingleFragmentActivity;
import com.square.apps.amigos.Fragments.CourseFragment;


public class DetailActivity extends SingleFragmentActivity {
    public static final String COURSEID = "DetailActivity";

    protected CourseFragment createFragment() {
        return CourseFragment.newInstance(getIntent().getStringExtra(COURSEID));
    }


}
