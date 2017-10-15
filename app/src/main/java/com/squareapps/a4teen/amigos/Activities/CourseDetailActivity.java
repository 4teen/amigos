package com.squareapps.a4teen.amigos.Activities;


import com.squareapps.a4teen.amigos.Abstract.SingleFragmentActivity;
import com.squareapps.a4teen.amigos.Fragments.CourseFragment;

public class CourseDetailActivity extends SingleFragmentActivity {
    public static final String COURSEID = "CourseDetailActivity";

    protected CourseFragment createFragment() {
        return CourseFragment.newInstance(getIntent().getStringExtra(COURSEID));
    }

}
